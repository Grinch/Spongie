/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin;

import com.google.inject.Injector;
import org.spongepowered.spongie.impl.inject.plugin.PluginModule;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;

import java.nio.file.Path;
import java.util.Optional;

public class SpongiePluginContainer extends MetaPluginContainer {

    private final Optional<?> instance;
    private final Injector injector;

    SpongiePluginContainer(Injector injector, Class<?> pluginClass, PluginMetadata metadata, Optional<Path> source) {
        super(metadata, source);

        this.injector = injector.createChildInjector(new PluginModule(this, pluginClass));
        this.instance = Optional.of(this.injector.getInstance(pluginClass));
    }

    @Override
    public Optional<?> getInstance() {
        return this.instance;
    }
}
