package org.spongepowered.spongie.impl.plugin.loader;

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.spongepowered.spongie.impl.SpongieImpl;
import org.spongepowered.spongie.impl.plugin.loader.asm.PluginClassVisitor;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadataSerializer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.annotation.Nullable;

public final class PluginScanner {

    private static final String ID_WARNING = "Plugin IDs should be lowercase, and only contain characters from "
            + "a-z, dashes or underscores, start with a lowercase letter, and not exceed 64 characters.";

    private static final String CLASS_EXTENSION = ".class";
    static final String JAR_EXTENSION = ".jar";

    private static final PathMatcher CLASS_FILE = path -> path.toString().endsWith(CLASS_EXTENSION);
    private static final PathMatcher JAR_FILE = path -> path.toString().endsWith(JAR_EXTENSION);
    private static final DirectoryStream.Filter<Path> JAR_FILTER = path -> path.toString().endsWith(JAR_EXTENSION);

    private static final String METADATA_FILE = "plugin.info";
    private static final String JAVA_HOME = System.getProperty("java.home");
    private static final Logger logger = SpongieImpl.getLogger();

    private final Map<String, PluginCandidate> plugins = new HashMap<>();
    private final Set<String> pluginClasses = new HashSet<>();

    @Nullable private FileVisitor<Path> classFileVisitor;

    public void scanClasspath(URLClassLoader loader, boolean scanJars) {
        final Set<URI> sources = new HashSet<>();

        for (URL url : loader.getURLs()) {
            if (!url.getProtocol().equals("file")) {
                logger.warn("Skipping unsupported classpath source: {}", url);
                continue;
            }

            if (url.getPath().startsWith(JAVA_HOME)) {
                logger.trace("Skipping JRE classpath entry: {}", url);
                continue;
            }

            if (!scanJars && url.getFile().endsWith(JAR_EXTENSION)) {
                logger.trace("Skipping classpath JAR file: {}", url);
                continue;
            }

            URI source;
            try {
                source = url.toURI();
            } catch (URISyntaxException e) {
                logger.error("Failed to search for classpath plugins in {}", url);
                continue;
            }

            if (sources.add(source)) {
                Path path = Paths.get(source);
                if (Files.isDirectory(path)) {
                    scanClasspathDirectory(path);
                } else if (scanJars && JAR_FILE.matches(path) && Files.exists(path)) {
                    scanJar(path, true);
                }
            }
        }
    }

    public void scanDirectory(Path path) {
        try (DirectoryStream<Path> dir = Files.newDirectoryStream(path, JAR_FILTER)) {
            for (Path jar : dir) {
                scanJar(jar, false);
            }
        } catch (IOException e) {
            logger.error("Failed to search for plugins in {}", path, e);
        }
    }

    private void scanClasspathDirectory(Path path) {
        logger.trace("Scanning {} for plugins", path);

        if (this.classFileVisitor == null) {
            this.classFileVisitor = new ClassFileVisitor();
        }

        try {
            Files.walkFileTree(path, Collections.singleton(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, this.classFileVisitor);
        } catch (IOException e) {
            logger.error("Failed to search for plugins in {}", path, e);
        }
    }

    void scanJar(Path path, boolean classpath) {
        logger.trace("Scanning {} for plugins", path);

        List<PluginMetadata> metadata = null;
        List<PluginCandidate> candidates = new ArrayList<>();

        final PluginSource source = new PluginSource(path);

        // Open the zip file so we can scan it for plugins
        try {
            try (JarInputStream jar = new JarInputStream(new BufferedInputStream(Files.newInputStream(path)))) {

                ZipEntry entry = jar.getNextEntry();

                if (entry == null) {
                    return;
                }

                do {
                    if (entry.isDirectory()) {
                        continue;
                    }

                    final String name = entry.getName();

                    if (!name.endsWith(CLASS_EXTENSION)) {
                        if (name.equals(METADATA_FILE)) {
                            try {
                                metadata = PluginMetadataSerializer.DEFAULT.read(jar);
                            } catch (IOException e) {
                                logger.error("Failed to read plugin metadata from " + METADATA_FILE + " in {}", path, e);
                                return;
                            }
                        }

                        continue;
                    }

                    final PluginCandidate candidate = scanClassFile(jar, source);
                    if (candidate != null) {
                        candidates.add(candidate);
                    }
                } while ((entry = jar.getNextEntry()) != null);
            }
        } catch (IOException e) {
            logger.error("Failed to scan plugin JAR: {}", path, e);
            return;
        }

        if (candidates.isEmpty()) {
            return;
        }

        boolean success = false;

        for (PluginCandidate candidate : candidates) {
            success |= addCandidate(candidate);

            // Find matching plugin metadata
            if (metadata != null) {
                boolean found = false;

                for (PluginMetadata meta : metadata) {
                    if (candidate.getPluginMetadata().getId().equals(meta.getId())) {
                        found = true;
                        candidate.setPluginMetadata(meta);
                        break;
                    }
                }

                if (!found) {
                    logger.warn("No matching metadata found for plugin '{}' in " + METADATA_FILE + " from {}", candidate.getPluginMetadata().getId(), path);
                }
            }
        }

        if (success) {
            if (metadata == null) {
                logger.warn("{} is missing a valid " + METADATA_FILE + " file."
                        + "This is not a problem when testing plugins, however it is recommended to include one in public plugins.\n"
                        + "Please see https://some.link.here for details.", path);
            }
        } else if (!classpath) {
            logger.warn("No valid plugins found in {}.", path);
        }
    }

    private PluginCandidate scanClassFile(InputStream in, PluginSource source) throws IOException {
        final ClassReader reader = new ClassReader(in);
        final PluginClassVisitor visitor = new PluginClassVisitor();

        try {
            reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            final PluginMetadata metadata = visitor.getMetadata();
            if (metadata == null) {
                return null; // Not a plugin class
            }

            return new PluginCandidate(visitor.getClassName().replace('/', '.'), source, metadata);
        } catch (InvalidPluginException e) {
            logger.error("Skipping invalid plugin {} from {}", visitor.getClassName(), source, e);
        }

        return null;
    }

    private boolean addCandidate(PluginCandidate candidate) {
        final String pluginClass = candidate.getPluginClass();
        final String id = candidate.getPluginMetadata().getId();

        if (!PluginMetadata.ID_PATTERN.matcher(id).matches()) {
            logger.error("Skipping plugin with invalid plugin ID '{}' from {}. " + ID_WARNING, id, candidate.getPluginSource());
            return false;
        }

        if (this.plugins.containsKey(id)) {
            logger.error("Skipping plugin with duplicate plugin ID '{}' from {}", id, candidate.getPluginSource());
            return false;
        }

        if (!this.pluginClasses.add(pluginClass)) {
            logger.error("Skipping duplicate plugin class {} from {}", pluginClass, candidate.getPluginSource());
            return false;
        }

        this.plugins.put(id, candidate);
        return true;
    }

    private final class ClassFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
            if (CLASS_FILE.matches(path)) {
                try (InputStream in = Files.newInputStream(path)) {
                    final PluginCandidate candidate = scanClassFile(in, PluginSource.CLASSPATH);
                    if (candidate != null) {
                        addCandidate(candidate);
                    }
                } catch (IOException e) {
                    logger.error("Failed to search for plugins in {}", path, e);
                }
            }
            return FileVisitResult.CONTINUE;
        }

    }
}