package org.spongepowered.spongie.api.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}
