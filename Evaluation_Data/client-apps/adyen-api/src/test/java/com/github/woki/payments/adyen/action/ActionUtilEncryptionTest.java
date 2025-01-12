/*
 * Copyright 2015 Willian Oki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.woki.payments.adyen.action;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidKeyException;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public class ActionUtilEncryptionTest {
    private static final String PUBKEY_TEXT = "10001|9B27E6AE115FB582C795C40F19BF77C2DD6875C4E410E39AFD3A861408A3D9A97057AFFC8D7C3FE3B3314ACEC2F8C3036CB6D6212005107529E253218240DC95173E45B9856C6266BBBB05797400674C028E1F86134F6BBE752C47ADD1A35BEAB972E8F2EFBCB9057C70EDE365BF9E8C9B75E58C2ED4AE34DFF2FAFB5DC1886AE1D90B13D48F6182CA0E37881D9277AED7F745D544EBDB066E129D2B74F5B294526679956E9CFA9A83624C67621796F95CB011B133A7D2DC18934D7505A31E5EABB7E05E21FFD33F9885A30BD494ED0F174FA0630BB1A60F270E30B8F8BCC18C0FA085D938AE12D7EC2D64254615F602A07D229517F46DC31CF354B7E6E12783";
    private static final String PUBKEY_TEXT_ERR1 = "foo";
    private static final String PUBKEY_TEXT_ERR2 = "1|2";

    @Test
    public void testEncryption() throws Exception {
        String result = CSEUtil.encrypt(CSEUtil.aesCipher(), CSEUtil.rsaCipher(PUBKEY_TEXT), "4444444444444444");
        System.out.println(result);
        Assert.assertTrue(StringUtils.isNotBlank(result));
    }

    @Test(expected = InvalidKeyException.class)
    public void testEncryptionError1() throws Exception {
        String result = CSEUtil.encrypt(CSEUtil.aesCipher(), CSEUtil.rsaCipher(PUBKEY_TEXT_ERR1), "4444444444444444");
        System.out.println(result);
    }

    @Test(expected = InvalidKeyException.class)
    public void testEncryptionError2() throws Exception {
        String result = CSEUtil.encrypt(CSEUtil.aesCipher(), CSEUtil.rsaCipher(PUBKEY_TEXT_ERR2), "4444444444444444");
        System.out.println(result);
    }
}
