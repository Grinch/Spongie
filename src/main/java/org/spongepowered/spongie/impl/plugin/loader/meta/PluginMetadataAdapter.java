package org.spongepowered.spongie.impl.plugin.loader.meta;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.istack.internal.Nullable;
import org.spongepowered.spongie.api.plugin.meta.PluginDependency;
import org.spongepowered.spongie.impl.plugin.meta.SpongiePluginDependency;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PluginMetadataAdapter extends TypeAdapter<PluginMetadata> {

    public static final PluginMetadataAdapter DEFAULT = new PluginMetadataAdapter(new Gson());

    private static final char VERSION_SEPARATOR = '@';

    private final Gson gson;

    public PluginMetadataAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, PluginMetadata value) throws IOException {
        out.beginObject();
        out.name("id").value(value.getId());
        writeIfPresent(out, "name", value.getName());
        writeIfPresent(out, "version", value.getVersion());
        writeIfPresent(out, "description", value.getDescription());

        if (!value.getAuthors().isEmpty()) {
            out.name("authors").beginArray();
            for (String author : value.getAuthors()) {
                out.value(author);
            }
            out.endArray();
        }

        final Map<PluginDependency.LoadOrder, Set<PluginDependency>> dependencies = value.groupDependenciesByLoadOrder();
        // Check if there are any dependencies we can't represent in the resulting file
        // (Optional dependencies with LoadOrder.NONE)
        final Set<PluginDependency> loadOrderNone = dependencies.get(PluginDependency.LoadOrder.NONE);
        if (loadOrderNone != null) {
            for (PluginDependency dependency : loadOrderNone) {
                if (dependency.isOptional()) {
                    throw new IllegalArgumentException("Cannot represent optional dependency with LoadOrder.NONE: " + dependency);
                }
            }
        }

        writeDependencies(out, "dependencies", dependencies.get(PluginDependency.LoadOrder.BEFORE));
        writeDependencies(out, "dependants", dependencies.get(PluginDependency.LoadOrder.AFTER));

        out.endObject();
    }

    @Override
    public PluginMetadata read(JsonReader in) throws IOException {
        in.beginObject();

        final Set<String> processedKeys = new HashSet<>();
        final PluginMetadata.Builder metadataBuilder = new PluginMetadata.Builder();
        String id;

        final Map<String, SpongiePluginDependency> requiredDependencies = new HashMap<>();

        while (in.hasNext()) {
            final String name = in.nextName();
            if (!processedKeys.add(name)) {
                throw new JsonParseException("Duplicate key '" + name + "' in " + in);
            }

            switch (name) {
                case "id":
                    id = in.nextString();
                    metadataBuilder.id(id);
                    break;
                case "name":
                    metadataBuilder.name(in.nextString());
                    break;
                case "version":
                    metadataBuilder.version(in.nextString());
                    break;
                case "description":
                    metadataBuilder.description(in.nextString());
                    break;
                case "authors":
                    in.beginArray();
                    while (in.hasNext()) {
                        metadataBuilder.addAuthor(in.nextString());
                    }
                    in.endArray();
                    break;
                case "dependencies":
                    readDependencies(in, metadataBuilder, PluginDependency.LoadOrder.BEFORE, requiredDependencies);
                    break;
                case "dependants":
                    readDependencies(in, metadataBuilder, PluginDependency.LoadOrder.AFTER, requiredDependencies);
                    break;
            }
        }
        return null;
    }

    private static void writeIfPresent(JsonWriter out, String key, @Nullable String value) throws IOException {
        if (value != null) {
            out.name(key).value(value);
        }
    }

    private static void writeDependencies(JsonWriter out, String key, @Nullable Set<PluginDependency> dependencies) throws IOException {
        if (dependencies != null && !dependencies.isEmpty()) {
            out.name(key).beginArray();
            for (PluginDependency dependency : dependencies) {
                writeDependency(out, dependency);
            }
            out.endArray();
        }
    }

    private static void writeDependency(JsonWriter out, PluginDependency dependency) throws IOException {
        if (dependency.getVersion() == null) {
            out.value(dependency.getId());
        } else {
            out.value(dependency.getId() + VERSION_SEPARATOR + dependency.getVersion());
        }
    }

    private static void readDependencies(JsonReader in, PluginMetadata.Builder metadataBuilder, PluginDependency.LoadOrder loadOrder,
            Map<String, SpongiePluginDependency> requiredDependencies) throws IOException {
        in.beginArray();
        while (in.hasNext()) {
            SpongiePluginDependency dependency = readDependency(in, loadOrder, true);

            // Make dependency required if we already know it is required
            final SpongiePluginDependency required = requiredDependencies.remove(dependency.getId());
            if (required != null) {
                if (required.getVersion() != null && !required.getVersion().equals(dependency.getVersion())) {
                    throw new IllegalArgumentException("Found conflicting version in required dependency: "
                            + dependency.getVersion() + " != " + required.getVersion());
                }

                dependency = dependency.required();
            }

            metadataBuilder.addDependency(dependency);
        }
        in.endArray();
    }

    private static SpongiePluginDependency readDependency(JsonReader in, PluginDependency.LoadOrder loadOrder, boolean optional) throws IOException {
        final String version = in.nextString();
        int pos = version.indexOf(VERSION_SEPARATOR);
        if (pos < 0) {
            return new SpongiePluginDependency(version, null, optional, loadOrder);
        } else {
            return new SpongiePluginDependency(version.substring(0, pos), version.substring(pos + 1), optional, loadOrder);
        }
    }
}
