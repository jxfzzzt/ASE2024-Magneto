package com.ql.util.express.test;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.junit.Assert;
import org.junit.Test;

public class PreloadExpressTest {
    @Test
    public void preloadExpress() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        String express = ""
            + "function add(int a, int b) {"
            + "    return a + b;"
            + "}"
            + "function sub(int a, int b) {"
            + "    return a - b;"
            + "}";
        runner.loadMultiExpress(null, express);
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("m", 1);
        context.put("n", 1);
        Object object = runner.execute("add(m, n) + sub(2, -2)", context, null, true, false);
        System.out.println(object);
        Assert.assertEquals(6, (int)(Integer)object);
    }
}
