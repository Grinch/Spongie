package org.spongepowered.spongie.impl.inject.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.slf4j.Logger;
import org.spongepowered.spongie.api.plugin.PluginContainer;

public class PluginModule extends AbstractModule {

    private final PluginContainer container;
    private final Class<?> pluginClass;

    public PluginModule(PluginContainer container, Class<?> pluginClass) {
        this.container = container;
        this.pluginClass = pluginClass;
    }

    @Override
    protected void configure() {
        this.bind(this.pluginClass).in(Scopes.SINGLETON);

        this.bind(PluginContainer.class).toInstance(this.container);
        this.bind(Logger.class).toInstance(this.container.getLogger());
    }
}
