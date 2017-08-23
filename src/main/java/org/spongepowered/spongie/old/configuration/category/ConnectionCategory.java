/*
 * This file is part of Grand Exchange, All Rights Reserved.
 *
 * Copyright (c) AlmuraDev <http://github.com/AlmuraDev/>
 */
package org.spongepowered.spongie.old.configuration.category;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ConnectionCategory {

    // TODO: Comments
    @Setting(value = "username", comment = "")
    public String username = "";

    @Setting(value = "nickname", comment = "")
    public String nickname = "";

    @Setting(value = "password", comment = "")
    public String password = "";

    @Setting(value = "server", comment = "")
    public String server = "";

    @Setting(value = "port", comment = "")
    public int port = 6667;

    @Setting(value = "accept-invalid-ssl", comment = "")
    public boolean acceptInvalidSsl = false;
}
