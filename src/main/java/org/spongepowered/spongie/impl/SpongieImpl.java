package org.spongepowered.spongie.impl;

import org.spongepowered.spongie.api.Spongie;
import org.spongepowered.spongie.impl.plugin.loader.PluginScanner;

import java.net.URLClassLoader;

public final class SpongieImpl {

    public static SpongieApplication getApplication() {
        return (SpongieApplication) Spongie.getApplication();
    }

    public static LaunchClassLoader getClassLoader() {
        return (LaunchClassLoader) Thread.currentThread().getContextClassLoader();
    }

    public static void launch() {
        final LaunchClassLoader classLoader = new LaunchClassLoader(((URLClassLoader) SpongieImpl.class.getClassLoader()).getURLs());
        Thread.currentThread().setContextClassLoader(classLoader);

        final PluginScanner scanner = new PluginScanner();
        scanner.scanClasspath(getClassLoader(), true);
        System.err.println("Test!");
    }
}
