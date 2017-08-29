/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.loader.meta.version;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

public final class DefaultArtifactVersion implements ArtifactVersion {

    private final ComparableVersion version;

    public DefaultArtifactVersion(String version) {
        checkNotNull(version, "version");
        this.version = new ComparableVersion(version);
    }

    public ComparableVersion getVersion() {
        return version;
    }

    @Override
    public int compareTo(ArtifactVersion version) {
        if (version instanceof DefaultArtifactVersion) {
            return this.version.compareTo(((DefaultArtifactVersion) version).version);
        } else {
            return compareTo(new DefaultArtifactVersion(version.toString()));
        }
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DefaultArtifactVersion that = (DefaultArtifactVersion) o;
        return this.version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return this.version.hashCode();
    }

    @Override
    public String toString() {
        return this.version.toString();
    }

}
