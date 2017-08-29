/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Optional<String> getVersion();

    /**
     * Gets the description of the {@link Plugin} within this container.
     *
     * @see Plugin#description()
     */
    Optional<String> getDescription();

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
    default Optional<?> getInstance() {
        return Optional.empty();
    }

    /**
     * Returns the assigned logger to this {@link Plugin}.
     *
     * @return The assigned logger
     */
    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getId());
    }
}
