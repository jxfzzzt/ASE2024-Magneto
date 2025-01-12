package com.magneto.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.magneto.staticanalysis.MethodCall;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class MethodTrimming {
    private final MethodCall targetMethodCall;
    private final MethodCall triggeredMethodCall;
    private final CompilationUnit compilationUnit;

    public MethodTrimming(@NonNull String sourceCode, @NonNull MethodCall targetMethodCall, @NonNull MethodCall triggeredMethodCall) {
        this.targetMethodCall = targetMethodCall;
        this.triggeredMethodCall = triggeredMethodCall;
        this.compilationUnit = StaticJavaParser.parse(sourceCode);
        doTrimming();
    }

    private void doTrimming() {
        List<MethodDeclaration> methodDeclarationList = compilationUnit.findAll(MethodDeclaration.class);
        MethodDeclaration targetMethodDeclaration = null;
        for (MethodDeclaration methodDeclaration : methodDeclarationList) {
            if (isEqualMethod(targetMethodCall, methodDeclaration)) {
                targetMethodDeclaration = methodDeclaration;
                break;
            }
        }
        if (targetMethodDeclaration == null) return;

        BlockStmt blockStmt = targetMethodDeclaration.findAll(BlockStmt.class).get(0);
        List<Node> childNodes = blockStmt.getChildNodes();
        int maxPosition = -1;
        for (int i = 0; i < childNodes.size(); i++) {
            if (containTriggeredMethod(childNodes.get(i))) {
                maxPosition = i;
            }
        }

        assertTrue(maxPosition >= 0);

        List<Node> removedNodes = new ArrayList<>();
        for (int i = maxPosition + 1; i < childNodes.size(); i++) {
            removedNodes.add(childNodes.get(i));
        }
        removedNodes.forEach(Node::remove);
    }

    private boolean containTriggeredMethod(Node node) {
        if (node instanceof MethodCallExpr && triggeredMethodCall.isMethod()) {
            MethodCallExpr expr = (MethodCallExpr) node;
            if (isEqualMethod(triggeredMethodCall, expr)) {
                return true;
            }
        }

        if (node instanceof ObjectCreationExpr && triggeredMethodCall.isConstructor()) {
            ObjectCreationExpr expr = (ObjectCreationExpr) node;
            if (isEqualConstructor(triggeredMethodCall, expr)) {
                return true;
            }
        }

        List<Node> childNodes = node.getChildNodes();
        boolean flag = false;
        for (Node childNode : childNodes) {
            boolean b = containTriggeredMethod(childNode);
            flag = flag || b;
        }
        return flag;
    }

    private boolean isEqualMethod(MethodCall methodCall, MethodDeclaration methodDeclaration) {
        String actualMethodName = methodDeclaration.getNameAsString();
        if (methodCall.getMethodName().equals(actualMethodName)) {
            List<String> needArgTypeList = methodCall.getArgTypeNameList();
            List<Type> actualArgTypeList = methodDeclaration.getParameters().stream().map(Parameter::getType).collect(Collectors.toList());
            if (needArgTypeList.size() == actualArgTypeList.size()) {
                for (int i = 0; i < needArgTypeList.size(); i++) {
                    String needArgType = needArgTypeList.get(i);
                    Type actualArgType = actualArgTypeList.get(i);
                    if (actualArgType instanceof PrimitiveType) {
                        PrimitiveType type = actualArgType.asPrimitiveType();
                        if (!type.asString().equals(needArgType)) {
                            return false;
                        }
                    } else {
                        String actualTypeName = getTrimTypeName(actualArgType.asString());
                        if (actualTypeName.contains(".")) {
                            if (!actualTypeName.equals(needArgType)) return false;
                        } else {
                            String[] split = needArgType.split("\\.");
                            if (!actualTypeName.equals(split[split.length - 1])) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // e.g. Map<String, String> ---> Map
    private String getTrimTypeName(String typeName) {
        int pos = typeName.indexOf('<');
        if (pos == -1) return typeName;
        else return typeName.substring(0, pos);
    }

    private boolean isEqualMethod(MethodCall methodCall, MethodCallExpr methodCallExpr) {
        assertTrue(methodCall.isMethod());

        String actualMethodName = methodCallExpr.getName().asString();
        if (methodCall.getMethodName().equals(actualMethodName)) {
            List<String> needArgTypeList = methodCall.getArgTypeNameList();
            NodeList<Expression> arguments = methodCallExpr.getArguments();
            return needArgTypeList.size() == arguments.size();
        } else {
            return false;
        }
    }

    private boolean isEqualConstructor(MethodCall methodCall, ObjectCreationExpr objectCreationExpr) {
        assertTrue(methodCall.isConstructor());

        String[] split = methodCall.getConstructor().getName().split("\\.");

        String className = split[split.length - 1];

        String typeName = objectCreationExpr.getType().getNameAsString();

        // solve inner classes
        if (typeName.equals(className) || className.contains(typeName)) {
            int argsSize = objectCreationExpr.getArguments().size();
            Integer argNum = methodCall.getArgNum();
            if (argNum == argsSize) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getTrimmingResult() {
        return compilationUnit.toString();
    }

}
