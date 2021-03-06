/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.old;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

    public static final class Filesystem {

        public static final Path PATH_ROOT = Paths.get(".");
        public static final String CONFIG_NAME = "settings.conf";
    }

    public static final class Text {

        public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    }
}
