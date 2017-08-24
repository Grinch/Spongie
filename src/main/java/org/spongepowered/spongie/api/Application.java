package org.spongepowered.spongie.api;

import org.spongepowered.spongie.api.event.EventManager;
import org.spongepowered.spongie.api.plugin.PluginManager;
import org.spongepowered.spongie.api.service.ServiceManager;

public interface Application {

    default EventManager getEventManager() {
        return Spongie.getEventManager();
    }

    default PluginManager getPluginManager() {
        return Spongie.getPluginManager();
    }

    default ServiceManager getServiceManager() {
        return Spongie.getServiceManager();
    }
}
