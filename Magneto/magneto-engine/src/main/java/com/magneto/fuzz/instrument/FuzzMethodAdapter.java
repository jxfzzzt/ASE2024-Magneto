package com.magneto.fuzz.instrument;

import com.magneto.fuzz.FuzzClassLoader;
import com.magneto.util.MethodUtil;
import lombok.NonNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

public class FuzzMethodAdapter extends GeneratorAdapter implements Opcodes {

    private final int methodAccess;
    private final String methodName;
    private final String methodDesc;
    private final String superName;
    private final boolean isConstructor;
    private boolean hasSetStartLabel;
    private final String methodSignature;

    private final Label startLabel = new Label();
    private final Label endLabel = new Label();
    private final Label handlerLabel = new Label();

    public FuzzMethodAdapter(int api, @NonNull MethodVisitor methodVisitor, String className, int methodAccess, String methodName, String methodDesc, String superName) {
        super(api, methodVisitor, methodAccess, methodName, methodDesc);
        this.methodAccess = methodAccess;
        this.methodName = methodName;
        this.methodSignature = MethodUtil.getMethodSignature(className, methodName, getReturnType(), getArgumentTypes());
        this.methodDesc = methodDesc;
        this.superName = superName;
        this.isConstructor = "<init>".equals(methodName);
        this.hasSetStartLabel = false;
    }


    @Override
    public void visitCode() {
        this.onMethodEnter();
        // (1) startLabel
        if (!isConstructor) {
            super.visitLabel(startLabel);
            this.hasSetStartLabel = true;
        }
        super.visitCode();
    }


    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (isConstructor) {
            if (hasSetStartLabel) {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            } else {
                if (opcode == INVOKESPECIAL && "<init>".equals(name) && superName.equals(owner)) {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    super.visitLabel(startLabel);
                    this.hasSetStartLabel = true;
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                }
            }
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (mv != null) {
            // (1) endLabel
            super.visitLabel(endLabel);

            // (2) handlerLabel
            super.visitLabel(handlerLabel);
            int localIndex = getLocalIndex();
            super.visitVarInsn(ASTORE, localIndex);

            // (3) handle the throwable
            this.onCatchThrowable(localIndex);

            // (4) visitTryCatchBlock
            super.visitTryCatchBlock(startLabel, endLabel, handlerLabel, "java/lang/Throwable");

            // (5) must visit at end
            super.visitMaxs(maxStack, maxLocals);
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            this.onMethodExit(opcode);
        }
        super.visitInsn(opcode);
    }

    protected void onCatchThrowable(int localIndex) {
        if (mv != null) {
            super.visitVarInsn(ALOAD, localIndex);

            String key = this.methodSignature + "#throw";
            mv.visitLdcInsn(key);
            mv.visitMethodInsn(INVOKESTATIC, FuzzClassLoader.STATE_TABLE_INTERNAL_NAME, FuzzClassLoader.ADD_STATE_NODE_METHOD_NAME, "(Ljava/lang/Object;Ljava/lang/String;)V", false);

            super.visitVarInsn(ALOAD, localIndex);
            super.visitInsn(Opcodes.ATHROW);
        }
    }

    protected void onMethodEnter() {
        if (mv != null) {
            boolean isStaticOrConstructor = ((methodAccess & ACC_STATIC) != 0) || "<init>".equals(methodName);

            // record whether reach the method
            mv.visitLdcInsn(this.methodSignature);
            mv.visitMethodInsn(INVOKESTATIC, FuzzClassLoader.STATE_TABLE_INTERNAL_NAME, FuzzClassLoader.ADD_METHOD_SIGNATURE, "(Ljava/lang/String;)V", false);

            // record the input params
            Type[] argumentTypes = super.getArgumentTypes();
            for (int i = 0; i < argumentTypes.length; i++) {
                String key = this.methodSignature + "#" + i;
                mv.visitLdcInsn(key);
                Type t = argumentTypes[i];
                super.loadArg(i);
                super.box(t);
                mv.visitMethodInsn(INVOKESTATIC, FuzzClassLoader.STATE_TABLE_INTERNAL_NAME, FuzzClassLoader.ADD_STATE_NODE_METHOD_NAME, "(Ljava/lang/String;Ljava/lang/Object;)V", false);
            }

            if (!isStaticOrConstructor) {
                String key = this.methodSignature + "#this";
                mv.visitLdcInsn(key);
                super.loadThis();
                mv.visitMethodInsn(INVOKESTATIC, FuzzClassLoader.STATE_TABLE_INTERNAL_NAME, FuzzClassLoader.ADD_STATE_NODE_METHOD_NAME, "(Ljava/lang/String;Ljava/lang/Object;)V", false);
            }
        }
    }

    protected void onMethodExit(int opcode) {
        if (mv != null) {
            if (opcode == ATHROW || opcode == RETURN || "<init>".equals(methodName)) {
                return;
            }

            // instrument the return value
            if (opcode == ARETURN) {
                dup();
                storageReturnValue();
            } else if (opcode == LRETURN || opcode == DRETURN) {
                dup2();
                storageReturnValue();
            } else {
                dup();
                storageReturnValue();
            }
        }
    }

    private void storageReturnValue() {
        if (mv != null) {
            box(getReturnType());
            String key = this.methodSignature + "#return";
            mv.visitLdcInsn(key);
            mv.visitMethodInsn(INVOKESTATIC, FuzzClassLoader.STATE_TABLE_INTERNAL_NAME, FuzzClassLoader.ADD_STATE_NODE_METHOD_NAME, "(Ljava/lang/Object;Ljava/lang/String;)V", false);

        }
    }

    private int getLocalIndex() {
        Type t = Type.getType(methodDesc);
        Type[] argumentTypes = t.getArgumentTypes();

        boolean isStaticMethod = ((methodAccess & ACC_STATIC) != 0);
        int localIndex = isStaticMethod ? 0 : 1;
        for (Type argType : argumentTypes) {
            localIndex += argType.getSize();
        }

        return localIndex;
    }
}
