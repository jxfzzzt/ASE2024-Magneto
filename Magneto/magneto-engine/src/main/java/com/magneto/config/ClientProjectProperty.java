package com.magneto.config;

import cn.hutool.core.lang.Pair;
import com.magneto.dependency.MavenDependency;
import com.magneto.dependency.MavenDependencyTree;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.MethodSet;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientProjectProperty {

    private static String classPath; // classespath

    private static String projectJarPath; // client project jar path

    private static List<String> dependencyJarPaths; // project dependency jar file path

    private static MavenDependencyTree dependencyTree; // dependency tree

    private static Map<String, MavenDependency> methodDependencyMap; // method signature --> maven dependency

    private static Map<String, MethodCall> methodCallMap; // SootMethod signature ---> method call

    private static Map<String, MethodSet> depMethodSetMap; //  dependency key name --> methodSet

    private static List<SootMethod> entryMethods; // entry method for call graph build

    private static Map<String, SootClass> classMap; // class name ---> SootClass

    private static Map<String, SootMethod> methodMap; // method signature ---> SootMethod

    private static Map<String, SootField> fieldMap; // field signature ---> SootField

    private static Set<String> clientProjectClassSet; // client project class set

    private static Map<String, Set<String>> dependencyClassesMap; // dependency descriptor ---> classes set

    private static Map<String, Set<Class>> inheritMap; // inherit or implement relation map

    private static Map<Pair<String, String>, String> dependencyVersionMap; // (groupId, artifactId) ---> version

    private static ClassLoader instrumentedFuzzLoader; // instrumented client project classloader

    private static ClassLoader fuzzLoader; // not instrumented client project classloader

    public static Map<Pair<String, String>, String> getDependencyVersionMap() {
        return dependencyVersionMap;
    }

    public static void setDependencyVersionMap(Map<Pair<String, String>, String> dependencyVersionMap) {
        ClientProjectProperty.dependencyVersionMap = dependencyVersionMap;
    }

    public static String getClassPath() {
        return classPath;
    }

    public static String getProjectJarPath() {
        return projectJarPath;
    }

    public static Map<String, Set<String>> getDependencyClassesMap() {
        return dependencyClassesMap;
    }

    public static void setDependencyClassesMap(Map<String, Set<String>> dependencyClassesMap) {
        ClientProjectProperty.dependencyClassesMap = dependencyClassesMap;
    }

    public static List<String> getDependencyJarPaths() {
        return dependencyJarPaths;
    }

    public static MavenDependencyTree getDependencyTree() {
        return dependencyTree;
    }

    public static Map<String, MethodCall> getMethodCallMap() {
        return methodCallMap;
    }

    public static Map<String, MethodSet> getDepMethodSetMap() {
        return depMethodSetMap;
    }

    public static List<SootMethod> getEntryMethods() {
        return entryMethods;
    }

    public static void setClassPath(String classPath) {
        ClientProjectProperty.classPath = classPath;
    }

    public static void setClientProjectClassSet(Set<String> clientProjectClassSet) {
        ClientProjectProperty.clientProjectClassSet = clientProjectClassSet;
    }

    public static Set<String> getClientProjectClassSet() {
        return clientProjectClassSet;
    }

    public static void setProjectJarPath(String projectJarPath) {
        ClientProjectProperty.projectJarPath = projectJarPath;
    }

    public static void setDependencyTree(MavenDependencyTree dependencyTree) {
        ClientProjectProperty.dependencyTree = dependencyTree;
    }

    public static void setDependencyJarPaths(List<String> dependencyJarPaths) {
        ClientProjectProperty.dependencyJarPaths = dependencyJarPaths;
    }

    public static void setDepMethodSetMap(Map<String, MethodSet> depMethodSetMap) {
        ClientProjectProperty.depMethodSetMap = depMethodSetMap;
    }

    public static void setEntryMethods(List<SootMethod> entryMethods) {
        ClientProjectProperty.entryMethods = entryMethods;
    }

    public static void setMethodCallMap(Map<String, MethodCall> methodCallMap) {
        ClientProjectProperty.methodCallMap = methodCallMap;
    }

    public static void setMethodDependencyMap(Map<String, MavenDependency> methodDependencyMap) {
        ClientProjectProperty.methodDependencyMap = methodDependencyMap;
    }

    public static Map<String, Set<Class>> getInheritMap() {
        return inheritMap;
    }

    public static void setInheritMap(Map<String, Set<Class>> inheritMap) {
        ClientProjectProperty.inheritMap = inheritMap;
    }

    public static Map<String, MavenDependency> getMethodDependencyMap() {
        return methodDependencyMap;
    }

    public static Map<String, SootClass> getClassMap() {
        return classMap;
    }

    public static Map<String, SootMethod> getMethodMap() {
        return methodMap;
    }

    public static void setClassMap(Map<String, SootClass> classMap) {
        ClientProjectProperty.classMap = classMap;
    }

    public static void setMethodMap(Map<String, SootMethod> methodMap) {
        ClientProjectProperty.methodMap = methodMap;
    }

    public static Map<String, SootField> getFieldMap() {
        return fieldMap;
    }

    public static void setFieldMap(Map<String, SootField> fieldMap) {
        ClientProjectProperty.fieldMap = fieldMap;
    }

    public static ClassLoader getFuzzLoader() {
        return fuzzLoader;
    }

    public static void setFuzzLoader(ClassLoader fuzzLoader) {
        ClientProjectProperty.fuzzLoader = fuzzLoader;
    }

    public static ClassLoader getInstrumentedFuzzLoader() {
        return instrumentedFuzzLoader;
    }

    public static void setInstrumentedFuzzLoader(ClassLoader instrumentedFuzzLoader) {
        ClientProjectProperty.instrumentedFuzzLoader = instrumentedFuzzLoader;
    }
}
