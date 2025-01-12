package com.magneto;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LANG_1385_Testcase1 {

    @Test
    public void testCreateNumber() {
        try {
            NumberUtils.createNumber("L");
            fail("fail the test");
        } catch (Exception e) {
            validateThrow(e);
        }
    }

    public void validateThrow(Throwable e) {
        assertTrue(e instanceof NumberFormatException);
    }
}
