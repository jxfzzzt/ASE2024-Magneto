package com.magneto.asm.traversal;

import cn.hutool.extra.spring.SpringUtil;
import com.magneto.config.ClientProjectProperty;
import com.magneto.config.ConfigProperty;
import com.magneto.config.GlobalClassLoader;
import com.magneto.instrument.SafeClassWriter;
import com.magneto.util.ClassUtil;
import com.magneto.util.FieldUtil;
import com.magneto.util.MethodUtil;
import org.objectweb.asm.*;

import java.io.IOException;
import java.util.*;

// breadth-first-search to get all needed class components
class BreadthFirstSearch implements Opcodes {
    private final Queue<MethodComponent> searchQueue;
    private final Map<String, Set<MethodComponent>> methodComponentMap;
    private final Map<String, Set<FieldComponent>> fieldComponentMap;
    private final Set<String> classSet;
    private Set<String> visitedMethodSet;
    private final Map<MethodComponent, Integer> distanceMap;
    private final Integer maxPromptScope = SpringUtil.getBean(ConfigProperty.class).getMaxPromptScope();

    public BreadthFirstSearch(Queue<MethodComponent> searchQueue, Map<String, Set<MethodComponent>> methodComponentMap, Map<String, Set<FieldComponent>> fieldComponentMap, Set<String> classSet) {
        this.searchQueue = searchQueue;
        this.methodComponentMap = methodComponentMap;
        this.fieldComponentMap = fieldComponentMap;
        this.classSet = classSet;
        this.distanceMap = new HashMap<>();
        // init distance
        for (MethodComponent methodComponent : searchQueue) {
            distanceMap.put(methodComponent, 1);
        }
    }

    public void search() throws IOException {
        visitedMethodSet = new HashSet<>();
        while (!searchQueue.isEmpty()) {
            MethodComponent node = searchQueue.poll();
            visitedMethodSet.add(node.getMethodSignature());

            // get bytecode
            String methodClassName = node.getMethodClassName();

            byte[] bytes = ClassUtil.getClassBytes(ClientProjectProperty.getFuzzLoader(), methodClassName);
            if (bytes == null) {
                throw new RuntimeException("the class bytes is null");
            }

            ClassReader cr = new ClassReader(bytes);
            SafeClassWriter cw = new SafeClassWriter(cr, GlobalClassLoader.getInstance().getClassLoader(), ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = new ClassComponentSearchAdapter(ASM9, cw, node);
            // begin search by ASM
            cr.accept(cv, 0);
        }
    }

    public Map<String, Set<FieldComponent>> getFieldComponentMap() {
        return fieldComponentMap;
    }

    public Map<String, Set<MethodComponent>> getMethodComponentMap() {
        return methodComponentMap;
    }

    class ClassComponentSearchAdapter extends ClassVisitor implements Opcodes {

        private final MethodComponent methodComponent;

        public ClassComponentSearchAdapter(int api, ClassVisitor classVisitor, MethodComponent methodComponent) {
            super(api, classVisitor);
            this.methodComponent = methodComponent;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            assert name.equals(methodComponent.getMethodClassName());
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            String methodSignature = MethodUtil.getMethodSignature(this.methodComponent.getMethodClassName(), name, Type.getReturnType(descriptor), Type.getArgumentTypes(descriptor));
            if (methodComponent.getMethodSignature().equals(methodSignature)) {
                mv = new MethodSearchAdapter(ASM9, mv, methodComponent);
            }
            return mv;
        }
    }

    class MethodSearchAdapter extends MethodVisitor implements Opcodes {

        private final MethodComponent parentNode;

        public MethodSearchAdapter(int api, MethodVisitor methodVisitor, MethodComponent parentNode) {
            super(api, methodVisitor);
            this.parentNode = parentNode;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            Type ownerClassType = Type.getType("L" + owner + ";");

            String methodSignature = MethodUtil.getMethodSignature(ownerClassType.getClassName(), name, Type.getReturnType(descriptor), Type.getArgumentTypes(descriptor));
            if (classSet.contains(ownerClassType.getClassName())) {
                Set<MethodComponent> methodComponentSet = methodComponentMap.getOrDefault(ownerClassType.getClassName(), new HashSet<>());

                MethodComponent methodComponent = new MethodComponent(name, ownerClassType.getClassName(), descriptor, methodSignature, ClientProjectProperty.getMethodMap().getOrDefault(methodSignature, null));

                methodComponentSet.add(methodComponent);
                methodComponentMap.put(ownerClassType.getClassName(), methodComponentSet);
                Integer newDistance = distanceMap.getOrDefault(parentNode, Integer.MAX_VALUE - 1) + 1;
                if (!visitedMethodSet.contains(methodSignature) && newDistance <= maxPromptScope) {
                    distanceMap.put(methodComponent, newDistance);
                    searchQueue.add(methodComponent);
                }
            }
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            super.visitFieldInsn(opcode, owner, name, descriptor);
            Type ownerClassType = Type.getType("L" + owner + ";");

            String fieldSignature = FieldUtil.getFieldSignature(ownerClassType.getClassName(), name, Type.getType(descriptor));

            if (classSet.contains(ownerClassType.getClassName())) {
                Set<FieldComponent> fieldComponentSet = fieldComponentMap.getOrDefault(ownerClassType.getClassName(), new HashSet<>());

                FieldComponent fieldComponent = new FieldComponent(name, ownerClassType.getClassName(), descriptor, fieldSignature, ClientProjectProperty.getFieldMap().getOrDefault(fieldSignature, null));
                fieldComponentSet.add(fieldComponent);
                fieldComponentMap.put(ownerClassType.getClassName(), fieldComponentSet);
            }
        }
    }
}
