package org.spongepowered.spongie.impl.plugin;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.logging.log4j.Logger;
import org.spongepowered.spongie.api.plugin.PluginContainer;
import org.spongepowered.spongie.api.plugin.PluginManager;
import org.spongepowered.spongie.impl.SpongieImpl;
import org.spongepowered.spongie.impl.plugin.loader.PluginCandidate;
import org.spongepowered.spongie.impl.plugin.loader.PluginScanner;
import org.spongepowered.spongie.impl.util.graph.DirectedGraph;
import org.spongepowered.spongie.impl.util.graph.TopologicalOrder;

import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Singleton;

@Singleton
public final class SpongiePluginManager implements PluginManager {

    private static final Logger logger = SpongieImpl.getLogger();
    private final Map<String, PluginContainer> plugins = new LinkedHashMap<>();

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        checkNotNull(id);
        return Optional.ofNullable(this.plugins.get(id));
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        for (PluginContainer container : this.plugins.values()) {
            final Object obj = container.getInstance().orElse(null);

            if (instance == obj) {
                return Optional.of(container);
            }
        }

        return Optional.empty();
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return Collections.unmodifiableCollection(this.plugins.values());
    }

    @Override
    public boolean isLoaded(String id) {
        return this.getPlugin(id).isPresent();
    }

    public void loadPlugins(ClassLoader classLoader, boolean scanClassPath) {
        final PluginScanner scanner = new PluginScanner();

        if (scanClassPath) {
            if (!(classLoader instanceof URLClassLoader)) {
                logger.info("Cannot scan classpath for plugins, invalid classloader detected: {}", classLoader);
            } else {
                logger.info("Scanning classpath for plugins...");
                scanner.scanClasspath((URLClassLoader) classLoader, true);
            }
        }

        // TODO Scan plugins directory

        logger.info("{} plugin(s) found.", scanner.getPlugins().size());


    }


    private final List<PluginCandidate> sortPlugins(Iterable<PluginCandidate> candidates) {
        final DirectedGraph<PluginCandidate> graph = new DirectedGraph<>();

        for (PluginCandidate candidate : candidates) {
            graph.add(candidate);
            for (PluginCandidate dependency : candidate.getDependencies()) {
                graph.addEdge(candidate, dependency);
            }
        }

        return TopologicalOrder.createOrderedLoad(graph);
    }
}
