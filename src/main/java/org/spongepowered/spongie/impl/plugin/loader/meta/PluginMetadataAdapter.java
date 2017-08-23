package org.spongepowered.spongie.impl.plugin.loader.meta;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.istack.internal.Nullable;

import java.io.IOException;

public class PluginMetadataAdapter extends TypeAdapter<PluginMetadata> {

    public static final PluginMetadataAdapter DEFAULT = new PluginMetadataAdapter(new Gson(), ImmutableMap.of());

    private static final char VERSION_SEPARATOR = '@';

    private final Gson gson;
    private final ImmutableMap<String, Class<?>> extensions;

    public PluginMetadataAdapter(Gson gson, ImmutableMap<String, Class<?>> extensions) {
        this.gson = gson;
        this.extensions = extensions;
    }

    @Override
    public void write(JsonWriter out, PluginMetadata value) throws IOException {
        out.beginObject();
        out.name("id").value(value.getId());
        writeIfPresent(out, "name", value.getName());
        writeIfPresent(out, "name", value.getName());
        writeIfPresent(out, "version", value.getVersion());
        writeIfPresent(out, "description", value.getDescription());
    }

    @Override
    public PluginMetadata read(JsonReader in) throws IOException {

        return null;
    }

    private static void writeIfPresent(JsonWriter out, String key, @Nullable String value) throws IOException {
        if (value != null) {
            out.name(key).value(value);
        }
    }
}
