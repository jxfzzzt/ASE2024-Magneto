package com.magneto;

import net.lingala.zip4j.ZipFile;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Zip4j_263_Testcase1 {

    @Test(timeout = 60000)
    public void testZipFileConstructorThrowsIllegalArgumentExceptionWhenFileParameterIsNull() {
        try {
            ZipFile zipFile = new ZipFile((File) null);
            fail("fail the test");
        } catch (IllegalArgumentException e) {
            validateThrow(e);
        }
    }

    public void validateThrow(Throwable e) {
        assertTrue(e instanceof IllegalArgumentException);
    }
}
