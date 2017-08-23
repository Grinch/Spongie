package org.spongepowered.spongie.api;

import org.spongepowered.spongie.api.event.EventManager;
import org.spongepowered.spongie.api.plugin.PluginManager;
import org.spongepowered.spongie.api.service.ServiceManager;

public interface Application {

    EventManager getEventManager();

    PluginManager getPluginManager();

    ServiceManager getServiceManager();
}
