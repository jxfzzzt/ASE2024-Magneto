package com.magneto;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;

public class LANG_1645_Testcase2 {

    @Test
    public void testCreateNumber() {
        Number ret = NumberUtils.createNumber("+0xFFFFFFFFFFFFFFFF");
        validateReturnValue(ret);
    }

    public void validateReturnValue(Number ret) {
        assertEquals(new BigInteger("+FFFFFFFFFFFFFFFF", 16), ret);
    }
}
