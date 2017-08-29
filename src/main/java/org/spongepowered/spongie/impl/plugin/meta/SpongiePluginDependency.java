/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.meta;

import org.spongepowered.spongie.api.plugin.meta.PluginDependency;

import java.util.Optional;

public class SpongiePluginDependency implements PluginDependency {

    private final String id, version;
    private boolean isOptional;
    private final LoadOrder loadOrder;

    public SpongiePluginDependency(String id, String version, boolean isOptional, LoadOrder loadOrder) {
        this.id = id;
        this.version = version;
        this.isOptional = isOptional;
        this.loadOrder = loadOrder;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.ofNullable(this.version);
    }

    @Override
    public boolean isOptional() {
        return this.isOptional;
    }

    @Override
    public LoadOrder getLoadOrder() {
        return this.loadOrder;
    }

    public SpongiePluginDependency required() {
        return new SpongiePluginDependency(this.id, this.version, false, this.loadOrder);
    }
}
