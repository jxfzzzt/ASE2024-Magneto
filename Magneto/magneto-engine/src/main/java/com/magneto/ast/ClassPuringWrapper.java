package com.magneto.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.magneto.asm.traversal.FieldComponent;
import com.magneto.asm.traversal.MethodComponent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//remove unrelated methods and fields in class
@Slf4j
public class ClassPuringWrapper {

    private CompilationUnit compilationUnit;

    private Set<String> savedFieldNameSet;

    private Set<MethodComponent> savedMethodComponentSet;

    public ClassPuringWrapper(@NonNull String sourceCode,
                              @NonNull Set<FieldComponent> fieldComponentSet,
                              @NonNull Set<MethodComponent> methodComponentSet) {

        try {
            this.compilationUnit = StaticJavaParser.parse(sourceCode);
        } catch (Exception e) {
            this.compilationUnit = null;
            log.warn("static java parser fail");
        }

        if (compilationUnit != null) {
            List<Comment> allComments = compilationUnit.getAllComments();
            if (!allComments.isEmpty()) {
                allComments.get(0).remove(); // remove the decompile comment
            }
            this.savedMethodComponentSet = new HashSet<>();

            // resolve the lambda function
            for (MethodComponent methodComponent : methodComponentSet) {
                if (checkLambdaFunction(methodComponent.getMethodName())) {
                    String methodName = methodComponent.getMethodName();
                    methodName = methodName.substring(methodName.indexOf("$") + 1, methodName.lastIndexOf("$"));
                    methodComponent.setMethodName(methodName);
                    savedMethodComponentSet.add(methodComponent);
                } else {
                    savedMethodComponentSet.add(methodComponent);
                }
            }

            this.savedFieldNameSet = fieldComponentSet.stream().map(FieldComponent::getFieldName).collect(Collectors.toSet());
            puringClass();
        }
    }

    private boolean checkLambdaFunction(String methodName) {
        int cnt = 0;
        for (int i = 0; i < methodName.length(); i++) {
            if (methodName.charAt(i) == '$') cnt++;
        }
        return cnt == 2;
    }

    private void puringClass() {
        ClassPuringVisitor classPuringVisitor = new ClassPuringVisitor();
        Set<Node> removedNode = new HashSet<>();
        classPuringVisitor.visit(compilationUnit, removedNode);
        for (Node node : removedNode) {
            node.remove();
        }
    }

    public String getPuringResult() {
        if (compilationUnit != null) {
            return compilationUnit.toString();
        } else {
            log.warn("puring result is null");
            return null;
        }
    }

    @Override
    public String toString() {
        return compilationUnit.toString();
    }

    class ClassPuringVisitor extends VoidVisitorAdapter<Set<Node>> {

        public ClassPuringVisitor() {

        }

        @Override
        public void visit(FieldDeclaration n, Set<Node> arg) {
            super.visit(n, arg);
            assert !n.getVariables().isEmpty();
            VariableDeclarator variableDeclarator = n.getVariables().get(0);
            String fieldName = variableDeclarator.getNameAsString();
            if (!savedFieldNameSet.contains(fieldName)) {
                arg.add(n);
            }
        }

        @Override
        public void visit(MethodDeclaration n, Set<Node> arg) {
            super.visit(n, arg);

            boolean flag = false;
            for (MethodComponent methodComponent : savedMethodComponentSet) {
                if (isEqualMethod(methodComponent, n)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                arg.add(n);
            }
        }

        private boolean isEqualMethod(MethodComponent methodComponent, MethodDeclaration methodDeclaration) {
            if (methodComponent.getMethodName().equals(methodDeclaration.getNameAsString())) {

                if (methodComponent.isLambda()) {
                    return true;
                }

                List<String> needArgTypeList = methodComponent.getArgTypeNameList();
                if (needArgTypeList == null) {
                    return false;
                }
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
    }
}
