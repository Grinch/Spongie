package org.spongepowered.spongie.impl.plugin.loader.meta;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.spongepowered.spongie.api.plugin.meta.PluginDependency;
import org.spongepowered.spongie.impl.plugin.meta.SpongiePluginDependency;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

public final class PluginMetadata {

    /**
     * The pattern plugin IDs must match. Plugin IDs must be lower case, and
     * start with an alphabetic character. It may only contain alphanumeric
     * characters, dashes or underscores. It cannot be longer than
     * 64 characters.
     */
    public static final Pattern ID_PATTERN = Pattern.compile("[a-z][a-z0-9-_]{0,63}");

    private final String id, name, version, description;
    private final List<String> authors;
    private final Map<String, SpongiePluginDependency> dependencies;

    public PluginMetadata(String id, String name, String version, String description, List<String> authors, Map<String, SpongiePluginDependency>
            dependencies) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.description = description;
        this.authors = Collections.unmodifiableList(authors);
        this.dependencies = Collections.unmodifiableMap(dependencies);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public Map<String, SpongiePluginDependency> getDependencies() {
        return this.dependencies;
    }

    public Set<PluginDependency> collectRequiredDependencies() {
        if (this.dependencies.isEmpty()) {
            return ImmutableSet.of();
        }

        final ImmutableSet.Builder<PluginDependency> builder = ImmutableSet.builder();

        for (PluginDependency dependency : this.dependencies.values()) {
            if (!dependency.isOptional()) {
                builder.add(dependency);
            }
        }

        return builder.build();
    }

    public Map<PluginDependency.LoadOrder, Set<PluginDependency>> groupDependenciesByLoadOrder() {
        if (this.dependencies.isEmpty()) {
            return ImmutableMap.of();
        }

        final EnumMap<PluginDependency.LoadOrder, Set<PluginDependency>> map = new EnumMap<>(PluginDependency.LoadOrder.class);

        for (PluginDependency.LoadOrder order : PluginDependency.LoadOrder.values()) {
            final ImmutableSet.Builder<PluginDependency> dependencies = ImmutableSet.builder();

            for (PluginDependency dependency : this.dependencies.values()) {
                if (dependency.getLoadOrder() == order) {
                    dependencies.add(dependency);
                }
            }

            map.put(order, dependencies.build());
        }

        return Maps.immutableEnumMap(map);
    }

    public static class Builder {
        private String id, name, version, description;
        private List<String> authors = new LinkedList<>();
        private Map<String, SpongiePluginDependency> dependencies = new HashMap<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addAuthor(String author) {
            this.authors.add(author);
            return this;
        }

        public Builder addDependency(SpongiePluginDependency dependency) {
            String id = dependency.getId();
            checkArgument(!this.dependencies.containsKey(id), "Duplicate dependency with plugin ID: %s", id);
            this.dependencies.put(id, dependency);
            return this;
        }

        public PluginMetadata build() {
            return new PluginMetadata(id, name, version, description, authors, dependencies);
        }
    }
}
