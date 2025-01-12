package com.magneto.staticanalysis.callgraph.analysis.dataflow;


import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import lombok.extern.slf4j.Slf4j;
import soot.Value;
import soot.jimple.internal.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class IfStmt extends AbstractStmt {


    public Var opt1;
    public Var opt2;
    public String optType;
    public IfStmt(Value v) {
        if (v instanceof JNeExpr) {
            JNeExpr jNeExpr = (JNeExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JEqExpr) {
            JEqExpr jNeExpr = (JEqExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JOrExpr) {
            JOrExpr jNeExpr = (JOrExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JAndExpr) {
            JAndExpr jNeExpr = (JAndExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JXorExpr) {
            JXorExpr jNeExpr = (JXorExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JLeExpr) {
            JLeExpr jNeExpr = (JLeExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JLtExpr) {
            JLtExpr jNeExpr = (JLtExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JGtExpr) {
            JGtExpr jNeExpr = (JGtExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else if (v instanceof JGeExpr) {
            JGeExpr jNeExpr = (JGeExpr) v;
            Value v1 = jNeExpr.getOp1();
            Value v2 = jNeExpr.getOp2();
            Var var1 = new Var(v1);
            Var var2 = new Var(v2);
            this.opt1 = var1;
            this.opt2 = var2;

        } else {
//            log.warn("new case");
        }
        this.optType = v.getClass().getSimpleName();

    }

    @Override
    public List<Var> getUsedVars() {
        List<Var> result = new ArrayList<>();
        if (!opt1.isConstant) {
            result.add(opt1);
        }
        if (!opt2.isConstant) {
            result.add(opt2);
        }
        return result;
    }

    /**
     * not assignment, no right vars
     *
     * @return
     */
    @Override
    public List<Var> getRightVars() {
        return null;
    }


}
