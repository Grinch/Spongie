/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.service;

import java.util.Optional;

/**
 * Manager object for services.
 */
public interface ServiceManager {

    /**
     * Registers a service.
     *
     * @param plugin The plugin registering a service
     * @param serviceClass The class of the service
     * @param service The service being registered
     * @param <T> The type of the service
     */
    <T> void registerService(Object plugin, Class<T> serviceClass, T service);

    /**
     * Gets a service by the service's class.
     *
     * @param serviceClass The class of the service
     * @param <T> The type of the service
     * @return An {@link Optional}
     */
    <T> Optional<T> getService(Class<T> serviceClass);
}
