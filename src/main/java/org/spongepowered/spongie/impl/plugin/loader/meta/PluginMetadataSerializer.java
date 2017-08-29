/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.loader.meta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class PluginMetadataSerializer {
    public static final PluginMetadataSerializer DEFAULT = new PluginMetadataSerializer(PluginMetadataCollectionAdapter.DEFAULT);
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final PluginMetadataCollectionAdapter adapter;

    private PluginMetadataSerializer(PluginMetadataCollectionAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Reads a {@link List} of {@link PluginMetadata} from the given
     * {@link InputStream}.
     *
     * @param in The input stream
     * @return The deserialized metadata list
     * @throws IOException If an error occurs while reading
     */
    public List<PluginMetadata> read(InputStream in) throws IOException {
        return this.adapter.fromJson(new BufferedReader(new InputStreamReader(in, CHARSET)));
    }
}
