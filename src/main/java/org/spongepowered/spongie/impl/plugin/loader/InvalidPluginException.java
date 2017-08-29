/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.loader;

public final class InvalidPluginException extends RuntimeException {

    private static final long serialVersionUID = -2566993626556875750L;

    public InvalidPluginException(String message) {
        super(message);
    }

}
