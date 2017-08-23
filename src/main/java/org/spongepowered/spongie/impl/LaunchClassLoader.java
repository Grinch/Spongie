package org.spongepowered.spongie.impl;

import java.net.URL;
import java.net.URLClassLoader;

public final class LaunchClassLoader extends URLClassLoader {

    public LaunchClassLoader(URL[] urls) {
        super(urls, null);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
