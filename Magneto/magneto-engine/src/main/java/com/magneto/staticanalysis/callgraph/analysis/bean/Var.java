package com.magneto.staticanalysis.callgraph.analysis.bean;

import com.magneto.staticanalysis.callgraph.analysis.dataflow.VarLink;
import lombok.extern.slf4j.Slf4j;
import soot.Type;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Var implements Serializable {

    public static final int LINK_TYPE_0 = 0;

    /**
     * initial soot object
     */
    public Value value;
    /**
     * varName      Type            fieldName    fieldType
     * CallGraphGlobal.var       N/A          org.edu.CallGraphGlobal  var          org.XX.Var
     * ins.var          ins          org.edu.CallGraphGlobal  var          org.XX.Var
     */
    public String varName;
    public Type type;

    public boolean isConstant;

    public boolean isChange;


    public boolean isField;
    public String fieldName;
    public Type fieldType;

    public boolean isArray;
    public String arrayIndexName;
    private List<VarLink> next;

    /**
     * * * * * * * * * * * * * * * * * * * * *
     *
     * The following section is related to the DataFlow chain
     *
     * * * * * * * * * * * * * * * * * * * * *
     */
    private List<VarLink> prev;
    private int linkType;
    public Var(Value v) {
        this.value = v;
        if (v instanceof JimpleLocal) {
            JimpleLocal jl = (JimpleLocal) v;
            setVarName(jl.getName());
            type = jl.getType();
        } else if (v instanceof Constant) {
            setVarName(v.toString());
            type = v.getType();
            isConstant = true;
        } else if (v instanceof JNewExpr) {
            JNewExpr jne = (JNewExpr) v;
            setVarName("#new#");
            type = jne.getBaseType();
        } else if (v instanceof StaticFieldRef) {
            StaticFieldRef sfr = (StaticFieldRef) v;
            type = sfr.getFieldRef().declaringClass().getType();
            setVarName("#static_field#");
            fieldName = sfr.getFieldRef().name();
            fieldType = sfr.getFieldRef().type();

        } else if (v instanceof JInstanceFieldRef) {
            JInstanceFieldRef instanceFieldRef = (JInstanceFieldRef) v;
            fieldName = instanceFieldRef.getFieldRef().name();
            fieldType = instanceFieldRef.getType();
            Value base = instanceFieldRef.getBase();
            if (base instanceof JimpleLocal) {
                JimpleLocal jl = (JimpleLocal) base;
//                varName = jl.getName();
                setVarName(jl.getName());
                type = jl.getType();
            }
        } else if (v instanceof JGotoStmt) {
            setVarName("#goto#");
        } else if (v instanceof JArrayRef) {
            this.isArray = true;
            JArrayRef jArrayRef = (JArrayRef) v;
            Value v2 = jArrayRef.getBase();
            Value v3 = jArrayRef.getIndex();
            if (v2 instanceof JimpleLocal && v3 instanceof JimpleLocal) {
                JimpleLocal jl = (JimpleLocal) v2;
                JimpleLocal jl2 = (JimpleLocal) v3;
                setVarName(jl.getName());
                type = jl.getType();
                this.arrayIndexName = jl2.getName();
            }

        } else {
//            log.warn("new case");
        }
        this.isChange = false;
    }

    /**
     * @return
     */
    public int getLinkType() {
        return linkType;
    }

    public void addNext(Var var, int linkType) {
        if (this.next == null) {
            this.next = new ArrayList<>();
        }
        this.next.add(new VarLink(var, linkType));
//        this.linkType = linkType;
    }

    public void addPrev(Var var, int linkType) {
        if (this.prev == null) {
            this.prev = new ArrayList<>();
        }
        this.prev.add(new VarLink(var, linkType));
//        this.linkType = linkType;
    }

    public List<VarLink> getNext() {
        return next;
    }

    public List<VarLink> getPrev() {
        return prev;
    }


    public boolean isEqualTo(Var v) {
        return this.varName.equals(v.varName) && this.type.toString().equals(v.type.toString());
    }

    @Override
    public String toString() {
        return varName;
    }

    private void setVarName(String name) {
        if (name.startsWith("$")) {
            name = name.substring(1);
        }
        this.varName = name;
    }
}
