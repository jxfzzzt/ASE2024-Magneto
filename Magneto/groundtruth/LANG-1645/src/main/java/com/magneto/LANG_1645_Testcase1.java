package com.magneto;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;

public class LANG_1645_Testcase1 {

    @Test
    public void testCreateBigInteger() {
        BigInteger ret = NumberUtils.createBigInteger("+#FFFFFFFFFFFFFFFF");
        validateReturnValue(ret);
    }

    public void validateReturnValue(BigInteger ret) {
        assertEquals(new BigInteger("+FFFFFFFFFFFFFFFF", 16), ret);
    }
}
