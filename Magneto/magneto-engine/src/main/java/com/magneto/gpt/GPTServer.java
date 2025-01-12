package com.magneto.gpt;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magneto.config.GlobalConfiguration;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/*
GPT response schema:
    {
      "id": "chatcmpl-8LOgIc4Ie3tpFDDmAuxoWZvDGlCQn",
      "object": "chat.completion",
      "created": 1700110454,
      "model": "gpt-3.5-turbo-0613",
      "choices": [
        {
          "index": 0,
          "message": {
            "role": "assistant",
            "content": "xxx"
          },
          "finish_reason": "stop"
        }
      ],
      "usage": {
        "prompt_tokens": 21,
        "completion_tokens": 50,
        "total_tokens": 71
      }
    }
*/

@Slf4j
class GPTServer {
    private final String accessToken;

    private final String modelName;

    private static final String CHAT_ENDPOINT = "https://api.openai.com/v1/chat/completions";

    private static final String DEFAULT_MODEL_NAME = "gpt-3.5-turbo";

    private String lastUserAsk;

    private String lastGPTAnswer;

    public GPTServer() {
        this.accessToken = readAccessToken();
        this.modelName = DEFAULT_MODEL_NAME;
        this.lastUserAsk = null;
        this.lastGPTAnswer = null;
    }

    public GPTServer(String accessToken, String modelName) {
        if (accessToken != null) {
            this.accessToken = accessToken;
        } else {
            this.accessToken = readAccessToken();
        }
        if (modelName == null) {
            modelName = DEFAULT_MODEL_NAME;
        }

        this.modelName = modelName;
        this.lastGPTAnswer = null;
        this.lastUserAsk = null;
    }

    private String readAccessToken() {
        InputStream CONFIGURATION_INPUTSTREAM = new ClassPathResource(GlobalConfiguration.CONFIGURATION_FILE_NAME).getStream();
        Properties properties = new Properties();
        String accessToken = null;
        try {
            properties.load(CONFIGURATION_INPUTSTREAM);
            accessToken = (String) properties.get("gpt.access-token");
        } catch (IOException e) {
            // do nothing
        }
        return accessToken;
    }

    public void reset() {
        this.lastGPTAnswer = null;
        this.lastUserAsk = null;
    }

    public synchronized String askGPT(@NonNull String content) {
        String trimContent = content.replaceAll("\n", "").replaceAll("\\s+", " ");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", modelName);
        List<Map<String, String>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, String>() {{
            put("role", "system");
            put("content", "Today is a workday, you are a professional Java code master. Just returning the JSON body without additional explanation or comments.");
        }});

        // put the last talk into request body
        if (this.lastUserAsk != null) {
            dataList.add(new HashMap<String, String>() {{
                put("role", "user");
                put("content", lastUserAsk);
            }});
        }

        if (this.lastGPTAnswer != null) {
            dataList.add(new HashMap<String, String>() {{
                put("role", "assistant");
                put("content", lastGPTAnswer);
            }});
        }

        dataList.add(new HashMap<String, String>() {{
            put("role", "user");
            put("content", trimContent);
        }});

        paramMap.put("messages", dataList);
        JSONObject message = null;

        String body = null;
        try {
            body = HttpRequest.post(CHAT_ENDPOINT)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(paramMap))
                    .execute()
                    .body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject result = choices.get(0, JSONObject.class, Boolean.TRUE);
            message = result.getJSONObject("message");
            return message.getStr("content");
        } catch (Exception e) {
            log.error("GPTServerAskError, GPT response: {}", body);
            return null;
        }
    }

    public void setLastGPTAnswer(String lastGPTAnswer) {
        this.lastGPTAnswer = lastGPTAnswer;
    }

    public void setLastUserAsk(String lastUserAsk) {
        this.lastUserAsk = lastUserAsk;
    }

}