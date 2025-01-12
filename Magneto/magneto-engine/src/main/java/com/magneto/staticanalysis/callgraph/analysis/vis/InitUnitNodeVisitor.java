package com.magneto.staticanalysis.callgraph.analysis.vis;


import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractVisitor;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import com.magneto.staticanalysis.callgraph.analysis.init.UnitParser;

public class InitUnitNodeVisitor extends AbstractVisitor {


    public boolean preVisit(AbstractNode node) {
        return true;
    }

    public boolean postVisit(AbstractNode node) {
        return true;
    }

    public boolean visit(AbstractNode node) {
        // The sequence of the parsing process is partially restricted
        Node n = (Node) node;
//        UnitParser.runParseUnit(n);
        UnitParser.runUnitGraph(n);
        UnitParser.parseNodeIO(n);
        UnitParser.parseUnitPredecessorAndSuccessor(n);
        // If there are no children, mapping to node is not possible
        // UnitParser.runMapUnitToNode(n);
        return node.getChildren() != null && node.getChildren().size() != 0;
    }

    public boolean endVisit(AbstractNode node) {
        if (node.getChildren() != null && node.getChildren().size() != 0) {
            UnitParser.parseVarPair((Node) node);
        }
        return true;
    }

    public void setParentNode(AbstractNode node) {
        if (node.getChildren() != null && node.getChildren().size() != 0) {
            for (AbstractNode n : node.getChildren()) {
                n.getParents().add(node);
            }
        }
    }
}
