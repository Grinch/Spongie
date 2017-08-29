/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.plugin;

import java.util.Collection;
import java.util.Optional;

/**
 * Manager object for {@link Plugin}s.
 */
public interface PluginManager {

    /**
     * Retrieves a {@link PluginContainer} based on its ID.
     *
     * @param id The plugin ID
     * @return The plugin, if available
     */
    Optional<PluginContainer> getPlugin(String id);

    /**
     * Gets the plugin container from an instance.
     *
     * @param instance The instance
     * @return The container
     */
    Optional<PluginContainer> fromInstance(Object instance);

    /**
     * Gets a {@link Collection} of all {@link PluginContainer}s.
     *
     * @return The plugins
     */
    Collection<PluginContainer> getPlugins();

    /**
     * Checks if a plugin is loaded based on its ID.
     *
     * @param id the id of the {@link Plugin}
     * @return {@code true} if loaded {@code false} if not loaded.
     */
    boolean isLoaded(String id);
}
