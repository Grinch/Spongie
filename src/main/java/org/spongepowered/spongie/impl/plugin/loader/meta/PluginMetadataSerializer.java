package org.spongepowered.spongie.impl.plugin.loader.meta;

import static java.util.Arrays.asList;

import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class PluginMetadataSerializer {
    public static final PluginMetadataSerializer DEFAULT = new PluginMetadataSerializer(PluginMetadataCollectionAdapter.DEFAULT);
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String INDENT = "    ";

    private final PluginMetadataCollectionAdapter adapter;

    private PluginMetadataSerializer(PluginMetadataCollectionAdapter adapter) {
        this.adapter = adapter;
    }


    /**
     * Deserializes the specified JSON string into a {@link List} of
     * {@link PluginMetadata}.
     *
     * @param json The JSON string
     * @return The deserialized metadata list
     */
    public List<PluginMetadata> fromJson(String json) {
        try {
            return this.adapter.fromJson(json);
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
    }

    /**
     * Reads a {@link List} of {@link PluginMetadata} from the file represented
     * by the specified {@link Path}.
     *
     * @param path The path to the file
     * @return The deserialized metadata list
     * @throws IOException If an error occurs while reading
     */
    public List<PluginMetadata> read(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
            return read(reader);
        }
    }

    /**
     * Reads a {@link List} of {@link PluginMetadata} from the given
     * {@link InputStream}.
     *
     * @param in The input stream
     * @return The deserialized metadata list
     * @throws IOException If an error occurs while reading
     */
    public List<PluginMetadata> read(InputStream in) throws IOException {
        return this.adapter.fromJson(new BufferedReader(new InputStreamReader(in, CHARSET)));
    }

    /**
     * Reads a {@link List} of {@link PluginMetadata} from the given
     * {@link Reader}.
     *
     * @param reader The reader
     * @return The deserialized metadata list
     * @throws IOException If an error occurs while reading
     */
    public List<PluginMetadata> read(Reader reader) throws IOException {
        return this.adapter.fromJson(reader);
    }

    /**
     * Reads a {@link List} of {@link PluginMetadata} from the given
     * {@link JsonReader}.
     *
     * @param reader The JSON reader
     * @return The deserialized metadata list
     * @throws IOException If an error occurs while reading
     */
    public List<PluginMetadata> read(JsonReader reader) throws IOException {
        return this.adapter.read(reader);
    }

    /**
     * Serializes the specified {@link PluginMetadata} to a JSON string.
     *
     * @param meta The plugin metadata to serialize
     * @return The serialized JSON string
     */
    public String toJson(PluginMetadata... meta) {
        return toJson(asList(meta));
    }

    /**
     * Serializes the specified {@link List} of {@link PluginMetadata} to a
     * JSON string.
     *
     * @param meta The plugin metadata to serialize
     * @return The serialized JSON string
     */
    public String toJson(List<PluginMetadata> meta) {
        StringWriter writer = new StringWriter();
        try {
            write(writer, meta);
        } catch (IOException e) {
            throw new JsonIOException(e);
        }
        return writer.toString();
    }

    /**
     * Writes the specified {@link PluginMetadata} to the file represented by
     * the {@link Path}.
     *
     * @param path The path to the file to write to
     * @param meta The plugin metadata to serialize
     * @throws IOException If an error occurs while writing
     */
    public void write(Path path, PluginMetadata... meta) throws IOException {
        write(path, asList(meta));
    }

    /**
     * Writes the specified {@link List} of {@link PluginMetadata} to the file
     * represented by the {@link Path}.
     *
     * @param path The path to the file to write to
     * @param meta The plugin metadata to serialize
     * @throws IOException If an error occurs while writing
     */
    public void write(Path path, List<PluginMetadata> meta) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET)) {
            write(writer, meta);
        }
    }

    /**
     * Writes the specified {@link PluginMetadata} to the {@link Writer}.
     *
     * @param writer The writer
     * @param meta The plugin metadata to serialize
     * @throws IOException If an error occurs while writing
     */
    public void write(Writer writer, PluginMetadata... meta) throws IOException {
        write(writer, asList(meta));
    }

    /**
     * Writes the specified {@link List} of {@link PluginMetadata} to the
     * {@link Writer}.
     *
     * @param writer The writer
     * @param meta The plugin metadata to serialize
     * @throws IOException If an error occurs while writing
     */
    public void write(Writer writer, List<PluginMetadata> meta) throws IOException {
        try (JsonWriter json = new JsonWriter(writer)) {
            json.setIndent(INDENT);
            write(json, meta);
            writer.write('\n'); // Add new line at the end of the file
        }
    }

    /**
     * Writes the specified {@link PluginMetadata} to the {@link JsonWriter}.
     *
     * @param writer The JSON writer
     * @param meta The plugin metadata to serialize
     * @throws IOException If an error occurs while writing
     */
    public void write(JsonWriter writer, PluginMetadata... meta) throws IOException {
        write(writer, asList(meta));
    }

    /**
     * Writes the specified {@link List} of {@link PluginMetadata} to the
     * {@link JsonWriter}.
     *
     * @param writer The JSON writer
     * @param meta The plugin metadata to serialize
     * @throws IOException If an error occurs while writing
     */
    public void write(JsonWriter writer, List<PluginMetadata> meta) throws IOException {
        this.adapter.write(writer, meta);
    }
}
