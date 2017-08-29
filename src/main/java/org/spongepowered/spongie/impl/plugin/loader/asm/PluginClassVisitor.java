/*
 * This file is part of Spongie, All Rights Reserved.
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered//>
 */
package org.spongepowered.spongie.impl.plugin.loader.asm;

import static org.objectweb.asm.Opcodes.ASM5;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;

import javax.annotation.Nullable;

public final class PluginClassVisitor extends ClassVisitor {

    private static final String PLUGIN_DESCRIPTOR = "Lorg/spongepowered/spongie/api/plugin/Plugin;";

    private String className;
    @Nullable private PluginAnnotationVisitor annotationVisitor;

    public PluginClassVisitor() {
        super(ASM5);
    }

    public String getClassName() {
        return this.className;
    }

    public PluginMetadata getMetadata() {
        return this.annotationVisitor != null ? this.annotationVisitor.getMetadata() : null;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
    }

    @Nullable
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (visible && desc.equals(PLUGIN_DESCRIPTOR)) {
            return this.annotationVisitor = new PluginAnnotationVisitor(this.className);
        }

        return null;
    }

}
