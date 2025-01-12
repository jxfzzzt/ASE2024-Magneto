package com.magneto.util;

import com.magneto.config.ClientProjectProperty;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Type;
import soot.SootField;

import java.lang.reflect.Field;

@Slf4j
public class FieldUtil {

    public static String getFieldSignature(Class clazz, Field field) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(clazz.getName());
        sb.append(": ");
        sb.append(corp(field.getType().getName()));
        sb.append(" ");
        sb.append(field.getName());
        sb.append(">");
        return sb.toString();
    }

    public static String getFieldSignature(SootField sootField) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(sootField.getDeclaringClass().getName());
        sb.append(": ");
        sb.append(corp(sootField.getType().toQuotedString()));
        sb.append(" ");
        sb.append(sootField.getName());
        sb.append(">");
        return sb.toString();
    }

    public static String getFieldSignature(String className, String fieldName, Type fieldType) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(className);
        sb.append(": ");
        sb.append(corp(fieldType.getClassName()));
        sb.append(" ");
        sb.append(fieldName);
        sb.append(">");
        return sb.toString();
    }

    public static Field getFieldBySignature(String fieldSignature) {
        fieldSignature = removeQuote(fieldSignature);
        String[] split = fieldSignature.split(":");
        String className = split[0].trim();
        String suffixName = split[1].trim();
        String typeName = suffixName.split(" ")[0];
        String fieldName = suffixName.split(" ")[1];
        try {
            Class<?> clazz = ClientProjectProperty.getFuzzLoader().loadClass(className);
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.getType().getName().equals(typeName) &&
                        declaredField.getName().equals(fieldName)) {
                    return declaredField;
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static String removeQuote(String signature) {
        if (signature.charAt(0) == '<' && signature.charAt(signature.length() - 1) == '>') {
            return signature.substring(1, signature.length() - 1);
        } else {
            return signature;
        }
    }

    private static String corp(String name) {
        if (name.charAt(0) == '[') {
            int j = 0;
            int cnt = 0;
            while (name.charAt(j) == '[') {
                j++;
                cnt++;
            }

            if (name.charAt(j) == 'L') j++;

            name = name.substring(j);

            if (name.charAt(name.length() - 1) == ';') {
                name = name.substring(0, name.length() - 1);
            }

            switch (name) {
                case "V":
                    name = "void";
                    break;
                case "Z":
                    name = "boolean";
                    break;
                case "B":
                    name = "byte";
                    break;
                case "C":
                    name = "char";
                    break;
                case "S":
                    name = "short";
                    break;
                case "I":
                    name = "int";
                    break;
                case "J":
                    name = "long";
                    break;
                case "F":
                    name = "float";
                    break;
                case "D":
                    name = "double";
                    break;
                default:
                    break;
            }

            StringBuilder sb = new StringBuilder(name);
            for (int i = 0; i < cnt; i++) sb.append("[]");
            name = sb.toString();
            return name;
        } else {
            return name;
        }
    }

}
