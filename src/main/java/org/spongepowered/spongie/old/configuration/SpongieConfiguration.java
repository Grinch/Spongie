/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.old.configuration;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.spongie.old.configuration.category.ConnectionCategory;
import org.spongepowered.spongie.old.configuration.category.GlobalCategory;

@ConfigSerializable
public class SpongieConfiguration extends AbstractConfiguration {

    @Setting(value = "global")
    public final GlobalCategory globalCategory = new GlobalCategory();

    @Setting(value = "connection")
    public final ConnectionCategory connectionCategory = new ConnectionCategory();
}
