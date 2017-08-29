/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.logging.log4j.Logger;
import org.spongepowered.spongie.api.Spongie;
import org.spongepowered.spongie.api.plugin.PluginContainer;
import org.spongepowered.spongie.api.plugin.PluginManager;
import org.spongepowered.spongie.impl.SpongieImpl;
import org.spongepowered.spongie.impl.plugin.loader.PluginCandidate;
import org.spongepowered.spongie.impl.plugin.loader.PluginScanner;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Singleton;

@Singleton
public final class SpongiePluginManager implements PluginManager {

    private static final Logger logger = SpongieImpl.getLogger();

    private final Injector rootInjector;

    private final Map<String, PluginContainer> plugins = new LinkedHashMap<>();
    private final Map<Object, PluginContainer> pluginInstances = new IdentityHashMap<>();

    @Inject
    public SpongiePluginManager(Injector rootInjector) {
        this.rootInjector = rootInjector;
    }

    private void registerPlugin(PluginContainer plugin) {
        this.plugins.put(plugin.getId(), plugin);
        plugin.getInstance().ifPresent(instance -> this.pluginInstances.put(instance, plugin));
    }

    public void loadPlugins(ClassLoader classLoader, boolean scanClasspath) throws IOException {
        final MetadataContainer metadataContainer = MetadataContainer.load();

        this.registerPlugin(metadataContainer.createContainer("spongie", "Spongie", Optional.of(Paths.get("."))));

        final PluginScanner scanner = new PluginScanner();

        if (scanClasspath) {
            logger.info("Scanning classpath for plugins...");
            if (!(classLoader instanceof URLClassLoader)) {
                logger.error("Invalid classloader detected, classpath scanning will not continue");
            } else {
                scanner.scanClasspath((URLClassLoader) classLoader, true);
            }
        }

        // TODO Load plugins from disk

        final Map<String, PluginCandidate> candidateMap = scanner.getPlugins();

        logger.info("{} plugin(s) found.", candidateMap.size());

        if (candidateMap.isEmpty()) {
            return; // Nothing to do
        }

        try {
            PluginSorter.sort(checkRequirements(candidateMap)).forEach(this::loadPlugin);
        } catch (Throwable e) {
            // TODO Formatted output for the requirements failure
        }
    }

    private Set<PluginCandidate> checkRequirements(Map<String, PluginCandidate> candidates) {
        // Collect all versions of already loaded plugins
        final Map<String, String> loadedPlugins = new HashMap<>();
        for (PluginContainer container : this.plugins.values()) {
            loadedPlugins.put(container.getId(), container.getVersion().orElse(null));
        }


        final Set<PluginCandidate> successfulCandidates = new HashSet<>(candidates.size());
        final List<PluginCandidate> failedCandidates = new ArrayList<>();

        for (PluginCandidate candidate : candidates.values()) {
            if (candidate.collectDependencies(loadedPlugins, candidates)) {
                successfulCandidates.add(candidate);
            } else {
                failedCandidates.add(candidate);
            }
        }

        if (failedCandidates.isEmpty()) {
            return successfulCandidates; // Nothing to do, all requirements satisfied
        }

        PluginCandidate candidate;
        boolean updated;
        while (true) {
            updated = false;
            Iterator<PluginCandidate> itr = successfulCandidates.iterator();
            while (itr.hasNext()) {
                candidate = itr.next();
                if (candidate.updateRequirements()) {
                    updated = true;
                    itr.remove();
                    failedCandidates.add(candidate);
                }
            }

            if (updated) {
                // Update failed candidates as well
                failedCandidates.forEach(PluginCandidate::updateRequirements);
            } else {
                break;
            }
        }

        for (PluginCandidate failed : failedCandidates) {
            if (failed.isInvalid()) {
                logger.error("Plugin '{}' from {} cannot be loaded because it is invalid", failed.getPluginMetadata().getId(), failed.getPluginSource());
            } else {
                // TODO Better output for the missing requirements
                logger.error("Cannot load plugin '{}' from {} because it is missing the required dependencies {}",
                        failed.getPluginMetadata().getId(), failed.getPluginSource(), failed.getMissingRequirements());
            }
        }

        return successfulCandidates;
    }

    private void loadPlugin(PluginCandidate candidate) {
        final String id = candidate.getPluginMetadata().getId();
        candidate.getPluginSource().addToClasspath();

        final PluginMetadata metadata = candidate.getPluginMetadata();
        final String name = firstNonNull(metadata.getName(), id);
        final String version = firstNonNull(metadata.getVersion(), "unknown");

        try {
            Class<?> pluginClass = Class.forName(candidate.getPluginClass());
            PluginContainer container = new SpongiePluginContainer(this.rootInjector, pluginClass, metadata, candidate.getPluginSource().getPath());

            registerPlugin(container);
            Spongie.getEventManager().registerListeners(container, container.getInstance().get());

            logger.info("Loaded plugin: {} {} (from {})", name, version, candidate.getPluginSource());
        } catch (Throwable e) {
            logger.error("Failed to load plugin: {} {} (from {})", name, version, candidate.getPluginSource(), e);
        }
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        checkNotNull(instance, "instance");

        if (instance instanceof PluginContainer) {
            return Optional.of((PluginContainer) instance);
        }

        return Optional.ofNullable(this.pluginInstances.get(instance));
    }

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        return Optional.ofNullable(this.plugins.get(id));
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return Collections.unmodifiableCollection(this.plugins.values());
    }

    @Override
    public boolean isLoaded(String id) {
        return this.plugins.containsKey(id);
    }
}
