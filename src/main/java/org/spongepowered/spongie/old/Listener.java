/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.old;

import net.engio.mbassy.listener.Handler;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.channel.ChannelPartEvent;

public class Listener {

    @Handler
    public void onUserJoinChannel(ChannelJoinEvent event) {
        // TODO: Replace messages with configuration-based messages

        // Send a message when we join a channel
        if (event.getClient().isUser(event.getUser())) { // It's me!
            //event.getChannel().sendMessage("Hola!");
            return;
        }

        // Send a message when someone else joins a channel
        //event.getChannel().sendMessage("Welcome!");
    }

    @Handler
    public void onUserPartChannel(ChannelPartEvent event) {
        // TODO: Replace messages with configuration-based messages

        // Send a message when we leave a channel
        if (event.getClient().isUser(event.getUser())) {
            //event.getChannel().sendMessage("You're fired.");
            return;
        }

        // Send a message when someone else parts a channel
        //event.getChannel().sendMessage("Bye.. You twat.");
    }

    @Handler
    public void onChannelMessage(ChannelMessageEvent event) {
        if (event.getClient().isUser(event.getActor())) {
            return;
        }

        if (event.getMessage().startsWith(Spongie.getConfiguration().globalCategory.commandPrefix)) {
            // TODO: Command parsing
            event.sendReply("Banana.");
        }
    }
}
