package org.spongepowered.spongie.impl;

import org.spongepowered.spongie.api.Spongie;

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
    }
}
