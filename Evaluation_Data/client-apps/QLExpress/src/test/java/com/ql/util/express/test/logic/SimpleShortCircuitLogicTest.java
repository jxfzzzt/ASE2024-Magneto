package com.ql.util.express.test.logic;

import java.util.ArrayList;
import java.util.List;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.junit.Test;

/**
 * 短路逻辑测试类
 *
 * @author tianqiao
 */
public class SimpleShortCircuitLogicTest {
    private final ExpressRunner runner = new ExpressRunner();

    public void initial() {
        runner.getOperatorFactory().getOperator("<").setErrorInfo("$1 < $2 = false");
        runner.getOperatorFactory().getOperator(">").setErrorInfo("$1 > $2 = false");
    }

    public boolean calculateLogicTest(String expression, IExpressContext<String, Object> expressContext,
        List<String> errorInfo) throws Exception {
        return (Boolean)runner.execute(expression, expressContext, errorInfo, true, false);
    }

    /**
     * 测试非短路逻辑,并且输出出错信息
     *
     * @throws Exception
     */
    @Test
    public void testShortCircuit() throws Exception {
        runner.setShortCircuit(true);
        IExpressContext<String, Object> expressContext = new DefaultContext<>();
        expressContext.put("违规天数", 100);
        expressContext.put("虚假交易扣分", 11);
        expressContext.put("VIP", false);
        List<String> errorInfo = new ArrayList<>();
        initial();
        String expression = "( 2 < 1 ) and (违规天数 < 90 or 虚假交易扣分 < 12)";
        boolean result = calculateLogicTest(expression, expressContext, errorInfo);
        if (result) {
            System.out.println(expression + " is true!");
        } else {
            System.out.println(expression + " is false!");
            for (String error : errorInfo) {
                System.out.println(error);
            }
        }
    }

    /**
     * 测试非短路逻辑,并且输出出错信息
     *
     * @throws Exception
     */
    @Test
    public void testNoShortCircuit() throws Exception {
        runner.setShortCircuit(false);
        IExpressContext<String, Object> expressContext = new DefaultContext<>();
        expressContext.put("违规天数", 100);
        expressContext.put("虚假交易扣分", 11);
        expressContext.put("VIP", false);
        List<String> errorInfo = new ArrayList<>();
        initial();
        String expression = "( 2 < 1 ) and (违规天数 < 90 or 虚假交易扣分 < 12)";
        boolean result = calculateLogicTest(expression, expressContext, errorInfo);
        if (result) {
            System.out.println(expression + " is true!");
        } else {
            System.out.println(expression + " is false!");
            for (String error : errorInfo) {
                System.out.println(error);
            }
        }
    }
}
