package com.magneto.staticanalysis;

import com.magneto.config.ClientProjectProperty;
import com.magneto.dependency.MavenDependency;
import com.magneto.dependency.MavenDependencyTree;
import com.magneto.util.ClassUtil;
import com.magneto.util.MethodUtil;
import lombok.extern.slf4j.Slf4j;
import soot.*;
import soot.options.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class PropertyAnalysis {
    private static PropertyAnalysis INSTANCE = null;

    protected final String classPath; // classespath

    protected final String projectJarPath; // client project jar path

    protected final List<String> dependencyJarPaths; // project dependency jar file path

    protected final MavenDependencyTree dependencyTree; // dependency tree

    protected final Map<String, MavenDependency> methodDependencyMap; // method signature --> maven dependency

    protected final Map<String, MethodCall> methodCallMap; // SootMethod signature ---> method call

    protected final Map<String, MethodSet> depMethodSetMap; //  dependency key name --> methodSet

    protected final List<SootMethod> entryMethods; // entry method for call graph build

    protected final Map<String, Set<Class>> inheritMap; // inherit or implement relation

    protected final Map<String, Set<String>> dependencyClassesMap; // dependency descriptor ---> class name set

    public static PropertyAnalysis getInstance(MavenDependencyTree dependencyTree, String projectJarPath, List<String> dependencyJarPaths) throws IOException, ClassNotFoundException {
        synchronized (PropertyAnalysis.class) {
            if (INSTANCE == null) {
                INSTANCE = new PropertyAnalysis(dependencyTree, projectJarPath, dependencyJarPaths);
            }
            return INSTANCE;
        }
    }

    private PropertyAnalysis(MavenDependencyTree dependencyTree, String projectJarPath, List<String> dependencyJarPaths) throws IOException, ClassNotFoundException {
        this.classPath = dependencyTree.getClasspathFile().getAbsolutePath();
        this.projectJarPath = projectJarPath;
        this.dependencyJarPaths = dependencyJarPaths;

        if (projectJarPath == null || dependencyJarPaths == null || dependencyJarPaths.contains(null)) {
            throw new RuntimeException("jar path can not be null");
        }
        this.dependencyTree = dependencyTree;
        this.methodDependencyMap = new HashMap<>();
        this.methodCallMap = new HashMap<>();
        this.depMethodSetMap = new HashMap<>();
        this.entryMethods = new ArrayList<>();
        this.inheritMap = new HashMap<>();
        this.dependencyClassesMap = new HashMap<>();
        defaultInitial(); // init soot, but do not run soot analysis
        loadEntryPoint(); // load entry methods
        preprocess(); // gather class and method information
        // transfer the result to ClientProjectProperty
        transfer();
    }

    private void inheritSearch(Class<?> clazz) {
        if (clazz == null || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return;
        }

        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            // search implement relation
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (anInterface != null) {
                    Set<Class> classSet = inheritMap.getOrDefault(anInterface.getName(), new HashSet<>());
                    classSet.add(clazz);
                    inheritMap.put(anInterface.getName(), classSet);
                }
            }

            // search parent class
            Class<?> superclass = currentClass.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                Set<Class> classSet = inheritMap.getOrDefault(superclass.getName(), new HashSet<>());
                classSet.add(clazz);
                inheritMap.put(superclass.getName(), classSet);
            }
            currentClass = superclass;
        }
    }

    private void transfer() throws IOException {
        ClientProjectProperty.setClassPath(this.classPath);
        ClientProjectProperty.setProjectJarPath(this.projectJarPath);
        ClientProjectProperty.setDependencyTree(this.dependencyTree);
        ClientProjectProperty.setDependencyJarPaths(this.dependencyJarPaths);
        ClientProjectProperty.setDepMethodSetMap(this.depMethodSetMap);
        ClientProjectProperty.setEntryMethods(this.entryMethods);
        ClientProjectProperty.setMethodCallMap(this.methodCallMap);
        ClientProjectProperty.setMethodDependencyMap(this.methodDependencyMap);
        ClientProjectProperty.setDependencyVersionMap(this.dependencyTree.getVersionMap());
        ClientProjectProperty.setInheritMap(this.inheritMap);
        ClientProjectProperty.setDependencyClassesMap(dependencyClassesMap);

        Map<String, SootClass> classMap = new HashMap<>();
        Map<String, SootMethod> methodMap = new HashMap<>();
        Map<String, SootField> fieldMap = new HashMap<>();
        for (SootClass sootClass : Scene.v().getClasses()) {
            classMap.put(sootClass.getName(), sootClass);
            for (SootMethod method : sootClass.getMethods()) {
                methodMap.put(method.getSignature(), method);
            }
            for (SootField field : sootClass.getFields()) {
                fieldMap.put(field.getSignature(), field);
            }
        }
        ClientProjectProperty.setClassMap(classMap);
        ClientProjectProperty.setMethodMap(methodMap);
        ClientProjectProperty.setFieldMap(fieldMap);

        Set<String> projectClassSet = ClassUtil.getClassesFromJar(this.projectJarPath).stream().map(Class::getName).collect(Collectors.toSet());
        ClientProjectProperty.setClientProjectClassSet(projectClassSet);
    }

    private void defaultInitial() {
        G.reset();
        List<String> argsList = new ArrayList<>();

        argsList.addAll(Arrays.asList("-allow-phantom-refs",
                "-w",
                "-keep-line-number", "enabled"
        ));

        for (String s : dependencyJarPaths) {
            argsList.add("-process-dir");
            argsList.add(s);
        }

        argsList.add("--process-dir");
        argsList.add(projectJarPath);

        argsList.addAll(Arrays.asList("-p", "jb", "use-original-names:true"));
        String[] args;
        args = argsList.toArray(new String[0]);

        Options.v().parse(args);
        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_keep_line_number(true);
        Options.v().set_verbose(true);
        Options.v().set_keep_line_number(true);
        Options.v().setPhaseOption("cg", "all-reachable:true");
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_app(true);
        // load necessary class and method
        Scene.v().loadNecessaryClasses();
    }

    // load entry point method (methods in client project)
    private void loadEntryPoint() throws IOException {
        entryMethods.clear();
        List<Class> classes = ClassUtil.getClassesFromJar(projectJarPath);

        for (Class clazz : classes) {
            Method[] declaredMethods;
            try {
                declaredMethods = clazz.getDeclaredMethods();
            } catch (Throwable e) {
                continue;
            }

            for (Method method : declaredMethods) {
                String signature = MethodUtil.getMethodSignature(clazz, method);
                SootMethod sootMethod = this.getSootMethodBySignature(signature);

                if (sootMethod == null || sootMethod.isPhantom()) {
                    continue;
                }

                entryMethods.add(sootMethod);
            }

            Constructor[] declaredConstructors;
            try {
                declaredConstructors = clazz.getDeclaredConstructors();
            } catch (Throwable e) {
                continue;
            }

            for (Constructor constructor : declaredConstructors) {
                String signature = MethodUtil.getMethodSignature(clazz, constructor);
                SootMethod sootMethod = this.getSootMethodBySignature(signature);

                if (sootMethod == null || sootMethod.isPhantom()) {
                    continue;
                }

                entryMethods.add(sootMethod);
            }
        }
    }

    private void preprocess() throws IOException {
        List<Class> classList = null;
        // process client project
        classList = ClassUtil.getClassesFromJar(projectJarPath);
        for (Class<?> clazz : classList) {
            inheritSearch(clazz);
            processClass(dependencyTree.getRootNode(), clazz);
        }

        // preprocess dependency jar
        Map<MavenDependency, String> dependencyJarPathMap = this.dependencyTree.getDependencyJarPathMap();
        Set<MavenDependency> dependencyJarSet = dependencyJarPathMap.keySet();

        for (MavenDependency dependency : dependencyJarSet) {
            if (dependency == null) {
                throw new RuntimeException("dependency is null");
            }

            File jarFile = dependency.getJarFile();
            if(jarFile == null) {
                throw new RuntimeException("jar file is null");
            }
            classList = ClassUtil.getClassesFromJar(jarFile.getAbsolutePath());
            Set<String> classNameSet = classList.stream().map(Class::getName).collect(Collectors.toSet());
            dependencyClassesMap.put(dependency.getDescriptor(), classNameSet);

            for (Class<?> clazz : classList) {
                try {
                    inheritSearch(clazz);
                    processClass(dependency, clazz);
                } catch (Throwable ignored) {
                    log.warn("load '{}' fail", clazz.getName());
                }
            }
        }
    }

    private void processClass(MavenDependency dependency, Class<?> clazz) {
        MethodSet methodSet = depMethodSetMap.getOrDefault(dependency.getDependencyKey(), new MethodSet());
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();

            // methods
            for (Method method : declaredMethods) {
                String signature = MethodUtil.getMethodSignature(clazz, method);
                SootMethod sootMethod = this.getSootMethodBySignature(signature);
                methodDependencyMap.put(signature, dependency);

                if (sootMethod == null || sootMethod.isPhantom()) {
                    log.warn("soot can not load the method: {}", signature);
                    continue;
                }

                MethodCall methodCall = new MethodCall(dependency, clazz, method, sootMethod);
                methodSet.add(sootMethod);

                if (this.methodCallMap.containsKey(signature)) {
                    throw new RuntimeException("duplicate method signature");
                }

                this.methodCallMap.put(signature, methodCall);
            }
        } catch (Throwable ignored) {
        }

        // constructor methods
        try {
            Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
            for (Constructor constructor : declaredConstructors) {
                String signature = MethodUtil.getMethodSignature(clazz, constructor);
                SootMethod sootMethod = this.getSootMethodBySignature(signature);
                methodDependencyMap.put(signature, dependency);

                if (sootMethod == null || sootMethod.isPhantom()) {
                    log.warn("soot can not load the constructor: {}", signature);
                    continue;
                }

                MethodCall methodCall = new MethodCall(dependency, clazz, constructor, sootMethod);
                methodSet.add(sootMethod);

                if (this.methodCallMap.containsKey(signature)) {
                    throw new RuntimeException("duplicate method signature");
                }

                this.methodCallMap.put(signature, methodCall);
            }
        } catch (Throwable ignored) {
        }
        depMethodSetMap.put(dependency.getDependencyKey(), methodSet);
    }

    private SootMethod getSootMethodBySignature(String methodSignature) {
        try {
            return Scene.v().getMethod(methodSignature);
        } catch (Exception e) {
            return null;
        }
    }
}
