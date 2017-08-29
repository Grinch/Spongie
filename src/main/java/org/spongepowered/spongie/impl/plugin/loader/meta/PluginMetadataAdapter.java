/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.loader.meta;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.spongepowered.spongie.api.plugin.meta.PluginDependency;
import org.spongepowered.spongie.impl.plugin.meta.SpongiePluginDependency;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PluginMetadataAdapter extends TypeAdapter<PluginMetadata> {

    static final PluginMetadataAdapter DEFAULT = new PluginMetadataAdapter();

    private static final char VERSION_SEPARATOR = '@';

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

    @Override
    public void write(JsonWriter out, PluginMetadata value) throws IOException {
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
}
