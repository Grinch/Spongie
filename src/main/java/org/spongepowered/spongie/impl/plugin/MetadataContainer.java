/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.spongie.api.plugin.PluginContainer;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadataSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class MetadataContainer {

    private final ImmutableMap<String, PluginMetadata> metadata;

    private MetadataContainer(ImmutableMap<String, PluginMetadata> metadata) {
        this.metadata = metadata;
    }

    static MetadataContainer load() {
        return load("");
    }

    private static MetadataContainer load(String path) {
        List<PluginMetadata> meta;

        path = path + '/' + PluginMetadata.STANDARD_FILENAME;
        try (InputStream in = MetadataContainer.class.getResourceAsStream(path)) {
            if (in == null) {
                // TODO Need a way to check for dev environment
                //                if (VanillaLaunch.ENVIRONMENT != DEVELOPMENT) {
                //                    throw new LaunchException("Unable to find metadata file at " + path);
                //                }

                return new MetadataContainer(ImmutableMap.of());
            }

            meta = PluginMetadataSerializer.DEFAULT.read(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load metadata", e);
        }

        final ImmutableMap.Builder<String, PluginMetadata> builder = ImmutableMap.builder();

        for (PluginMetadata m : meta) {
            builder.put(m.getId(), m);
        }

        return new MetadataContainer(builder.build());
    }

    public PluginMetadata get(String id, String name) {
        PluginMetadata meta = this.metadata.get(id);
        if (meta == null) {
            // TODO Need a way to check for dev environment
            //            if (VanillaLaunch.ENVIRONMENT != DEVELOPMENT) {
            //                throw new RuntimeException("Unable to find metadata for " + id);
            //            }

            meta = new PluginMetadata(id, name);
        }

        return meta;
    }

    PluginContainer createContainer(String id, String name, Optional<Path> source) {
        return new MetaPluginContainer(get(id, name), source);
    }
}
