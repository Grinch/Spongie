package org.spongepowered.spongie.impl.plugin.loader.asm;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.objectweb.asm.Opcodes.ASM5;

import com.google.common.base.Preconditions;
import org.objectweb.asm.AnnotationVisitor;
import org.spongepowered.spongie.impl.plugin.loader.InvalidPluginException;
import org.spongepowered.spongie.impl.plugin.loader.meta.PluginMetadata;

final class PluginAnnotationVisitor extends WarningAnnotationVisitor {

    private enum State {
        DEFAULT, AUTHORS, DEPENDENCIES
    }

    private final PluginMetadata.Builder metadataBuilder = new PluginMetadata.Builder();
    private PluginMetadata metadata;

    private State state = State.DEFAULT;
    private boolean hasId;

    PluginAnnotationVisitor(String className) {
        super(ASM5, className);
    }

    PluginMetadata getMetadata() {
        if (this.metadata == null) {
            this.metadata = metadataBuilder.build();
        }
        return this.metadata;
    }

    @Override
    String getAnnotation() {
        return "@Plugin";
    }

    private void checkState(State state) {
        Preconditions.checkState(this.state == state, "Expected state %s, but is %s", state, this.state);
    }

    @Override
    public void visit(String name, Object value) {
        if (this.state == State.AUTHORS) {
            this.metadataBuilder.addAuthor((String) value);
            return;
        }

        checkState(State.DEFAULT);
        checkNotNull(name, "name");

        switch (name) {
            case "id":
                this.hasId = true;
                this.metadataBuilder.id((String) value);
                return;
            case "name":
                this.metadataBuilder.name((String) value);
                return;
            case "version":
                this.metadataBuilder.version((String) value);
                return;
            case "description":
                this.metadataBuilder.description((String) value);
                return;
            default:
                super.visit(name, value);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        if (this.state == State.DEPENDENCIES) {
            return new DependencyAnnotationVisitor(this.className, this.metadataBuilder);
        }
        return super.visitAnnotation(name, desc);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        checkNotNull(name, "name");

        switch (name) {
            case "authors":
                this.state = State.AUTHORS;
                return this;
            case "dependencies":
                this.state = State.DEPENDENCIES;
                return this;
            default:
                return super.visitArray(name);
        }
    }

    @Override
    public void visitEnd() {
        if (this.state != State.DEFAULT) {
            this.state = State.DEFAULT;
            return;
        }

        if (!this.hasId) {
            throw new InvalidPluginException("Plugin annotation is missing required element 'id'");
        }
    }

}
