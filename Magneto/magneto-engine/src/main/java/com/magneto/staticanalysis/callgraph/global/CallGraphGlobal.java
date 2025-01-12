package com.magneto.staticanalysis.callgraph.global;

import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import lombok.extern.slf4j.Slf4j;
import soot.SootMethod;

import java.util.*;

@Slf4j
public class CallGraphGlobal {

    public static int nodeCounter = 0;

    public static List<SootMethod> sootMethodAll = new ArrayList<>();

    public static Map<SootMethod, Node> nodeAll = new HashMap<>();

    public static Set<Node> nodeSet = new HashSet<>();

}
