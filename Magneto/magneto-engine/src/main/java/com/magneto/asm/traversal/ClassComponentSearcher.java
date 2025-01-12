package com.magneto.asm.traversal;

import com.magneto.config.ClientProjectProperty;
import com.magneto.config.GlobalClassLoader;
import com.magneto.instrument.SafeClassWriter;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.util.ClassUtil;
import com.magneto.util.FieldUtil;
import com.magneto.util.MethodUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.*;

import java.io.IOException;
import java.util.*;

@Slf4j
public class ClassComponentSearcher implements Opcodes {
    private final MethodCall targetMethodCall;
    private final MethodCall triggeredMethodCall;
    private final Set<String> classSet;
    private final Map<String, Set<MethodComponent>> methodComponentMap;
    private final Map<String, Set<FieldComponent>> fieldComponentMap;
    private final Queue<MethodComponent> searchQueue;
    private final Set<MethodComponent> queuedMethodComponents;
    private boolean hasSearch = false;

    public ClassComponentSearcher(MethodCall targetMethodCall, MethodCall triggeredMethodCall) {
        this.targetMethodCall = targetMethodCall;
        this.triggeredMethodCall = triggeredMethodCall;
        this.methodComponentMap = new HashMap<>();
        this.fieldComponentMap = new HashMap<>();
        this.searchQueue = new LinkedList<>();
        this.queuedMethodComponents = new HashSet<>(); // ensure the method component in initQueue is unique
        // ensure the search components satisfy they all in the same dependency
        this.classSet = ClientProjectProperty.getDependencyClassesMap().get(targetMethodCall.getDependency().getDescriptor());
        assert this.classSet != null;
    }

    private void initQueue() throws IOException {
        Class<?> clazz = targetMethodCall.getClazz();
        String name = clazz.getName();

        // init
        Set<MethodComponent> methodComponentSet = new HashSet<>();
        methodComponentSet.add(new MethodComponent(targetMethodCall.getMethodName(), name,
                targetMethodCall.isMethod() ?
                        Type.getMethodDescriptor(targetMethodCall.getMethod()) : Type.getConstructorDescriptor(targetMethodCall.getConstructor()),
                targetMethodCall.getMethodSignature(), targetMethodCall.getSootMethod()));
        methodComponentMap.put(name, methodComponentSet);

        byte[] bytes = ClassUtil.getClassBytes(ClientProjectProperty.getFuzzLoader(), name);
        if (bytes == null) {
            throw new RuntimeException("the class bytes is null");
        }

        ClassReader cr = new ClassReader(bytes);
        SafeClassWriter cw = new SafeClassWriter(cr, GlobalClassLoader.getInstance().getClassLoader(), ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new InitializationClassAdapter(ASM9, cw, name);
        cr.accept(cv, 0);
    }

    public void doSearch() throws IOException {
        if (!hasSearch) {
            initQueue();
            BreadthFirstSearch search = new BreadthFirstSearch(this.searchQueue, this.methodComponentMap, this.fieldComponentMap, this.classSet);
            search.search();
            hasSearch = true;
        }
    }

    public void validate() {
        if (hasSearch) {
            for (Map.Entry<String, Set<FieldComponent>> entry : fieldComponentMap.entrySet()) {
                if (!classSet.contains(entry.getKey())) {
                    throw new RuntimeException("validate search result fail");
                }
            }
            for (Map.Entry<String, Set<MethodComponent>> entry : methodComponentMap.entrySet()) {
                if (!classSet.contains(entry.getKey())) {
                    throw new RuntimeException("validate search result fail");
                }
            }
        }
    }

    private Queue<MethodComponent> getSearchQueue() {
        return this.searchQueue;
    }

    public MethodCall getTriggeredMethodCall() {
        return triggeredMethodCall;
    }

    public MethodCall getTargetMethodCall() {
        return targetMethodCall;
    }

    public Map<String, Set<MethodComponent>> getMethodComponentMap() {
        return methodComponentMap;
    }

    public Map<String, Set<FieldComponent>> getFieldComponentMap() {
        return fieldComponentMap;
    }

    class InitializationClassAdapter extends ClassVisitor {

        private final String className;

        public InitializationClassAdapter(int api, ClassVisitor classVisitor, @NonNull String className) {
            super(api, classVisitor);
            this.className = className;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            assert className.equals(name);
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            String methodSignature = MethodUtil.getMethodSignature(className, name, Type.getReturnType(descriptor), Type.getArgumentTypes(descriptor));
            if (targetMethodCall.getMethodSignature().equals(methodSignature)) {
                mv = new InitializationMethodAdapter(ASM9, mv);
            }
            return mv;
        }
    }

    class InitializationMethodAdapter extends MethodVisitor {
        private boolean hasMeetTriggeredMethod;

        public InitializationMethodAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
            this.hasMeetTriggeredMethod = false;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            Type ownerClassType = Type.getType("L" + owner + ";");

            String methodSignature = MethodUtil.getMethodSignature(ownerClassType.getClassName(), name, Type.getReturnType(descriptor), Type.getArgumentTypes(descriptor));

            // check whether trigger the vulnerable method
            if (!this.hasMeetTriggeredMethod && classSet.contains(ownerClassType.getClassName())
                    && !methodSignature.equals(triggeredMethodCall.getMethodSignature())) {
                Set<MethodComponent> methodComponentSet = getMethodComponentMap().getOrDefault(ownerClassType.getClassName(), new HashSet<>());
                MethodComponent methodComponent = new MethodComponent(name, ownerClassType.getClassName(), descriptor, methodSignature, ClientProjectProperty.getMethodMap().getOrDefault(methodSignature, null));

                methodComponentSet.add(methodComponent);
                getMethodComponentMap().put(ownerClassType.getClassName(), methodComponentSet);

                // add method component to Queue
                if (!queuedMethodComponents.contains(methodComponent)) {
                    searchQueue.add(methodComponent);
                    queuedMethodComponents.add(methodComponent);
                }
            }

            if (methodSignature.equals(triggeredMethodCall.getMethodSignature())) {
                this.hasMeetTriggeredMethod = true;
            }
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            super.visitFieldInsn(opcode, owner, name, descriptor);
            Type ownerClassType = Type.getType("L" + owner + ";");

            String fieldSignature = FieldUtil.getFieldSignature(ownerClassType.getClassName(), name, Type.getType(descriptor));

            // check whether trigger the vulnerable method
            if (!this.hasMeetTriggeredMethod && classSet.contains(ownerClassType.getClassName())) {
                Set<FieldComponent> fieldComponentSet = getFieldComponentMap().getOrDefault(ownerClassType.getClassName(), new HashSet<>());
                FieldComponent fieldComponent = new FieldComponent(name, ownerClassType.getClassName(), descriptor, fieldSignature, ClientProjectProperty.getFieldMap().getOrDefault(fieldSignature, null));
                fieldComponentSet.add(fieldComponent);
                getFieldComponentMap().put(ownerClassType.getClassName(), fieldComponentSet);
            }
        }
    }
}
