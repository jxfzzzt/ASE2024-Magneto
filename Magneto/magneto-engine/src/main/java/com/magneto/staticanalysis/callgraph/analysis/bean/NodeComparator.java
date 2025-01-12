package com.magneto.staticanalysis.callgraph.analysis.bean;

import soot.Body;
import soot.SootMethod;

import java.io.Serializable;
import java.util.*;

public class NodeComparator implements Serializable {


    public boolean isNodeSame(AbstractNode a, AbstractNode b) {
        Node aa = (Node) a;
        Node bb = (Node) b;
        SootMethod sootMethod = aa.getMethod();
        SootMethod sootMethod2 = bb.getMethod();
        Body body1 = sootMethod.retrieveActiveBody();
        Body body2 = sootMethod2.retrieveActiveBody();
        if (body1 != null && body2 != null) {
            List<UnitWrapper> aaWrappers = aa.getUnitWrapperContainer().getUnitWrappers();
            List<UnitWrapper> bbWrappers = bb.getUnitWrapperContainer().getUnitWrappers();
            if (aaWrappers.size() != bbWrappers.size()) {
                return false;
            }
            for (int i = 0; i < aaWrappers.size(); i++) {
                UnitWrapper uw1 = aaWrappers.get(i);
                UnitWrapper uw2 = bbWrappers.get(i);
                if (!uw1.unit.toString().equals(uw2.unit.toString())) {
                    return false;
                }
            }
            return true;

        } else {
            // No `body`, no comparison
            return aa.getMethodSignatureFull().equals(bb.getMethodSignatureFull());
        }

    }

    public List<Node> sortNode(List nodes) {
        Map<String, Node> temp = new HashMap<>();
        for (Object o : nodes) {
            Node nn = (Node) o;
            temp.put(nn.getMethodSignatureFull(), nn);
        }
        List<Map.Entry<String, Node>> entries = new ArrayList<>(temp.entrySet());
        entries.sort(new Comparator<Map.Entry<String, Node>>() {
            @Override
            public int compare(Map.Entry<String, Node> o1, Map.Entry<String, Node> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        List result = new ArrayList();
        for (Map.Entry<String, Node> item : entries) {
            result.add(item.getValue());
        }
        return result;
    }
}
