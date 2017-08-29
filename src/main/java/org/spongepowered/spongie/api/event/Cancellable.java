/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.event;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancel);
}
