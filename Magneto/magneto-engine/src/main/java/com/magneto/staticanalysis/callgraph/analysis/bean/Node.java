package com.magneto.staticanalysis.callgraph.analysis.bean;

import com.magneto.staticanalysis.callgraph.global.CallGraphConstant;
import com.magneto.staticanalysis.callgraph.global.CallGraphGlobal;
import soot.SootMethod;
import soot.Type;
import soot.toolkits.graph.UnitGraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node extends AbstractNode implements Serializable {
    public int isChange;
    /**
     * SootMethod node
     */
    private transient SootMethod method = null;
    /**
     * identifier for node
     */
    private String declaringClass;
    private String methodSignatureParamShort;
    /**
     * Statement relationships, statement parsing results, and relationships between statements and child nodes
     */
    private transient UnitWrapperContainer unitWrapperContainer;
    private transient List<UnitWrapperToNodePair> unitToNodePairList;
    private transient UnitGraph unitGraph;
    /**
     * The variables of the initial method: this, params, returns
     */
    private transient NodeIOVar nodeIOVars;
    /**
     * The key point is to identify variables with data changes in child nodes.
     * These values are used by the parent node to analyze data flow within the parent node.
     */
    private transient List<Var> changeSpot;

    public Node() {

    }

    private Node(SootMethod method) {
        super();
        CallGraphGlobal.sootMethodAll.add(method);
        this.method = method;
        isChange = CallGraphConstant.SAME;
    }

    /**
     * @return true create, false exists
     */
    public static Node createNodeInstance(SootMethod method) {
        if (CallGraphGlobal.sootMethodAll.contains(method)) {
            return CallGraphGlobal.nodeAll.get(method);
        }
        Node node = new Node(method);
        CallGraphGlobal.nodeAll.put(method, node);
        return node;
    }


    public void initUnitWrapperContainer(int index) {
        this.unitWrapperContainer = new UnitWrapperContainer(index);
    }

    public void initUnitToNodePair() {
        this.unitToNodePairList = new ArrayList<>();
    }

//    public void initNodeIOVar() {
//        this.nodeIOVars = new NodeIOVar();
//    }

    public List<UnitWrapperToNodePair> getUnitToNodePairList() {
        return unitToNodePairList;
    }

    public UnitWrapperContainer getUnitWrapperContainer() {
        return unitWrapperContainer;
    }

    public void setUnitWrapperContainer(UnitWrapperContainer unitWrapperContainer) {
        this.unitWrapperContainer = unitWrapperContainer;
    }

    public SootMethod getMethod() {
        return method;
    }

    public void setMethod(SootMethod method) {
        this.method = method;
    }

    public List<Var> getChangeSpot() {
        return changeSpot;
    }

    public void setChangeSpot(List<Var> changeSpot) {
        this.changeSpot = changeSpot;
    }

    public UnitGraph getUnitGraph() {
        return unitGraph;
    }

    public void setUnitGraph(UnitGraph unitGraph) {
        this.unitGraph = unitGraph;
    }

    public String getMethodSignatureFull() {
        List<Type> params = method.getParameterTypes();
        StringBuilder sb = new StringBuilder();
        if (params == null || params.size() == 0) {
            sb.append("()");
        } else {
            sb.append("(");
            for (Type t : params) {
                sb.append(t.toString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }

        return method.getDeclaringClass().getName() + "." + method.getName() + sb;
    }

    public String getMethodSignatureName() {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    /**
     * without params
     *
     * @return
     */
    public String getMethodSignatureShortName() {
        return method.getDeclaringClass().getShortName() + "." + method.getName();
    }

    public void addunitToNodePair(UnitWrapperToNodePair unitWrapperToNodePair) {
        this.unitToNodePairList.add(unitWrapperToNodePair);
    }


    public NodeIOVar getNodeIOVars() {
        return nodeIOVars;
    }

    public void setNodeIOVars(NodeIOVar nodeIOVars) {
        this.nodeIOVars = nodeIOVars;
    }

    public UnitWrapperToNodePair getMappedNodeOfUnitWrapper(UnitWrapper unitWrapper) {
        for (UnitWrapperToNodePair unitWrapperToNodePair : this.unitToNodePairList) {
            if (unitWrapperToNodePair.getFromInvokeStmt() == unitWrapper) {
                return unitWrapperToNodePair;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getMethodSignatureFull();
    }


    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    /**
     * org.Xxx.method(String,int)
     *
     * @return
     */
    public String getMethodSignatureParamShort() {
        return methodSignatureParamShort;
    }

    public void setMethodSignatureParamShort(String methodSignatureParamShort) {
        this.methodSignatureParamShort = methodSignatureParamShort;
    }

    @Override
    public void acceptChild(AbstractVisitor visitor, List nodes) {
        if (nodes == null) {
            return;
        }
        for (Object n : nodes) {
            Node node = (Node) n;

            if (visitor.isPhantom(node)) {
                continue;
            }

            node.accept(visitor);

//            if(node.getMethod().isPhantom()){
//                continue;
//            }
//            node.accept(visitor);
        }
    }
}
