package org.spongepowered.spongie.impl.plugin.loader;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;

public class PluginCandidate {

    private final String pluginClass;
    private final PluginSource pluginSource;
    private PluginMetadata pluginMetadata;

    public PluginCandidate(String pluginClass, PluginSource pluginSource, PluginMetadata pluginMetadata) {
        this.pluginClass = checkNotNull(pluginClass);
        this.pluginSource = checkNotNull(pluginSource);
        this.pluginMetadata = checkNotNull(pluginMetadata);
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public PluginSource getPluginSource() {
        return pluginSource;
    }

    public PluginMetadata getPluginMetadata() {
        return pluginMetadata;
    }

    protected void setPluginMetadata(PluginMetadata pluginMetadata) {
        this.pluginMetadata = pluginMetadata;
    }
}
