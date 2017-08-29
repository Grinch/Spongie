/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.event.service;

import org.spongepowered.spongie.api.event.Cancellable;
import org.spongepowered.spongie.api.plugin.PluginContainer;

public interface RegisterServiceEvent extends Cancellable {

    PluginContainer getPlugin();

    <T> T getService();
}
