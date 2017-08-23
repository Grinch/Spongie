package org.spongepowered.spongie.impl.plugin;

import org.slf4j.Logger;
import org.spongepowered.spongie.api.plugin.PluginContainer;
import org.spongepowered.spongie.api.plugin.meta.PluginDependency;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class SpongiePluginContainer implements PluginContainer {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getAuthors() {
        return null;
    }

    @Override
    public Set<PluginDependency> getDependencies() {
        return null;
    }

    @Override
    public Optional<PluginDependency> getDependency(String id) {
        return null;
    }

    @Override
    public Optional<Path> getSource() {
        return null;
    }

    @Override
    public Optional<?> getInstance() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }
}
