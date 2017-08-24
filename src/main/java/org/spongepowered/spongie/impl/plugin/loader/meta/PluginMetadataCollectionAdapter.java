package org.spongepowered.spongie.impl.plugin.loader.meta;

import com.google.common.collect.ImmutableList;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

public final class PluginMetadataCollectionAdapter extends TypeAdapter<List<PluginMetadata>> {

    public static final PluginMetadataCollectionAdapter DEFAULT = new PluginMetadataCollectionAdapter(PluginMetadataAdapter.DEFAULT);
    private final PluginMetadataAdapter adapter;

    public PluginMetadataCollectionAdapter(PluginMetadataAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void write(JsonWriter out, List<PluginMetadata> value) throws IOException {
        out.beginArray();
        for (PluginMetadata meta : value) {
            this.adapter.write(out, meta);
        }
        out.endArray();
    }

    @Override
    public List<PluginMetadata> read(JsonReader in) throws IOException {
        in.beginArray();
        final ImmutableList.Builder<PluginMetadata> result = ImmutableList.builder();
        while (in.hasNext()) {
            result.add(this.adapter.read(in));
        }
        in.endArray();
        return result.build();
    }
}
