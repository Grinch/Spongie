/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.plugin;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.spongepowered.spongie.api.plugin.meta.Dependency;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({TYPE})
@Retention(RUNTIME)
public @interface Plugin {

    /**
     * An ID for the plugin to be used internally. The ID should be unique as to
     * not conflict with other plugins.
     **
     * @return The plugin identifier
     * @see <a href="https://goo.gl/MRRYSJ">Java package naming conventions</a>
     */
    String id() default "";

    /**
     * The human readable name of the plugin as to be used in descriptions and
     * similar things.
     *
     * @return The plugin name, or an empty string if unknown
     */
    String name() default "";

    /**
     * The version of the plugin.
     *
     * @return The plugin version, or an empty string if unknown
     */
    String version() default "";

    /**
     * The description of the plugin, explaining what it can be used for.
     *
     * @return The plugin description, or an empty string if unknown
     */
    String description() default "";

    /**
     * The authors of the plugin.
     *
     * @return The plugin authors, or empty if unknown
     */
    String[] authors() default {};

    /**
     * The plugin dependencies.
     *
     * @return The plugin dependencies
     */
    Dependency[] dependencies() default {};
}
