package org.spongepowered.spongie.impl;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.spongie.impl.inject.SpongieImplementationModule;
import org.spongepowered.spongie.impl.inject.SpongieModule;
import org.spongepowered.spongie.impl.plugin.loader.PluginScanner;

import java.net.URLClassLoader;

public final class SpongieImpl {

    @Inject private static SpongieApplication application;
    private static Logger logger = LogManager.getLogger("Spongie");

    public static SpongieApplication getApplication() {
        return application;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static LaunchClassLoader getClassLoader() {
        return (LaunchClassLoader) Thread.currentThread().getContextClassLoader();
    }

    public static void launch() {
        final LaunchClassLoader classLoader = new LaunchClassLoader(((URLClassLoader) SpongieImpl.class.getClassLoader()).getURLs());
        Thread.currentThread().setContextClassLoader(classLoader);

        Guice.createInjector(new SpongieModule(), new SpongieImplementationModule());

        final PluginScanner scanner = new PluginScanner();
        scanner.scanClasspath(getClassLoader(), true);

        logger.error(SpongieImpl.getApplication().getEventManager());
    }
}
