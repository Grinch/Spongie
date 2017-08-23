/*
 * This file is part of Grand Exchange, All Rights Reserved.
 *
 * Copyright (c) AlmuraDev <http://github.com/AlmuraDev/>
 */
package org.spongepowered.spongie.configuration;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.spongie.configuration.category.ConnectionCategory;
import org.spongepowered.spongie.configuration.category.GlobalCategory;

@ConfigSerializable
public class SpongieConfiguration extends AbstractConfiguration {

    @Setting(value = "global")
    public final GlobalCategory globalCategory = new GlobalCategory();

    @Setting(value = "connection")
    public final ConnectionCategory connectionCategory = new ConnectionCategory();
}
