package org.spongepowered.spongie.impl.service;

import org.spongepowered.spongie.api.service.ServiceManager;

import java.util.Optional;

import javax.inject.Singleton;

@Singleton
public class SpongieServiceManager implements ServiceManager {

    @Override
    public <T> void registerService(Object plugin, Class<T> serviceClass, T service) {

    }

    @Override
    public <T> Optional<T> getService(Class<T> serviceClass) {
        return null;
    }
}
