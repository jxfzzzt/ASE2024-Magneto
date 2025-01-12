package com.ql.util.express.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import com.ql.util.express.Operator;
import org.junit.Test;

public class DateFormatTest {
    @Test
    public void testDateFormatCompare() throws Exception {
        ExpressRunner runner = new ExpressRunner();

        runner.addFunction("DateFormat", new Operator() {
            private final Map<String, Date> cache = new HashMap<>();

            @Override
            public Object executeInner(Object[] objects) throws Exception {
                String s = objects[0].toString();
                Date date = cache.get(s);
                if (date == null) {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
                    cache.put(s, date);
                }
                return date;
            }
        });

        IExpressContext<String, Object> context = new DefaultContext<>();
        context.put("nowStr", new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-23"));

        String sb = "nowStr.before(new Date(2018,2,22))";
        testTimeCost(runner, sb, context);

        String sb2 = "nowStr > DateFormat('2018-02-22')";
        testTimeCost(runner, sb2, context);

        String sb3
            = "nowStr > DateFormat('2018-02-22') and nowStr < DateFormat('2018-02-24') "
            + "and nowStr == DateFormat('2018-02-23')";
        testTimeCost(runner, sb3, context);
    }

    private static void testTimeCost(ExpressRunner runner, String sb, IExpressContext<String, Object> context)
        throws Exception {
        //预热编译
        Object r = runner.execute(sb, context, null, true, false);
        System.out.println(sb + " 运行结果:" + r);

        long start = System.currentTimeMillis();
        int count = 100000;
        while (count-- > 0) {
            runner.execute(sb, context, null, true, false);
        }
        long cost = System.currentTimeMillis() - start;
        System.out.println(sb + " 运行" + count + "万次，耗时:" + cost + "ms");
    }
}
