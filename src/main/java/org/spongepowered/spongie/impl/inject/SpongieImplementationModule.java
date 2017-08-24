package org.spongepowered.spongie.impl.inject;

import com.google.inject.PrivateModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.spongie.api.Application;
import org.spongepowered.spongie.api.Spongie;
import org.spongepowered.spongie.api.event.EventManager;
import org.spongepowered.spongie.api.plugin.PluginManager;
import org.spongepowered.spongie.api.service.ServiceManager;
import org.spongepowered.spongie.impl.SpongieApplication;
import org.spongepowered.spongie.impl.SpongieImpl;
import org.spongepowered.spongie.impl.event.SpongieEventManager;
import org.spongepowered.spongie.impl.plugin.SpongiePluginManager;
import org.spongepowered.spongie.impl.service.SpongieServiceManager;

public final class SpongieImplementationModule extends PrivateModule {

    @Override
    protected void configure() {
        this.bindAndExpose(Application.class).to(SpongieApplication.class);
        this.bindAndExpose(EventManager.class).to(SpongieEventManager.class);
        this.bindAndExpose(PluginManager.class).to(SpongiePluginManager.class);
        this.bindAndExpose(ServiceManager.class).to(SpongieServiceManager.class);

        this.bind(Logger.class).toInstance(SpongieImpl.getLogger());
        this.bind(org.slf4j.Logger.class).toInstance(LoggerFactory.getLogger(SpongieImpl.getLogger().getName()));

        this.requestStaticInjection(Spongie.class);
        this.requestStaticInjection(SpongieImpl.class);
    }

    private <T> AnnotatedBindingBuilder<T> bindAndExpose(final Class<T> type) {
        this.expose(type);
        return this.bind(type);
    }
}
