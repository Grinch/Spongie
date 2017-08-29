/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.loader;

import org.spongepowered.spongie.impl.SpongieImpl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Optional;

public final class PluginSource {

    static final PluginSource CLASSPATH = new PluginSource();

    public static Optional<Path> find(Class<?> type) {
        CodeSource source = type.getProtectionDomain().getCodeSource();
        if (source == null) {
            return Optional.empty();
        }

        URL location = source.getLocation();
        String path = location.getPath();

        if (!location.getProtocol().equals("file")) {
            return Optional.empty();
        }

        if (path.endsWith(PluginScanner.JAR_EXTENSION)) {
            try {
                return Optional.of(Paths.get(new URI(path)));
            } catch (URISyntaxException e) {
                throw new InvalidPathException(path, "Not a valid URI");
            }
        }

        return Optional.empty();
    }

    private final Optional<Path> path;
    private boolean onClasspath;

    private PluginSource() {
        this.path = Optional.empty();
        this.onClasspath = true;
    }

    PluginSource(Path path) {
        this.path = Optional.of(path);
        this.onClasspath = false;
    }

    public Optional<Path> getPath() {
        return this.path;
    }

    public void addToClasspath() {
        if (this.onClasspath) {
            return;
        }

        this.onClasspath = true;

        try {
            SpongieImpl.getClassLoader().addURL(this.path.get().toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to add plugin " + this + " to classpath", e);
        }
    }
}
