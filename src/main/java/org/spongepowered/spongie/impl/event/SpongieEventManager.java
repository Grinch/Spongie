package org.spongepowered.spongie.impl.event;

import org.spongepowered.spongie.api.event.Event;
import org.spongepowered.spongie.api.event.EventManager;

import javax.inject.Singleton;

@Singleton
public class SpongieEventManager implements EventManager {

    @Override
    public void registerListeners(Object plugin, Object listener) {

    }

    @Override
    public void unregisterListeners(Object obj) {

    }

    @Override
    public boolean post(Event event) {
        return false;
    }
}
