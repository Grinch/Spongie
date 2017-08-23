package org.spongepowered.spongie.impl.plugin.loader.meta;

import org.spongepowered.spongie.impl.plugin.meta.SpongiePluginDependency;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

    public static List<PluginMetadata> read(JarInputStream jar) throws IOException {

    }

    private final String id, name, version, description;
    private final List<String> authors;
    private final List<SpongiePluginDependency> dependencies;

    public PluginMetadata(String id, String name, String version, String description, List<String> authors, List<SpongiePluginDependency> dependencies) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.description = description;
        this.authors = Collections.unmodifiableList(authors);
        this.dependencies = Collections.unmodifiableList(dependencies);
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

    public List<SpongiePluginDependency> getDependencies() {
        return this.dependencies;
    }

    public static class Builder {
        private String id, name, version, description;
        private List<String> authors = new LinkedList<>();
        private List<SpongiePluginDependency> dependencies = new LinkedList<>();

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
            this.dependencies.add(dependency);
            return this;
        }

        public PluginMetadata build() {
            return new PluginMetadata(id, name, version, description, authors, dependencies);
        }
    }
}
