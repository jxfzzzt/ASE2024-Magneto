package com.car.gateway.common.fallback;

import com.alibaba.fastjson.JSONArray;
import com.car.common.enums.ResEnum;
import com.car.common.res.ResultRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author xlj
 */
@Component
@Slf4j
public class ServiceFallbackProvider implements FallbackProvider {
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        cause.printStackTrace();
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return getStatusCode().value();
            }

            @Override
            public String getStatusText() throws IOException {
                return getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                //响应体
                log.error("微服务路由：{}，服务出现异常",route);
                ResultRes res = ResultRes.error(ResEnum.MICRO_SERVICE_UNAVAILABLE.getValue(),"微服务["+route+"]异常");
                return new ByteArrayInputStream(JSONArray.toJSONString(res).getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return httpHeaders;
            }
        };
    }


    @Override
    public String getRoute() {
        //表明是为哪个微服务提供回退，"*"全部
        return "*";
    }
}
