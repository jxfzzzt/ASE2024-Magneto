package com.magneto.staticanalysis.callgraph.analysis.dataflow;


import com.magneto.staticanalysis.callgraph.analysis.bean.Var;

import java.io.Serializable;
import java.util.List;

/**
 * AbstractStmt->AssignmentStmt
 * ->InvokeStmt->AssignmentInvokeStmt
 * <p>
 * core statements:
 * 1. NopStmt,
 * #2. IdentityStmt,
 * #3. AssignStmt
 * intraprocedural control-flow:
 * #4. IfStmt,
 * #5. GotoStmt,
 * 6. TableSwitchStmt,
 * <p>
 * interprocedural control-flow:
 * #7. InvokeStmt,
 * #8. ReturnStmt,
 * #9. ReturnVoidStmt;
 * monitor statement:
 * 10. EnterMonitorStmt,
 * 11. ExitMonitorStmt
 * 12. ThrowStmt,
 * 12. RetStmt
 */
public abstract class AbstractStmt implements Serializable {


    public boolean isAssignment() {
        return false;
    }

    public Var getLeftValue() {
        return null;
    }

    public boolean hasInvokeExp() {
        return false;
    }

    public abstract List<Var> getUsedVars();

    public abstract List<Var> getRightVars();

}
