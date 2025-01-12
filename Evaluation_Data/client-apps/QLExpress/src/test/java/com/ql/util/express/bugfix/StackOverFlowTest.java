package com.ql.util.express.bugfix;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.junit.Test;

public class StackOverFlowTest {
    @Test
    public void test() throws Exception {
        String[] expressList = new String[] {
            "1",
            "1+2",
            "max(1,2)",
            "max(1,max(2,3))",
            "max(1,max(2,max(3,4)))",
            "max(1,max(2,max(3,max(4,5))))",
            "max(1,max(2,max(3,max(4,max(5,6)))))",
            "max(1,max(2,max(3,max(4,max(5,max(6,7))))))",
        };

        for (String express : expressList) {
            ExpressRunner runner = new ExpressRunner();
            IExpressContext<String, Object> context = new DefaultContext<>();
            Object result = runner.execute(express, context, null, true, false);
            System.out.println(express + " = " + result);

            System.out.println("优化栈深度之后:");
            ExpressRunner runner2 = new ExpressRunner();
            Object result2 = runner2.execute(express, context, null, true, false);
            System.out.println(express + " = " + result2);
        }
    }
}
