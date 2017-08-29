/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.event;

/**
 * Manager object for {@link Event}s and {@link Listener}s.
 */
public interface EventManager {

    /**
     * Registers {@link Event} methods annotated with @{@link Listener} in the
     * specified object.
     *
     * <p>Only methods that are public will be registered and the class must be
     * public as well.</p>
     *
     * @param plugin The plugin instance
     * @param listener The listener
     * @throws IllegalArgumentException Thrown if {@code plugin} is not a plugin
     *         instance
     */
    void registerListeners(Object plugin, Object listener);

    /**
     * Un-registers an object from receiving {@link Event}s.
     *
     * @param obj The object
     */
    void unregisterListeners(Object obj);

    /**
     * Calls an {@link Event} to all listeners that listen to it.
     *
     * @param event The event
     * @return True if cancelled, false if not
     */
    boolean post(Event event);
}
