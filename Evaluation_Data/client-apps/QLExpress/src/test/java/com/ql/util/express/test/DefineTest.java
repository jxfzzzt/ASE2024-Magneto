package com.ql.util.express.test;

import java.util.HashMap;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.junit.Assert;
import org.junit.Test;

public class DefineTest {
    @Test
    public void testDefExpressInner() throws Exception {
        String express = "int qh = 1";
        DefaultContext<String, Object> context = new DefaultContext<>();
        ExpressRunner runner = new ExpressRunner(false, true);
        context.put("qh", 100);
        Object r = runner.execute(express, context, null, false, false);
        Assert.assertTrue("表达式变量作用域错误", r.toString().equalsIgnoreCase("1"));
        Assert.assertTrue("表达式变量作用域错误", context.get("qh").toString().equalsIgnoreCase("100"));
    }

    @Test
    public void testDefUserContext() throws Exception {
        String express = "qh = 1 + 1";
        DefaultContext<String, Object> context = new DefaultContext<>();
        ExpressRunner runner = new ExpressRunner();
        context.put("qh", 100);
        Object r = runner.execute(express, context, null, false, false);
        Assert.assertTrue("表达式变量作用域错误", r.toString().equalsIgnoreCase("2"));
        Assert.assertTrue("表达式变量作用域错误", context.get("qh").toString().equalsIgnoreCase("2"));
    }

    @Test
    public void testAlias() throws Exception {
        String express = ""
            + "定义别名 qh example.child;"
            + "{"
            + "    定义别名 qh example.child.a;"
            + "    qh = qh + \"-ssss\";"
            + "};"
            + "qh.a = qh.a + \"-qh\";"
            + "return example.child.a";
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("定义别名", "alias", null);
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("example", new BeanExample());
        runner.addOperatorWithAlias("如果", "if", null);
        runner.addOperatorWithAlias("则", "then", null);
        runner.addOperatorWithAlias("否则", "else", null);
        Object r = runner.execute(express, context, null, false, false);
        Assert.assertTrue("别名实现 错误", r.toString().equalsIgnoreCase("qh-ssss-qh"));
        Assert.assertTrue("别名实现 错误", ((BeanExample)context.get("example")).child.a
            .equalsIgnoreCase("qh-ssss-qh"));
    }

    @Test
    public void testMacro() throws Exception {
        String express = "定义宏 惩罚 {bean.unionName(name)}; 惩罚; return 惩罚;";
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("定义宏", "macro", null);
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("bean", new BeanExample("qhlhl2010@gmail.com"));
        context.put("name", "xuannan");
        Object r = runner.execute(express, context, null, false, false);
        Assert.assertTrue("别名宏 错误", r.toString().equalsIgnoreCase("qhlhl2010@gmail.com-xuannan"));
        System.out.println(r);
    }

    @Test
    public void test_self_define_function() throws Exception {
        String express = ""
            + "定义函数 递归(int a) {"
            + "    if(a == 1) then {"
            + "        return 1;"
            + "    } else {"
            + "        return 递归(a - 1) * a;"
            + "    }"
            + "};"
            + "递归(10);";
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("定义函数", "function", null);
        DefaultContext<String, Object> context = new DefaultContext<>();
        Object r = runner.execute(express, context, null, true, false);
        Assert.assertEquals("自定义函数 错误", "3628800", r.toString());
    }

    @Test
    public void testProperty() throws Exception {
        String express = ""
            + "example.child.a = \"ssssssss\";"
            + "map.name =\"ffff\";"
            + "return map.name;";
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("example", new BeanExample("张三"));
        context.put("map", new HashMap<String, Object>());
        runner.addFunctionOfClassMethod("isVIP", BeanExample.class.getName(), "isVIP", new String[] {"String"},
            "$1不是VIP用户");
        Object r = runner.execute(express, context, null, false, false);
        Assert.assertTrue("属性操作错误", r.toString().equalsIgnoreCase("ffff"));
        Assert.assertTrue("属性操作错误", ((BeanExample)context.get("example")).child.a
            .equalsIgnoreCase("ssssssss"));
    }

    @Test
    public void test_loop() throws Exception {
        String express = ""
            + "qh = 0;"
            + "循环(int i = 1; i <= 10; i = i + 1) {"
            + "    if(i > 5) then { "
            + "        终止;"
            + "    }; "
            + "    循环(int j = 0; j < 10; j = j + 1) {"
            + "        if(j > 5) then {"
            + "            终止;"
            + "        }; "
            + "        qh = qh + j;"
            + "    };"
            + "};"
            + "return qh;";
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("循环", "for", null);
        runner.addOperatorWithAlias("继续", "continue", null);
        runner.addOperatorWithAlias("终止", "break", null);
        runner.addFunctionOfServiceMethod("打印", System.out, "println", new String[] {Object.class.getName()}, null);
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("bean", new BeanExample("qhlhl2010@gmail.com"));
        context.put("name", "xuannan");
        int count = 1;
        long s = System.currentTimeMillis();

        Object r = runner.execute(express, context, null, false, false);
        System.out.println("r = " + r);
        System.out.println("编译耗时：" + (System.currentTimeMillis() - s));

        for (int i = 0; i < count; i++) {
            r = runner.execute(express, context, null, false, false);
            Assert.assertEquals("循环处理错误", "75", r.toString());
        }
        System.out.println("执行耗时：" + (System.currentTimeMillis() - s));
        System.out.println(context);
    }
}
