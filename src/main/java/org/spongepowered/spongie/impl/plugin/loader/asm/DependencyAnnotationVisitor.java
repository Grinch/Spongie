package org.spongepowered.spongie.impl.plugin.loader.asm;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.objectweb.asm.Opcodes.ASM5;

import org.spongepowered.spongie.api.plugin.meta.PluginDependency;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;
import org.spongepowered.spongie.impl.plugin.meta.SpongiePluginDependency;

import javax.annotation.Nullable;

final class DependencyAnnotationVisitor extends WarningAnnotationVisitor {

    private final PluginMetadata.Builder metadataBuilder;

    @Nullable private String id;
    @Nullable private String version;
    private boolean optional;

    DependencyAnnotationVisitor(String className, PluginMetadata.Builder metadataBuilder) {
        super(ASM5, className);
        this.metadataBuilder = metadataBuilder;
    }

    @Override
    String getAnnotation() {
        return "@Dependency";
    }

    @Override
    public void visit(String name, Object value) {
        checkNotNull(name, "name");

        switch (name) {
            case "id":
                this.id = (String) value;
                return;
            case "version":
                this.version = (String) value;
                return;
            case "optional":
                this.optional = (boolean) value;
                return;
            default:
                super.visit(name, value);
        }
    }

    @Override
    public void visitEnd() {
        if (this.id == null) {
            throw new IllegalArgumentException("Dependency plugin ID is required");
        }

        this.metadataBuilder.addDependency(new SpongiePluginDependency(this.id, this.version, this.optional, PluginDependency.LoadOrder.BEFORE));
    }
}
