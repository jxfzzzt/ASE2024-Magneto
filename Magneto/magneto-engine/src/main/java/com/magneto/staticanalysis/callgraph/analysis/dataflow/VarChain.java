package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import com.magneto.staticanalysis.callgraph.analysis.bean.NodeIOVar;
import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import com.magneto.staticanalysis.callgraph.global.CallGraphConstant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VarChain {

    public Var newAddedVar;
    /**
     * start point
     */
    List<Var> anchors;
    List<Var> retVars;

    public VarChain() {
        this.anchors = new ArrayList<>();
        this.retVars = new ArrayList<>();
    }

    public void addAnchor(Var v) {
        this.anchors.add(v);
    }

    public void addRetVar(Var v) {
        this.retVars.add(v);
    }


//    public void addVarDeclaration(Var v){
//        this.anchors.add(v);
//        this.newAddedVar = v;
//    }

    public void addVars(List<Var> vars) {
        if (vars == null) {
            return;
        }
        for (Var v : vars) {
            addAnchor(v);
        }
    }

//
//    public void addChangedVarSpot(Var v){
//        this.changeVarSpot.add(v);
//    }

    public List<Var> traceChangedVarBackwardsToFindChangedParams(List<Var> doChangeVars) {
        return null;
    }

    public void addVarLink(Var from, Var to, int linkType) {
        if (from != null) {
            from.addNext(to, linkType);
//            to.setPrev(from, linkType);
        } else {
            anchors.add(to);
        }

        newAddedVar = to;
    }

    /**
     * Same var name
     *
     * @param v
     * @return
     */
    public boolean hasVar(Var v, int forwardOrBackward) {
        LinkedList queue = new LinkedList();
        for (Var param : anchors) {
            queue.offer(param); //2603  2624
        }


        while (!queue.isEmpty()) {
            Var qv = (Var) queue.pop();
            if (qv.varName.equals(v.varName)) {
                return true;
            }
            if (forwardOrBackward == CallGraphConstant.FORWARD) {
                if (qv.getNext() != null) {
                    for (VarLink next : qv.getNext()) {
                        queue.offer(next.var);
                    }
                }
            } else if (forwardOrBackward == CallGraphConstant.BACKWARD) {
                if (qv.getPrev() != null) {
                    for (VarLink prev : qv.getPrev()) {
                        queue.offer(prev.var);
                    }
                }
            }
        }
        return false;
    }

    public boolean hasVar(List<Var> vars, int forwardOrBackward) {
        for (Var v : vars) {
            if (hasVar(v, forwardOrBackward)) {
                return true;
            }
        }
        return false;
    }


    public Var getSameVarName(Var v, int forwardOrBackward) {
        LinkedList queue = new LinkedList();
        for (Var param : anchors) {
            queue.offer(param);
        }
        while (!queue.isEmpty()) {
            Var qv = (Var) queue.pop();
            if (qv.varName.equals(v.varName)) {
                return qv;
            }
            if (forwardOrBackward == CallGraphConstant.FORWARD) {
                if (qv.getNext() != null) {
                    for (VarLink next : qv.getNext()) {
                        queue.offer(next.var);
                    }
                }
            } else if (forwardOrBackward == CallGraphConstant.BACKWARD) {
                if (qv.getPrev() != null) {
                    for (VarLink prev : qv.getPrev()) {
                        queue.offer(prev.var);
                    }
                }
            }
        }
        return null;
    }

    public List<Var> getRelatedVar(Var v) {
        LinkedList queue = new LinkedList();
        List<Var> relatedVar = new ArrayList<>();
        for (Var param : anchors) {
            queue.offer(param);
        }
        while (!queue.isEmpty()) {
            Var qv = (Var) queue.pop();
            if (qv == v) {
                relatedVar.add(qv);
            }
            if (qv.getNext() != null) {
//                for(Var next:qv.getNext()){
//                    queue.offer(next);
//                }
            }
        }
        return relatedVar;

    }

    public void applyVarLink(Var left, List<Var> right, int linkType, int backwardOrForward) {

        if (backwardOrForward == CallGraphConstant.FORWARD) {
            for (Var v : right) {
                v.addNext(left, linkType);
            }
        } else if (backwardOrForward == CallGraphConstant.BACKWARD) {
            Var v = this.getSameVarName(left, CallGraphConstant.BACKWARD);
            v.addPrev(right.get(0), linkType);
//            for (Var v : right) {
//                v.addPrev(left, linkType);
//            }
        }
    }

    public boolean haveParams(List<Var> params, Node node) {
        boolean flag = false;
        for (Var v : anchors) {
            LinkedList<Var> q = new LinkedList();
            q.offer(v);
            while (q.size() != 0) {
                Var item = q.pop();

                for (int i = 0; i < params.size(); i++) {
                    Var param = params.get(i);
                    if (param.varName.equals(item.varName)) {
                        NodeIOVar nodeIOVar = node.getNodeIOVars();
                        List<Var> inputVars = nodeIOVar.getInputParams();
                        inputVars.get(i).isChange = true;
                        flag = true;
                    }
                }
                if (item.getPrev() != null && item.getPrev().size() != 0) {
                    for (VarLink varLink : item.getPrev()) {
                        q.offer(varLink.var);
                    }
                }
            }
        }

        return flag;
    }

    public boolean haveRetVars(Node node) {
        boolean flag = false;
        for (Var v : anchors) {
            LinkedList<Var> q = new LinkedList();
            q.offer(v);
            while (q.size() != 0) {
                Var item = q.pop();
                System.out.println(item.varName);
                for (Var ret : this.retVars) {
                    if (ret.varName.equals(item.varName)) {
                        node.getNodeIOVars().getOutputParams().get(0).isChange = true;
                        flag = true;
                        break;
                    }
                }
                if (item.getNext() != null && item.getNext().size() != 0) {
                    for (VarLink varLink : item.getNext()) {
                        q.offer(varLink.var);
                    }
                }
            }
        }
        return flag;
    }
}
