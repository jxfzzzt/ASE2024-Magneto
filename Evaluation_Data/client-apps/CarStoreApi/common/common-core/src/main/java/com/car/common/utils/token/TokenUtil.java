package com.car.common.utils.token;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author linjiang.xie
 * @date 2020/3/27 15:00
 */
public class TokenUtil {
    private static final String HMAC_KEY = "9527car#!B";
    private static final String ISSUER = "WAIC-USER";

    public TokenUtil() {

    }
    
    public static void main(String[] args) {
		String s="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJXQUlDLVVTRVIgZm9yIG5vLXRlcm1pbmFsIiwiYXVkIjoibm8tdGVybWluYWwiLCJpc3MiOiJXQUlDLVVTRVIiLCJsb2dpblRva2VuIjoie1wiZXhwaXJlVGltZVwiOjIxNjAwLFwiZXh0cmFJbmZvXCI6e30sXCJsb2dpblRlcm1pbmFsXCI6XCJuby10ZXJtaW5hbFwiLFwibG9naW5UaW1lXCI6MTYxNTk2NzU2NjUyMSxcInVzZXJNb2JpbGVcIjpcIjEzNjUxODkwNzg2XCIsXCJ1c2VyTmFtZVwiOlwi5ZOI5ZOIXCIsXCJ1c2VyVHlwZVwiOjIsXCJ1c2VyVXVpZFwiOlwiMWM2ZDZlMGVkMjg1NDYzNDllOGFlNDJhMTk5ZGFiNWJcIixcInV1aWRcIjpcIjFjNmQ2ZTBlZDI4NTQ2MzQ5ZThhZTQyYTE5OWRhYjViXCJ9IiwiZXhwIjoxNjE3MjYzNTY2LCJpYXQiOjE2MTU5Njc1NjZ9.T8QtBv5qnUNlaI0V0dKqVjTBGFfwbNl2VyLE-qiGpvY";
		System.err.println(JSONObject.toJSONString(verifyToken(s)));
	}

    public static LoginToken verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(HMAC_KEY);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        Claim claim = claims.get("loginToken");
        LoginToken loginTokenInfo = JSONObject.parseObject(claim.asString(), LoginToken.class);
        return loginTokenInfo;
    }

    public static String createToken(LoginToken loginToken) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(HMAC_KEY);
        Map<String, Object> map = new HashMap();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        Date nowDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis((long)loginToken.getExpireTime()));
        String token = JWT.create().withHeader(map).withIssuer(ISSUER).withSubject(ISSUER+" for " + loginToken.getLoginTerminal()).withAudience(loginToken.getLoginTerminal()).withIssuedAt(nowDate).withExpiresAt(expireDate).withClaim("loginToken", JSONObject.toJSONString(loginToken)).sign(algorithm);
        return token;
    }
}
