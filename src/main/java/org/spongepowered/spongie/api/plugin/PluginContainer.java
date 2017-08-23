package org.spongepowered.spongie.api.plugin;

import org.slf4j.Logger;
import org.spongepowered.spongie.api.plugin.meta.PluginDependency;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PluginContainer {

    /**
     * Gets the qualified ID of the {@link Plugin} within this container.
     *
     * @return The plugin ID
     * @see Plugin#id()
     */
    String getId();

    /**
     * Gets the name of the {@link Plugin} within this container.
     *
     * @see Plugin#name()
     */
    String getName();

    /**
     * Gets the description of the {@link Plugin} within this container.
     *
     * @see Plugin#description()
     */
    String getDescription();

    /**
     * Gets the authors of the {@link Plugin} within this container.
     *
     * @see Plugin#authors()
     */
    List<String> getAuthors();

    /**
     * Gets a {@link Set} of all dependencies of the {@link Plugin} within this
     * container.
     *
     * @return The plugin dependencies, can be empty
     * @see Plugin#dependencies()
     */
    Set<PluginDependency> getDependencies();

    /**
     * Gets the {@link PluginDependency} the plugin in this container has on
     * the plugin with the specified plugin ID.
     *
     * @param id The plugin ID of the dependency
     * @return The dependency, or {@link Optional#empty()} if unknown
     */
    Optional<PluginDependency> getDependency(String id);

    /**
     * Returns the source the plugin was loaded from.
     *
     * @return The source the plugin was loaded from or {@link Optional#empty()}
     *     if unknown
     */
    Optional<Path> getSource();

    /**
     * Returns the created instance of {@link Plugin} if it is available.
     *
     * @return The instance if available
     */
    Optional<?> getInstance();

    /**
     * Returns the assigned logger to this {@link Plugin}.
     *
     * @return The assigned logger
     */
    Logger getLogger();
}
