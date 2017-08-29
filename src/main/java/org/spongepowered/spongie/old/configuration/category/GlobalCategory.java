/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.old.configuration.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GlobalCategory {

    // TODO: Comments
    @Setting(value = "name", comment = "The name used internally by the bot. Can be referenced in messages with '${bot.name}'")
    public String name = "Spongie";

    @Setting(value = "command-prefix", comment = "The global command prefix used to determine the attempt to run a bot command.")
    public String commandPrefix = ".";
}
