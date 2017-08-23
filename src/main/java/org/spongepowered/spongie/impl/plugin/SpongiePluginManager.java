package org.spongepowered.spongie.impl.plugin;

import org.spongepowered.spongie.api.plugin.PluginContainer;
import org.spongepowered.spongie.api.plugin.PluginManager;

import java.util.Collection;
import java.util.Optional;

public final class SpongiePluginManager implements PluginManager {

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        return null;
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        return null;
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return null;
    }

    @Override
    public boolean isLoaded(String id) {
        return false;
    }
}
