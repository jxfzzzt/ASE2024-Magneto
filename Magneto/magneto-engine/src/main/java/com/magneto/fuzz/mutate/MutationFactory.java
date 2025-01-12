package com.magneto.fuzz.mutate;

import com.magneto.fuzz.mutate.strategy.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Type;

import java.io.File;
import java.util.List;

@Slf4j
public class MutationFactory {

    private final static String[] NATIVE_CLASS_LIST = {"com/magneto", "org/junit", "junit", "java", "[",
            "org/eclipse/collections", "janala", "org/objectweb/asm", "sun", "jdk", "javax"};

    public static MutationStrategy getStrategy(@NonNull Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return new MutatePrimitiveStrategy();
        } else if (clazz.isEnum()) {
            return new MutateEnumStrategy();
        } else if (clazz.isArray()) {
            return new MutateArrayStrategy();
        } else if (clazz == Class.class) {
            return new MutateClassStrategy();
        } else if (clazz == String.class || clazz == Character.class) {
            return new MutateStringStrategy();
        } else if (clazz == File.class) {
            return new MutateFileStrategy();
        } else if (List.class.isAssignableFrom(clazz)) {
            return new MutateListStrategy();
        } else if (checkClassNamePrefixIsNative(clazz)) {
            return new DefaultStrategy();
        } else {
            return new MutateReferenceStrategy();
        }
    }

    private static boolean checkClassNamePrefixIsNative(Class<?> clazz) {
        String internalName = Type.getType(clazz).getInternalName();
        for (String e : NATIVE_CLASS_LIST) {
            if (internalName.startsWith(e)) {
                return true;
            }
        }
        return false;
    }
}
