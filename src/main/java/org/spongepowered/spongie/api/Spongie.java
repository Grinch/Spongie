package org.spongepowered.spongie.api;

import static com.google.common.base.Preconditions.checkState;

import com.google.inject.Inject;
import org.spongepowered.spongie.api.event.EventManager;
import org.spongepowered.spongie.api.plugin.PluginManager;
import org.spongepowered.spongie.api.service.ServiceManager;

import javax.annotation.Nullable;

public final class Spongie {

    @Inject private static Application application;

    private static <T> T check(@Nullable T instance) {
        checkState(instance != null, "Spongie has not been initialized!");
        return instance;
    }

    public static Application getApplication() {
        return check(application);
    }

    public static EventManager getEventManager() {
        return getApplication().getEventManager();
    }

    public static PluginManager getPluginManager() {
        return getApplication().getPluginManager();
    }

    public static ServiceManager getServiceManager() {
        return getApplication().getServiceManager();
    }
}
