/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.api.event.lifecycle;

public interface ChangeLifecycleEvent {

    interface Construction extends ChangeLifecycleEvent {

    }

    interface PreInitialization extends ChangeLifecycleEvent {

    }

    interface Intialization extends ChangeLifecycleEvent {

    }

    interface PostInitialization extends ChangeLifecycleEvent {

    }

    interface Ready extends ChangeLifecycleEvent {

    }

    interface Stopping extends ChangeLifecycleEvent {

    }

    interface Stopped extends ChangeLifecycleEvent {

    }
}
