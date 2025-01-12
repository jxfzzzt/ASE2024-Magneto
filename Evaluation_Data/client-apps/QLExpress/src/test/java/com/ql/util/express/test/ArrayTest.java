package com.ql.util.express.test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.junit.Assert;
import org.junit.Test;

public class ArrayTest {

    @Test
    public void testABC() throws Exception {
        String express = ""
            + "int[][] abc = new int[2][2];"
            + "int[] b = new int[2];"
            + "abc[0] = b;"
            + "b[0] =11;"
            + "abc[0][1] = 22;"
            + "return abc;";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        int[][] r = (int[][])runner.execute(express, context, null, false, true);
        System.out.println(r[0][1]);
        Assert.assertEquals("数组操作实现错误", 22, r[0][1]);
    }

    @Test
    public void testAnonyNewArrayOrMapOrList() throws Exception {
        ExpressRunner runner = new ExpressRunner(false, false);
        String[][] expressTest = new String[][] {
            {"int[] abc = [1,2,3];return abc[2]", "3"},
            {"int[][] abc = [[11,12,13],[21,22,23]];return abc[1][2]", "23"},
            {"String[] abc = [\"xuannan\",\"qianghui\"];return abc[1]", "qianghui"},
            {"String[] abc = [\"xuannan\"+100,\"qianghui\"+100];return abc[1]", "qianghui100"},
            {"Object[] abc = [];return abc.length", "0"},
            {"Map abc = NewMap(1:1,2:2); return abc.get(1) + abc.get(2)", "3"},
            {"Map abc = NewMap(\"a\":1,\"b\":2); return abc.a + abc.b", "3"},
            {"int o1 =10; int o2=20;String k1 =\"a\";String k2 =\"b\"; Map abc = NewMap(k1:o1,k2:o2); return abc.a +"
                + " abc.b", "30"},
            {"Map abc = NewMap(1:\"xuannan\",2:\"qianghui\"); return abc.get(1) +\"-\"+ abc.get(2)",
                "xuannan-qianghui"},
            {"List abc = NewList(1,2,3); return abc.get(1)", "2"},
        };
        IExpressContext<String, Object> expressContext = new ExpressContextExample(null);

        for (int point = 0; point < expressTest.length; point++) {
            String expressStr = expressTest[point][0];
            Object result = runner.execute(expressStr, expressContext, null, false, false);
            if (result == null && !expressTest[point][1].equalsIgnoreCase("null")
                || expressTest[point][1].equalsIgnoreCase("null") && result != null
                || result != null && !expressTest[point][1].equalsIgnoreCase(result.toString())) {
                throw new Exception(
                    "处理错误,计算结果与预期的不匹配:" + expressStr + " = " + result + "但是期望值是：" + expressTest[point][1]);
            }
            System.out.println("Example " + point + " : " + expressStr + " = " + result);
        }
        System.out.println(expressContext);
    }

    @Test
    public void testArrayField() throws Exception {
        ExpressRunner runner = new ExpressRunner(false, true);
        String[] expressList = new String[] {
            "(args[0]).test.code",
            "System.out.println(args[0].code)"
        };

        for (String express : expressList) {
            DefaultContext<String, Object> context = new DefaultContext<>();
            Args[] args = new Args[2];
            args[0] = new Args();
            args[0].setCode("parent");
            Args child = new Args();
            child.setCode("child");
            args[0].setTest(child);
            context.put("args", args);
            runner.execute(express, context, null, true, false);
        }
    }

    @Test
    public void testFunction2() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        String exp = "this.println((args[0]));";
        String[] args = {"123", "456"};
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("args", args);
        context.put("this", new ArrayTest());
        Object result = runner.execute(exp, context, null, false, false);
        System.out.println("result = " + result);
    }

    public void println(String x) {
        System.out.println("println(String x)");
    }

    public void println(Object x) {
        System.out.println("println(Object x)");
    }

    public class Args {
        private Args test;
        private String code;

        public Args getTest() {
            return test;
        }

        public void setTest(Args test) {
            this.test = test;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
