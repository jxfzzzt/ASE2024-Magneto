package com.magneto.asm;

import org.objectweb.asm.*;

public class ClassInfoVisitor extends ClassVisitor implements Opcodes {
    private ClassInfo classInfo;

    public ClassInfoVisitor(int api) {
        super(api);
    }

    public ClassInfoVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.classInfo = new ClassInfo(version, access, name, signature, superName, interfaces);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldInfo fieldInfo = new FieldInfo(access, name, descriptor, signature, value);
        this.classInfo.addFieldInfo(fieldInfo);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodInfo methodInfo = new MethodInfo(access, name, descriptor, signature, exceptions);
        this.classInfo.addMethodInfo(methodInfo);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

}
