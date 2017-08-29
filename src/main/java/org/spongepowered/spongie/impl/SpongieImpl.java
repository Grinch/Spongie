/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.spongie.impl.event.SpongieEventManager;
import org.spongepowered.spongie.impl.inject.SpongieImplementationModule;
import org.spongepowered.spongie.impl.inject.SpongieModule;
import org.spongepowered.spongie.impl.plugin.SpongiePluginManager;
import org.spongepowered.spongie.impl.service.SpongieServiceManager;

import java.io.IOException;
import java.net.URLClassLoader;

public final class SpongieImpl {

    @Inject private static SpongieEventManager eventManager;
    @Inject private static SpongiePluginManager pluginManager;
    @Inject private static SpongieServiceManager serviceManager;

    private static Logger logger = LogManager.getLogger("Spongie");

    public static Logger getLogger() {
        return logger;
    }

    public static SpongieEventManager getEventManager() {
        return eventManager;
    }

    public static SpongiePluginManager getPluginManager() {
        return pluginManager;
    }

    public static SpongieServiceManager getServiceManager() {
        return serviceManager;
    }

    public static LaunchClassLoader getClassLoader() {
        return (LaunchClassLoader) Thread.currentThread().getContextClassLoader();
    }

    static void launch() throws IOException {
        logger.info("Spongie v0.1 Copyright (c) SpongePowered.");

        final LaunchClassLoader classLoader = new LaunchClassLoader(((URLClassLoader) SpongieImpl.class.getClassLoader()).getURLs());
        Thread.currentThread().setContextClassLoader(classLoader);

        Guice.createInjector(new SpongieModule(), new SpongieImplementationModule());

        // TODO Configurable classpath scanning
        pluginManager.loadPlugins(getClassLoader(), true);
    }
}
