package org.spongepowered.plugin.irc;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.spongie.api.Spongie;
import org.spongepowered.spongie.api.event.Listener;
import org.spongepowered.spongie.api.event.lifecycle.ChangeLifecycleEvent;
import org.spongepowered.spongie.api.event.service.RegisterServiceEvent;
import org.spongepowered.spongie.api.plugin.Plugin;
import org.spongepowered.spongie.api.service.builtin.IRCService;

@Plugin(
        id = "irc",
        name = "IRC",
        version = "0.1",
        description = "Allows Spongie to interact with IRC servers",
        authors = "SpongeDev"
)
public final class IRC {

    @Inject Logger logger;

    IRCService service;

    @Listener
    public void onConstruction(ChangeLifecycleEvent.Construction event) {
        this.logger.info("Constructed IRC plugin!");

        Spongie.getServiceManager().registerService(this, IRCService.class, new SimpleIRCService());
    }

    @Listener
    public void onRegisterService(RegisterServiceEvent event) {
        if (event.getService() instanceof IRCService) {
            if (!(event.getService() instanceof SimpleIRCService)) {
                this.logger.info("Other IRC service detected, will not use fallback.");
            }

            this.service = event.getService();
        }
    }
}
