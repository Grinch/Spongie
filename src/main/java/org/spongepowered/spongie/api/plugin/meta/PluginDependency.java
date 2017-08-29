/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.plugin.meta;

import java.util.Optional;

public interface PluginDependency {

    String getId();

    Optional<String> getVersion();

    boolean isOptional();

    LoadOrder getLoadOrder();

    /**
     * Defines when the dependency should be loaded in relation to the plugin.
     */
    enum LoadOrder {

        /**
         * The plugin and the dependency can be loaded in any order.
         */
        NONE,

        /**
         * The dependency should be loaded <b>before</b> the plugin.
         */
        BEFORE,

        /**
         * The dependency should be loaded <b>after</b> the plugin.
         */
        AFTER

    }
}
