package com.magneto.util;

import com.magneto.asm.ClassInfo;
import com.magneto.asm.ClassInfoVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

public class ASMUtil implements Opcodes {

    public static ClassInfo getClassInfo(byte[] bytecode) {
        if (bytecode == null) {
            throw new RuntimeException("bytecode can not be null");
        }

        ClassReader classReader = new ClassReader(bytecode);
        ClassInfoVisitor classInfoVisitor = new ClassInfoVisitor(ASM9);
        classReader.accept(classInfoVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        return classInfoVisitor.getClassInfo();
    }
}
