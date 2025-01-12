package com.magneto;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;

public class IO_611_Testcase1 {

    @Test(timeout = 60000)
    public void testFileName() {
        String normalize = FilenameUtils.normalize("//foo//./bar");
        validateReturnValue(normalize);
    }

    public void validateReturnValue(String result) {
        Assert.assertEquals("//foo/bar", result);
    }
}
