package net.sourceforge.ondex.workflow2.gui.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

/**
 * Implements a tree that supports hiding and showing of branches given a path. Only works with DocumentedTreeNode
 * as path is specified as a sequence of node names (excluding the root). This class is functional to fulfil the
 * immediate requirements, but makes some assumptions about the classes of the tree nodes, so should be treated as
 * prototype and not used elsewhere until it is finalised.
 *
 * @author lysenkoa
 */
public class HidableBranchJTree extends JTree {
    private static final long serialVersionUID = 1L;
    private Map<MutableTreeNode, MutableTreeNode> hiddenNodes = new HashMap<MutableTreeNode, MutableTreeNode>();

    public HidableBranchJTree(DefaultTreeModel model) {
        super(model);
    }

    @SuppressWarnings("unchecked")
    public void hide(String... path) {
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        MutableTreeNode root = (MutableTreeNode) model.getRoot();
        List<MutableTreeNode> currentLevel = new ArrayList<MutableTreeNode>();
        currentLevel.add(root);
        for (String element : path) {
            for (MutableTreeNode n : currentLevel) {
                List<MutableTreeNode> nextLevel = new ArrayList<MutableTreeNode>();
                if (element.equals("?")) {
                    Enumeration<MutableTreeNode> en = (Enumeration<MutableTreeNode> ) n.children();
                    while (en.hasMoreElements()) {
                        nextLevel.add(en.nextElement());
                    }
                } else {
                    Enumeration<MutableTreeNode> en = (Enumeration<MutableTreeNode> ) n.children();
                    while (en.hasMoreElements()) {
                        Object o = en.nextElement();
                        if (o instanceof DocumentedTreeNode) {
                            DocumentedTreeNode node = (DocumentedTreeNode) o;
                            if (node.getName().equals(element)) {
                                nextLevel.add(node);
                            }
                        }
                    }
                }
                currentLevel = nextLevel;
            }
        }
        for (MutableTreeNode node : currentLevel) {
            hiddenNodes.put(node, (MutableTreeNode) node.getParent());
            model.removeNodeFromParent(node);
        }
        this.validate();
        this.updateUI();
    }

    public void show(String... path) {
        //TODO
    }

    public void showAll() {
        for (Entry<MutableTreeNode, MutableTreeNode> nodes : hiddenNodes.entrySet()) {
            nodes.getValue().insert(nodes.getKey(), nodes.getValue().getChildCount());
        }
        this.validate();
        this.updateUI();
    }
}
