/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.old;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.util.AcceptingTrustManagerFactory;
import org.spongepowered.spongie.old.configuration.MappedConfigurationAdapter;
import org.spongepowered.spongie.old.configuration.SpongieConfiguration;
import org.spongepowered.spongie.old.configuration.category.ConnectionCategory;
import org.spongepowered.spongie.old.configuration.category.GlobalCategory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spongie {

    private static Client client;
    private static MappedConfigurationAdapter<SpongieConfiguration> configurationAdapter;

    public static void main(String[] args) {
        construct();
    }

    private static void construct() {
        // Load configuration
        configurationAdapter = new MappedConfigurationAdapter<>(
                SpongieConfiguration.class,
                ConfigurationOptions.defaults(),
                Constants.Filesystem.PATH_ROOT,
                Constants.Filesystem.CONFIG_NAME);

        try {
            if (Files.notExists(configurationAdapter.getConfigFolder())) {
                Files.createDirectories(configurationAdapter.getConfigFolder());
            }
            if (Files.notExists(configurationAdapter.getConfigPath())) {
                configurationAdapter.save();
            }

            configurationAdapter.load();
        } catch (IOException | ObjectMappingException e) {
            throw new RuntimeException("Failed to save config for class [" + configurationAdapter.getConfigClass() + "] in [" +
                    configurationAdapter.getConfigPath() + "]!", e);
        }

        // Create the client
        GlobalCategory globalCategory = configurationAdapter.getConfig().globalCategory;
        ConnectionCategory connectionCategory = configurationAdapter.getConfig().connectionCategory;
        Client.Builder builder = Client.builder()
                .serverHost(connectionCategory.server)
                .serverPort(connectionCategory.port)
                .serverPassword(connectionCategory.password)
                .name(globalCategory.name)
                .user(connectionCategory.username)
                .nick(connectionCategory.nickname)
                .listenInput(s -> System.out.println("[Received] " + s))
                .listenOutput(s -> System.out.println("[Sent] " + s));

        if (connectionCategory.acceptInvalidSsl) {
            builder.secureTrustManagerFactory(new AcceptingTrustManagerFactory());
        }

        // Log details about connection, try to find a better way to print this....
        final StringBuilder configurationBuilder = new StringBuilder("[Local] Preparing to connect using the following settings..." + Constants
                .Text.LINE_SEPARATOR);
        final Map<Object, List<Field>> categoryFieldMap = new HashMap<>();
        categoryFieldMap.put(globalCategory, Arrays.asList(globalCategory.getClass().getDeclaredFields()));
        categoryFieldMap.put(connectionCategory, Arrays.asList(connectionCategory.getClass().getDeclaredFields()));
        final int maxFieldLength = categoryFieldMap.entrySet().stream()
                .flatMap(l -> l.getValue().stream().map(f -> f.getName().length()))
                .sorted((i1, i2) -> Integer.compare(i2, i1))
                .findFirst().orElse(0) + 2;

        categoryFieldMap.forEach((k, v) -> v.forEach((f -> {
            configurationBuilder.append(String.format("%" + maxFieldLength + "s", f.getName()));
            try {
                configurationBuilder.append(": ").append(f.get(k)).append(Constants.Text.LINE_SEPARATOR);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        })));

        System.out.println(configurationBuilder.toString());

        client = builder.build();
        client.getEventManager().registerEventListener(new Listener());
        client.addChannel("#inxe");
    }

    public static SpongieConfiguration getConfiguration() {
        return configurationAdapter.getConfig();
    }

    public static Client getClient() {
        return client;
    }
}
