package org.spongepowered.spongie.impl.plugin.loader.asm;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.AnnotationVisitor;

import javax.annotation.Nullable;

abstract class WarningAnnotationVisitor extends AnnotationVisitor {

    @Inject private Logger logger;

    final String className;

    WarningAnnotationVisitor(int api, String className) {
        super(api);
        this.className = className;
    }

    abstract String getAnnotation();

    @Override
    public void visit(String name, Object value) {
        logger.warn("Found unknown {} annotation element in {}: {} = {}", getAnnotation(), this.className, name, value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        logger.warn("Found unknown {} annotation element in {}: {} ({}) = {}", getAnnotation(), this.className, name, desc, value);
    }

    @Override
    @Nullable
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        logger.warn("Found unknown {} annotation element in {}: {} ({})", getAnnotation(), this.className, name, desc);
        return null;
    }

    @Override
    @Nullable
    public AnnotationVisitor visitArray(String name) {
        logger.warn("Found unknown {} annotation element in {}: {}", getAnnotation(), this.className, name);
        return null;
    }

}
