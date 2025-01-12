package com.car.utility.web.common.utils;

import com.car.utility.web.common.utils.weixin.WXPayUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

/**
 * @author linjiang.xie
 * @date 2020/4/11 14:22
 */
public class DecodeUtil {
    private static final String ALGORITHM = "AES";

    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

    private static final String key = "waic202004092335byxielinjiang199";

    private static SecretKeySpec secretKey = new SecretKeySpec(Md5Util.MD5Encode(key, "UTF-8").toLowerCase().getBytes(), ALGORITHM);

    public static String decryptData(String base64Data) {
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64Utils.decode(base64Data)));
        }catch (Exception ex){
            ex.printStackTrace();;
            return null;
        }

    }

    public static void main(String[] args) throws Exception {
        String A = "QCFdvH4mfBqAVMYuV0RwZz8XTYQ7gQw584TJF0k6aoxGvnL0A/gG5Qtnr+WvxQlJ/i6/rv/zX4XKiLoTGbI1buLQ18v1vHHXczvfJEdpgE9IWXFGNQiopAWbwvWtjjTY1P6VoqAd3JP3baKbP8DVHUfRrc6lOOu2dQ1W5k/1fm3ooshHI78nw6iXXYR3r1f47y26C8MMDowK8mW3hwyZIJfJ7deW6Ir9Mzgn9ukYvanttLK46XPsMAYPtYHzrQYFOezINvl+f7DURdYd6NjZrEpTI/KnK1qP45Q6Vhp0NfGZwV6AlyBXOed5wCOyMrZk6SNau7JLPxu3YAtezLkNYPpac04A/wk208N61nRkmARieVInpla7pKK1/5In28JjQElCyZojWmYLILI7SJktMpewMbOFyH6ooEpR/OIJUJPl+qRYZjAML8zFLON3HAv83kkTZaoMovohBYh+7Dlu4dIJ5tvCI7aZ0n1PrjBS/ocuVZmcCIHAx4YsVwkw8H9P5ZxGrfEuj0hPN0Q8qkIzmIMSkHtbSGNoEovPV0IAFSr5olIGrjQSJBHTSh0O1vXhLqsWFi/r1MaU/DvAhv5rJ5qi7UMhpNixMIInQxrfsBjNrTtTAtwII8bshc4qRo5qf5rhwwcV+8oBvYSoFPjfHNkVDJkLocPcQhMFxpJpAPop3ywrLtYv5vxC/tVYEyB/iYVurUTE2LvWPAjg3uI9RPbhukyRl5cbT7PhDEQnTII49xCnaC8d13Eo4vIkM00EmBvWeqnwJTsiFJTZwC1EyvwGlftaIaUIXDQHCZ9uure+r7Eg1GccxK475J4a3AJasI4wuu4SysEkIxhchtJnCBD6eN9RxX6T3336kAhnf0GFnXq2oXkwUxOf+oDbVfmHa40eyNTbwUOF3/8SR5hoMVab+/lJIIU3Uwj6Z4OrlWTdA/vmafvyeouwWaogKnZFS/DPzz2ZH0o6ljMne58bkF7E8ol5nXwoPmj+6985WMXEbqoYpVHDetUoWduZcoggPlFUauHjHVr3LF0hUkiPZVoYr20Z+s8BWvz6/DgddOmsIsiBsCjw0mQdqIi6ijBFs1OwGp4i4xJmLtRw2rZ1ZQ==";
        String B = DecodeUtil.decryptData(A);
        Map<String, String> paraMap = WXPayUtil.xmlToMap(B);
        System.out.println(paraMap.get("out_refund_no"));
        System.out.println(B);
    }
}
