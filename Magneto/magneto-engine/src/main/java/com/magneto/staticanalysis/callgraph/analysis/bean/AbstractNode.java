package com.magneto.staticanalysis.callgraph.analysis.bean;


import com.magneto.staticanalysis.callgraph.global.CallGraphGlobal;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class AbstractNode implements Serializable {

    public boolean isVisited;
    /**
     * Used to solve the cycle problem:
     * -1: Not visited
     * 1: The node's child node visits it, indicating a cycle
     * 2: Visited completely, no cycle
     */
    public int condition;
    /**
     * Basic information
     */
    private final int id;
    /**
     * Graph relationships: parent node and child node
     */
    private List<AbstractNode> children;
    private transient List<AbstractNode> parents;
    /**
     * Node depth
     */
    private int depth;

    public AbstractNode() {
        this.id = CallGraphGlobal.nodeCounter;
        CallGraphGlobal.nodeCounter++;
        this.isVisited = false;
        this.condition = -1;
    }

    public List<AbstractNode> getChildren() {
        return children;
    }

    public void setChildren(List<AbstractNode> children) {
        this.children = children;
    }

    public List<AbstractNode> getParents() {
        return parents;
    }

    public void setParents(List<AbstractNode> parents) {
        this.parents = parents;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void removeChild(AbstractNode child) {
        this.children.remove(child);
    }


    /**
     * Visitor
     *
     * @param visitor
     */
    public void accept(AbstractVisitor visitor) {
        if (visitor.preVisit(this)) {
            boolean visitChildren = visitor.visit(this);
            if (visitChildren) {
                acceptChild(visitor, this.getChildren());

            }
            visitor.endVisit(this);
        }
        visitor.postVisit(this);
    }

    protected void acceptChild(AbstractVisitor visitor, List nodes) {
        if (nodes == null) {
            return;
        }
        for (Object n : nodes) {
            AbstractNode abstractNode = (AbstractNode) n;
            abstractNode.accept(visitor);
        }
    }

    public int getId() {
        return id;
    }

    /**
     * BFS only calls the visit method of the Visitor.
     *
     * @param visitor
     */
    public void acceptBFS(AbstractVisitor visitor) {
        Queue<AbstractNode> queue = new LinkedList<>();
        queue.offer(this);
        int size = 1;
        int newSize = 0;
        int depth = 0;
        while (queue.size() != 0) {
            AbstractNode node = queue.poll();
            size--;
            node.setDepth(depth);
            visitor.visit(node);
            node.isVisited = true;
            List children = node.getChildren();
            if (children != null && children.size() != 0) {
                for (Object o : children) {
                    AbstractNode can = (AbstractNode) o;
                    if (can.isVisited) {
                        continue;
                    }
                    queue.offer(can);
                }
                newSize += children.size();
            }
            if (size == 0) {
                size = newSize;
                newSize = 0;
                depth += 1;
            }
        }
    }


    public boolean acceptBFSCompare(NodeComparator comparator, Node compareNode) {
        Queue<AbstractNode> queue = new LinkedList<>();
        Queue<AbstractNode> queue2 = new LinkedList<>();
        queue.offer(this);
        queue2.offer(compareNode);
        int size = 1;
        int newSize = 0;
        int depth = 0;
        while (queue.size() != 0) {
            AbstractNode node = queue.poll();
            AbstractNode node2 = queue2.poll();

            size--;
            node.setDepth(depth);
            node2.setDepth(depth);
            // node isEqualToCompareNode
            boolean isSame = comparator.isNodeSame(node, node2);
            if (!isSame) {
                return false;
            }

            node.isVisited = true;
            node2.isVisited = true;
            List children = node.getChildren();
            List children2 = node2.getChildren();
            if (children.size() != children2.size()) {
                return false;
            }
            if (children != null && children.size() != 0) {
                List alphabeticNodeList = comparator.sortNode(children);
                List alphabeticNodeList2 = comparator.sortNode(children2);
                for (Object o : alphabeticNodeList) {
                    AbstractNode can = (AbstractNode) o;
                    if (can.isVisited) {
                        continue;
                    }
                    queue.offer(can);
                }
                for (Object o : alphabeticNodeList2) {
                    AbstractNode can = (AbstractNode) o;
                    if (can.isVisited) {
                        continue;
                    }
                    queue2.offer(can);
                }

                newSize += children.size();
            }
            if (size == 0) {
                size = newSize;
                newSize = 0;
                depth += 1;
            }
        }
        return true;
    }


}
