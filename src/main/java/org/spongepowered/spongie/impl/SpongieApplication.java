package org.spongepowered.spongie.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.spongie.api.Application;
import org.spongepowered.spongie.impl.event.SpongieEventManager;
import org.spongepowered.spongie.impl.plugin.SpongiePluginManager;
import org.spongepowered.spongie.impl.service.SpongieServiceManager;

public final class SpongieApplication implements Application {

    // TODO Configurable name
    private final Logger logger = LogManager.getLogger("Spongie");

    @Override
    public SpongieEventManager getEventManager() {
        return null;
    }

    @Override
    public SpongiePluginManager getPluginManager() {
        return null;
    }

    @Override
    public SpongieServiceManager getServiceManager() {
        return null;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
