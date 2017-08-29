/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.loader;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.MoreObjects;
import org.apache.logging.log4j.Logger;
import org.spongepowered.spongie.api.plugin.meta.PluginDependency;
import org.spongepowered.spongie.impl.SpongieImpl;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;
import org.spongepowered.spongie.impl.plugin.loader.meta.version.DefaultArtifactVersion;
import org.spongepowered.spongie.impl.plugin.loader.meta.version.InvalidVersionSpecificationException;
import org.spongepowered.spongie.impl.plugin.loader.meta.version.VersionRange;
import org.spongepowered.spongie.impl.plugin.meta.SpongiePluginDependency;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public final class PluginCandidate {

    private static final Logger logger = SpongieImpl.getLogger();
    private final String pluginClass;
    private final PluginSource pluginSource;
    private PluginMetadata pluginMetadata;

    private boolean invalid;

    @Nullable private Set<PluginCandidate> dependencies;
    @Nullable private Set<PluginCandidate> requirements;
    private final Set<String> dependenciesWithUnknownVersion = new HashSet<>();
    @Nullable private Map<String, String> versions;
    @Nullable private Map<String, String> missingRequirements;

    public PluginCandidate(String pluginClass, PluginSource pluginSource, PluginMetadata pluginMetadata) {
        this.pluginClass = checkNotNull(pluginClass);
        this.pluginSource = checkNotNull(pluginSource);
        this.pluginMetadata = checkNotNull(pluginMetadata);
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public PluginSource getPluginSource() {
        return pluginSource;
    }

    public PluginMetadata getPluginMetadata() {
        return pluginMetadata;
    }

    void setPluginMetadata(PluginMetadata pluginMetadata) {
        this.pluginMetadata = pluginMetadata;
    }

    public boolean isInvalid() {
        return this.invalid;
    }

    public boolean isLoadable() {
        return !this.invalid && getMissingRequirements().isEmpty();
    }

    public boolean dependenciesCollected() {
        return this.dependencies != null;
    }

    private void ensureState() {
        checkState(dependenciesCollected(), "Dependencies not collected yet");
    }

    public Set<PluginCandidate> getDependencies() {
        ensureState();
        return this.dependencies;
    }

    public Set<PluginCandidate> getRequirements() {
        ensureState();
        return this.requirements;
    }

    public Map<String, String> getMissingRequirements() {
        ensureState();
        return this.missingRequirements;
    }

    public String getVersion(String id) {
        ensureState();
        return this.versions.get(id);
    }

    public boolean updateRequirements() {
        ensureState();
        if (this.requirements.isEmpty()) {
            return false;
        }

        Iterator<PluginCandidate> itr = this.requirements.iterator();
        while (itr.hasNext()) {
            final PluginCandidate candidate = itr.next();
            if (!candidate.isLoadable()) {
                itr.remove();
                this.missingRequirements.put(candidate.pluginMetadata.getId(), this.versions.get(candidate.getPluginMetadata().getId()));
            }
        }

        return this.invalid || !this.missingRequirements.isEmpty();
    }

    public boolean collectDependencies(Map<String, String> loadedPlugins, Map<String, PluginCandidate> candidates) {
        checkState(this.dependencies == null, "Dependencies already collected");

        if (loadedPlugins.containsKey(this.pluginMetadata.getId())) {
            this.invalid = true;
        }

        this.dependencies = new HashSet<>();
        this.requirements = new HashSet<>();
        this.versions = new HashMap<>();
        this.missingRequirements = new HashMap<>();

        for (PluginDependency dependency : this.pluginMetadata.collectRequiredDependencies()) {
            final String id = dependency.getId();
            if (this.pluginMetadata.getId().equals(id)) {
                logger.warn("Plugin '{}' from {} requires itself to be loaded. "
                        + "This is redundant and can be removed from the dependencies.", this.pluginMetadata.getId(), this.pluginSource);
                continue;
            }

            final String version = dependency.getVersion().orElse(null);

            if (loadedPlugins.containsKey(id)) {
                if (!verifyVersionRange(id, version, loadedPlugins.get(id))) {
                    this.missingRequirements.put(id, version);
                }

                continue;
            }

            final PluginCandidate candidate = candidates.get(id);
            if (candidate != null && verifyVersionRange(id, version, candidate.getPluginMetadata().getVersion())) {
                this.requirements.add(candidate);
                continue;
            }

            this.missingRequirements.put(id, version);
        }

        Map<PluginDependency.LoadOrder, Set<PluginDependency>> dependencies = this.pluginMetadata.groupDependenciesByLoadOrder();

        collectOptionalDependencies(dependencies.get(PluginDependency.LoadOrder.BEFORE), loadedPlugins, candidates);

        // TODO: Dependencies to load after this plugin
        //collectOptionalDependencies(dependencies.get(PluginDependency.LoadOrder.AFTER), loadedPlugins, candidates);

        Set<PluginDependency> loadAfter = dependencies.get(PluginDependency.LoadOrder.AFTER);
        if (loadAfter != null && !loadAfter.isEmpty()) {
            this.invalid = true;
            logger.error("Invalid dependency with load order AFTER on plugin '{}' from {}. "
                            + "This is currently not supported for Spongie plugins! Requested dependencies: {}",
                    this.pluginMetadata.getId(), this.pluginMetadata, loadAfter);
        }

        return isLoadable();
    }

    private void collectOptionalDependencies(@Nullable Iterable<PluginDependency> dependencies,
            Map<String, String> loadedPlugins, Map<String, PluginCandidate> candidates) {
        if (dependencies == null) {
            return;
        }

        for (PluginDependency dependency : dependencies) {
            final String id = dependency.getId();
            if (this.pluginMetadata.getId().equals(id)) {
                logger.error("Plugin '{}' from {} cannot have a dependency on itself. This is redundant and should be "
                        + "removed.", this.pluginMetadata.getId(), this.pluginSource);
                this.invalid = true;
                continue;
            }

            final String version = dependency.getVersion().orElse(null);

            if (loadedPlugins.containsKey(id)) {
                if (!verifyVersionRange(id, version, loadedPlugins.get(id))) {
                    this.missingRequirements.put(id, version);
                }

                continue;
            }

            PluginCandidate candidate = candidates.get(id);
            if (candidate != null) {
                if (verifyVersionRange(id, version, candidate.getPluginMetadata().getVersion())) {
                    this.dependencies.add(candidate);
                } else {
                    this.missingRequirements.put(id, version);
                }
            }
        }
    }

    private boolean verifyVersionRange(String id, @Nullable String expectedRange, @Nullable String version) {
        if (expectedRange == null) {
            return true;
        }

        // Don't check version again if it already failed
        if (expectedRange.equals(this.missingRequirements.get(id))) {
            return false;
        }

        // Don't check version again if it was already checked
        if (expectedRange.equals(this.versions.get(id))) {
            return true;
        }

        if (version != null) {
            try {
                VersionRange range = VersionRange.createFromVersionSpec(expectedRange);
                DefaultArtifactVersion installedVersion = new DefaultArtifactVersion(version);
                if (range.containsVersion(installedVersion)) {
                    String currentRange = this.versions.get(id);
                    if (currentRange != null) {
                        // This should almost never happen because it means the plugin is
                        // depending on two different versions of another plugin

                        // We need to merge the ranges
                        VersionRange otherRange;
                        try {
                            otherRange = VersionRange.createFromVersionSpec(currentRange);
                        } catch (InvalidVersionSpecificationException e) {
                            throw new AssertionError(e); // Should never happen because we already parsed it once
                        }

                        expectedRange = otherRange.restrict(range).toString();
                    }

                    this.versions.put(id, expectedRange);

                    if (range.getRecommendedVersion() instanceof DefaultArtifactVersion) {
                        BigInteger majorExpected = ((DefaultArtifactVersion) range.getRecommendedVersion()).getVersion().getFirstInteger();
                        if (majorExpected != null) {
                            BigInteger majorInstalled = installedVersion.getVersion().getFirstInteger();

                            // Show a warning if the major version does not match,
                            // or if the installed version is lower than the recommended version
                            if (majorInstalled != null
                                    && (!majorExpected.equals(majorInstalled) || installedVersion.compareTo(range.getRecommendedVersion()) < 0)) {
                                logger.warn("Plugin {} from {} was designed for {} {}. It may not work properly.",
                                        this.pluginMetadata.getId(), this.pluginSource, id, range.getRecommendedVersion());
                            }
                        }

                    }

                    return true;
                }
            } catch (InvalidVersionSpecificationException e) {
                logger.error("Failed to parse version range {} for dependency {} of plugin {} from {}: {}",
                        version, id, this.pluginMetadata.getId(), this.pluginSource, e.getMessage());
                this.invalid = true;
            }
        } else {
            if (this.dependenciesWithUnknownVersion.add(id)) {
                logger.warn("Cannot check version of dependency {} for plugin {} from {}: Version of dependency unknown",
                        id, this.pluginMetadata.getId(), this.pluginSource);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PluginCandidate candidate = (PluginCandidate) o;
        return this.pluginMetadata.getId().equals(candidate.pluginMetadata.getId());

    }

    @Override
    public int hashCode() {
        return this.pluginMetadata.getId().hashCode();
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", this.pluginMetadata.getId())
                .add("class", this.pluginClass)
                .add("source", this.pluginSource);
        if (this.invalid) {
            helper.addValue("INVALID");
        } else if (this.missingRequirements != null && !this.missingRequirements.isEmpty()) {
            helper.addValue("FAILED");
        }

        return helper.toString();
    }
}
