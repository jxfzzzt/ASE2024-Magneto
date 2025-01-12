package com.magneto.fuzz.instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FuzzClassAdapter extends ClassVisitor implements Opcodes {

    private final String className;

    private final String internalName;

    private String superName;

    public FuzzClassAdapter(int api, ClassVisitor classVisitor, String className, String internalName) {
        super(api, classVisitor);
        this.className = className;
        this.internalName = internalName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        assert name.equals(className);
        this.superName = superName;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null) {
            boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0;
            boolean isNativeMethod = (access & ACC_NATIVE) != 0;
            if (!isAbstractMethod && !isNativeMethod) {
                mv = new FuzzMethodAdapter(api, mv, className, access, name, descriptor, superName);
            }
        }
        return mv;
    }

    public String getClassName() {
        return className;
    }

    public String getInternalName() {
        return internalName;
    }
}
