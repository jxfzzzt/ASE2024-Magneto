package com.magneto.staticanalysis.callgraph.analysis.init;

import com.magneto.staticanalysis.callgraph.analysis.bean.*;
import com.magneto.staticanalysis.callgraph.analysis.dataflow.AbstractStmt;
import com.magneto.staticanalysis.callgraph.analysis.dataflow.*;
import com.magneto.staticanalysis.callgraph.global.CallGraphConstant;
import lombok.extern.slf4j.Slf4j;
import soot.*;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.internal.*;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class UnitParser {

    /**
     * Parse the statements of sootMethod in the Node and place the results into the Node
     */
    public static void runParseUnit(Node node) {
        if (node.getUnitWrapperContainer() != null && !node.getUnitWrapperContainer().isUnitParsed()) {
            createUnitWrapperContainer(node);
        }
    }

    /**
     * map
     *
     * @param node
     */
    public static void runMapUnitToNode(Node node, Map<UnitWrapper, List<SootMethod>> kvMap) {
//        parseUnitsMapUnitToNode(node,node.getChildren());
        parseUnitsMapUnitToNode(node, kvMap);
    }

    /**
     * @param node
     */
    public static void runUnitGraph(Node node) {
        try {
            SootMethod sootMethod = node.getMethod();
            Body body = sootMethod.retrieveActiveBody();
            UnitGraph ug = new BriefUnitGraph(body);
            node.setUnitGraph(ug);
        } catch (RuntimeException e) {
            log.error("RetrieveMethodBodyWrong: ", e);
        }
    }

    public static void parseNodeIO(Node node) {
        NodeIOVar nodeIOVar = new NodeIOVar();
        List<UnitWrapper> uws = node.getUnitWrapperContainer().getUnitWrappers();
        List<Var> outputParams = new ArrayList<>();
        List<Var> inputParams = new ArrayList<>();
        for (UnitWrapper uw : uws) {
            AbstractStmt stmt = uw.parsedStmt;
            if (stmt instanceof ParameterStmt) {
                ParameterStmt parameterAssignment = (ParameterStmt) stmt;
                inputParams.add(parameterAssignment.leftVar);
            }
            if (stmt instanceof ReturnStmt) {
                ReturnStmt returnStmt = (ReturnStmt) uw.parsedStmt;
                outputParams.add(returnStmt.var);
            }
        }
        nodeIOVar.setInputParams(inputParams);
        nodeIOVar.setOutputParams(outputParams);
        node.setNodeIOVars(nodeIOVar);
    }

    /**
     * map
     *
     * @param node
     * @param calling
     */
    private static void parseUnitsMapUnitToNode(Node node, List<AbstractNode> calling) {
        List<UnitWrapper> uws = node.getUnitWrapperContainer().getUnitWrappers();
        node.initUnitToNodePair();
        for (int i = node.getUnitWrapperContainer().getStartIndex(); i < uws.size(); i++) {
            UnitWrapper uw = uws.get(i);
            AbstractStmt stmt = uw.parsedStmt;
            if (stmt != null && stmt.hasInvokeExp()) {
                InvokeStmt invokeStmt = (InvokeStmt) stmt;
                Node invokedNode = findNode(calling, invokeStmt.sootMethod);
                if (invokedNode != null) {
                    UnitWrapperToNodePair pair = new UnitWrapperToNodePair(uw, invokedNode);
                    node.addunitToNodePair(pair);
                }
                // The parameter map is in the post-order traversal endVisit
            }
        }
    }

    private static void parseUnitsMapUnitToNode(Node node, Map<UnitWrapper, List<SootMethod>> kvMap) {
        node.initUnitToNodePair();
        node.setChildren(new ArrayList<>());
        for (Map.Entry<UnitWrapper, List<SootMethod>> entry : kvMap.entrySet()) {
            UnitWrapper uw = entry.getKey();
            List<SootMethod> sootMethod = entry.getValue();
            for (SootMethod sub : sootMethod) {
                Node child = Node.createNodeInstance(sub);
                if (child == null) {
                    continue;
                }
                node.getChildren().add(child);
                UnitWrapperToNodePair pair = new UnitWrapperToNodePair(uw, child);
                node.addunitToNodePair(pair);
            }
        }
    }

    /**
     * map unit to Node
     *
     * @param calling
     * @param sootMethod
     * @return
     */
    private static Node findNode(List<AbstractNode> calling, SootMethod sootMethod) {
        if (calling != null && calling.size() != 0) {
            for (AbstractNode n : calling) {
                Node an = (Node) n;
                if (an.getMethod() == sootMethod) {
                    return an;
                }
            }
        }
        return null;
    }

    public static UnitWrapper initUnit(Unit unit, int id) {
        UnitWrapper uw = new UnitWrapper(unit, id);
        return uw;
    }

    /**
     * Set parsed Stmt
     *
     * @param uw
     * @return
     */
    public static UnitWrapper parseUnit(UnitWrapper uw, UnitWrapperContainer unitWrapperContainer) {
        Unit unit = uw.unit;
        if (unit instanceof JIdentityStmt) {
            JIdentityStmt jis = (JIdentityStmt) unit;
            Value lv = jis.getLeftOp();
            if (lv instanceof JimpleLocal) {
                uw.parsedStmt = AbstractStmtFactory.createIdentityStmt(lv);
            }

        } else if (unit instanceof JAssignStmt) {
            JAssignStmt assign = (JAssignStmt) unit;
            ValueBox vb = assign.getLeftOpBox();
            Value leftV = vb.getValue();
            Value rightV = assign.getRightOpBox().getValue();
            if (rightV instanceof JVirtualInvokeExpr || rightV instanceof JNewExpr ||
                    rightV instanceof JInterfaceInvokeExpr || rightV instanceof JStaticInvokeExpr || rightV instanceof JSpecialInvokeExpr) {
//            if (rightV instanceof JVirtualInvokeExpr || rightV instanceof JNewExpr ||
//                    rightV instanceof JInterfaceInvokeExpr) {
                uw.parsedStmt = AbstractStmtFactory.createAssignmentInvokeStmt(leftV, rightV);
                uw.setAssignmentType(CallGraphConstant.VarLinkType.LINK_INVOCATION);
            } else {
                uw.parsedStmt = AbstractStmtFactory.createAssignmentStmt(leftV, rightV);
                if (rightV instanceof JimpleLocal || rightV instanceof Constant || rightV instanceof JInstanceFieldRef || rightV instanceof StaticFieldRef) {
                    uw.setAssignmentType(CallGraphConstant.VarLinkType.LINK_ADD_REF);
                } else {
                    uw.setAssignmentType(CallGraphConstant.VarLinkType.LINK_ARITHMETIC);
                }
            }

        } else if (unit instanceof JInvokeStmt) {
            JInvokeStmt jis = (JInvokeStmt) unit;
            InvokeExpr ie = jis.getInvokeExpr();
            uw.parsedStmt = AbstractStmtFactory.createInvokeStmt(ie);
        } else if (unit instanceof JIfStmt) {
            JIfStmt jis = (JIfStmt) unit;
            UnitBox s = jis.getTargetBox();
            Stmt stmt = jis.getTarget();
            Value v = jis.getCondition();
            uw.parsedStmt = AbstractStmtFactory.createIfStmt(v);
        } else if (unit instanceof JReturnStmt) {
            JReturnStmt jrs = (JReturnStmt) unit;
            Value v = jrs.getOp();
            uw.parsedStmt = AbstractStmtFactory.createReturnStmt(v);
        } else if (unit instanceof JGotoStmt) {
            JGotoStmt jGotoStmt = (JGotoStmt) unit;
            Unit gotoUnit = jGotoStmt.getTarget();
            int id = unitWrapperContainer.getUnitIdByUnit(gotoUnit);
            uw.parsedStmt = AbstractStmtFactory.createGotoStmt(id);
        } else if (unit instanceof JReturnVoidStmt) {
            uw.parsedStmt = AbstractStmtFactory.createReturnStmt(null);
        } else if (unit instanceof JLookupSwitchStmt) {
            JLookupSwitchStmt jLookupSwitchStmt = (JLookupSwitchStmt) unit;
            //todo
            uw.parsedStmt = AbstractStmtFactory.createSwitchStmt(jLookupSwitchStmt);
        } else if (unit instanceof JThrowStmt) {
            JThrowStmt jThrowStmt = (JThrowStmt) unit;
            uw.parsedStmt = AbstractStmtFactory.createThrowStmt(jThrowStmt.getOp());
        } else if (unit instanceof JEnterMonitorStmt) {
            JEnterMonitorStmt jEnterMonitorStmt = (JEnterMonitorStmt) unit;
        } else if (unit instanceof JExitMonitorStmt) {

        } else if (unit instanceof JTableSwitchStmt) {
            uw.parsedStmt = AbstractStmtFactory.createSwitchStmt((JTableSwitchStmt) unit);
        } else {
//            log.warn("new case");
        }
        return uw;
    }


    /**
     * get UnitWrapperContainer
     *
     * @param node
     */

    public static UnitWrapperContainer createUnitWrapperContainer(Node node) {
        List<UnitWrapper> wrappers = node.getUnitWrapperContainer().getUnitWrappers();

        for (UnitWrapper uw : wrappers) {
            parseUnit(uw, node.getUnitWrapperContainer());
        }
        node.getUnitWrapperContainer().setUnitParsed(true);
        return node.getUnitWrapperContainer();
    }


    /**
     *
     */
    public static void parseVarPair(Node node) {
        List<UnitWrapperToNodePair> unitWrapperToNodePairList = node.getUnitToNodePairList();
        for (UnitWrapperToNodePair uwtPair : unitWrapperToNodePairList) {
            Node invokeNode = uwtPair.getToInvokedMethod();
            UnitWrapper uw = uwtPair.getFromInvokeStmt();
            if (invokeNode == null) {
                continue;
            }
            if (invokeNode.getMethod().isPhantom()) {
                continue;
            }
            if (invokeNode.getUnitGraph() == null) {
                continue;
            }
            // The size of the input parameters is not zero
            // from:
            InvokeStmt invokeStmt = (InvokeStmt) uw.parsedStmt;
            List<Var> args = invokeStmt.args;
            Var base = invokeStmt.invokeVar;
            Var ret = null;
            if (invokeStmt instanceof AssignmentInvokeStmt) {
                AssignmentInvokeStmt assignmentInvokeStmt = (AssignmentInvokeStmt) invokeStmt;
                ret = assignmentInvokeStmt.leftVar;
            }
            // ret = base(args)
            // to:
            List<Var> inputParams = invokeNode.getNodeIOVars().getInputParams();
            List<Var> outputParams = invokeNode.getNodeIOVars().getOutputParams();
            int j = 0;
            if (base != null) { // If the static invoke base is null, then args.size equals inputParams.size
                VarPair vp = new VarPair(base, inputParams.get(j));
                uwtPair.getBaseAndArgsPair().add(vp);
                j++;
            }

            for (int i = 0; i < args.size(); i++, j++) {
                try {
                    VarPair tempVp = new VarPair(args.get(i), inputParams.get(j));
                    uwtPair.getBaseAndArgsPair().add(tempVp);
                } catch (Exception e) {
                    System.out.println("aa");
                }
            }
            // There can be multiple outputParams, but only one ret, a one-to-many relationship
            if (ret != null && outputParams.size() != 0) {
                for (int i = 0; i < outputParams.size(); i++) {
                    VarPair tempVp = new VarPair(ret, outputParams.get(i));
                    uwtPair.getReturnValuePair().add(tempVp);
                }
            }
        }

    }

    /**
     * Successor
     *
     * @param node
     */
    public static void parseUnitPredecessorAndSuccessor(Node node) {
//        SootMethod m = node.getMethod();
//        Body b = m.retrieveActiveBody();
//        UnitGraph graph = new ExceptionalUnitGraph(b);
//        SimpleLiveLocals sll = new SimpleLiveLocals(graph);
        UnitWrapperContainer unitWrapperContainer = node.getUnitWrapperContainer();
        List<UnitWrapper> unitWrappers = unitWrapperContainer.getUnitWrappers();
        for (UnitWrapper unitWrapper : unitWrappers) {
//            Unit u = unitWrapper.unit;
//            List before = sll.getLiveLocalsBefore(u);
//            List after = sll.getLiveLocalsAfter(u);
//            Iterator befIt = before.iterator();
//            while (befIt.hasNext()) {
//                Local l = (Local) befIt.next();
//                unitWrapper.before.add(new Var(l.getName(), l.getType()));
//            }
//            Iterator aftIt = after.iterator();
//            while (aftIt.hasNext()) {
//                Local l = (Local) aftIt.next();
//                unitWrapper.after.add(new Var(l.getName(), l.getType()));
//            }
            List<Unit> succsUnit = node.getUnitGraph().getSuccsOf(unitWrapper.unit);
            List<UnitWrapper> unitWrapperList = new ArrayList<>();
            for (Unit unit : succsUnit) {
                UnitWrapper uw = unitWrapperContainer.getUnitWrapperByUnit(unit);
                unitWrapperList.add(uw);
            }
            unitWrapper.successorUnitWrapper.addAll(unitWrapperList);
            for (UnitWrapper uw : unitWrapperList) {
                uw.predecessorUnitWrapper.add(unitWrapper);
            }
        }
    }

    /**
     * test
     *
     * @param sootMethod
     */
    public static void analyzeAssignment(SootMethod sootMethod) {
        Body body = sootMethod.retrieveActiveBody();
        UnitPatchingChain upc = body.getUnits();
        Iterator iter = upc.iterator();
        List<UnitWrapper> results = new ArrayList<>();
        int i = 0;
        while (iter.hasNext()) {
            Unit unit = (Unit) iter.next();
            UnitWrapper js = initUnit(unit, i);
            results.add(js);
            i++;
            // If there is a GlobalSootAssignmentStmt, it indicates modification of global parameters
            // Search in LocalFieldSootAssignmentStmt for variables involved in ParameterSootAssignment
        }

        for (i = 0; i < results.size(); i++) {
            UnitWrapper uw = results.get(i);
            //System.out.println(i+" "+uw.unit.toString());
//            UnitWrapper js = parseUnit(uw);
//            System.out.println(i + " " + js.unit.toString());
//            CallGraphGlobal.logger.info(i + " " + js.unit.toString());
        }
        UnitGraph ug = new BriefUnitGraph(body);
        List<Unit> sucees = ug.getSuccsOf(results.get(5).unit);
//        ForwardFlowAnalysis ffa = new MyForwardFlowAnalysis(ug);
//        Object o = ffa.getFlowAfter(results.get(8).unit);

    }


}
