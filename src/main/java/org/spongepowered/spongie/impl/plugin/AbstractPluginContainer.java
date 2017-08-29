/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin;

import com.google.common.base.MoreObjects;
import org.spongepowered.spongie.api.plugin.PluginContainer;

public abstract class AbstractPluginContainer implements PluginContainer {
    AbstractPluginContainer() {
    }

    private MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper("Plugin")
                .omitNullValues()
                .add("id", getId())
                .add("name", getName())
                .add("version", getVersion().orElse(null))
                .add("description", getDescription().orElse(null))
                .add("authors", getAuthors().isEmpty() ? null : getAuthors())
                .add("source", getSource().orElse(null));
    }

    @Override
    public final String toString() {
        return toStringHelper().toString();
    }
}
