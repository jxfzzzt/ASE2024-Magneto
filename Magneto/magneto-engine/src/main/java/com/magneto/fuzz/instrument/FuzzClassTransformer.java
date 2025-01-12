package com.magneto.fuzz.instrument;

import com.magneto.instrument.SafeClassWriter;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

@Slf4j
public class FuzzClassTransformer implements ClassFileTransformer {
    private final static String[] CLASS_BAN_LIST = {"com/magneto", "org/junit", "junit", "[", "java/lang", "org/eclipse/collections", "janala", "org/objectweb/asm", "sun", "jdk", "java/util/function"};

    private boolean shouldExclude(String cname) {
        for (String e : CLASS_BAN_LIST) {
            if (cname.startsWith(e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte[] transform(ClassLoader loader, String classInternalName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classInternalName == null) {
            return null;
        }
        String className = classInternalName.replace('/', '.');

        boolean toInstrument = !shouldExclude(classInternalName);

        if (toInstrument) {
            try {
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new SafeClassWriter(cr, loader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new FuzzClassAdapter(Opcodes.ASM9, cw, className, classInternalName);
                cr.accept(cv, ClassReader.EXPAND_FRAMES | ClassReader.SKIP_DEBUG);
                byte[] byteArray = cw.toByteArray();
                return byteArray;
            } catch (Throwable t) {
                log.warn("{} without instrumentation", classInternalName);
                return null;
            }
        } else {
            return classfileBuffer;
        }
    }
}
