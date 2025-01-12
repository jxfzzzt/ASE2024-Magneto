package com.magneto.gpt;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magneto.config.ConfigProperty;
import com.magneto.staticanalysis.MethodCall;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GPTSolver {

    public static final String FIELD_PROPERTIES_KEY = "FieldProperties";

    public static final String INPUT_PARAMETERS_KEY = "Arguments";

    private static final ConfigProperty CONFIG_PROPERTY = SpringUtil.getBean(ConfigProperty.class);

    private final GPTServer gptServer;

    private static final Map<String, Pair<String, String>> QUERY_RECORD_MAP = new HashMap<>(); // prompt string ---> (prompt, answer)

    public GPTSolver() {
        ConfigProperty config = SpringUtil.getBean(ConfigProperty.class);
        this.gptServer = new GPTServer(null, config.getGptModelName());
    }

    // <prompt string, gpt answer string>
    public Pair<String, String> getGPTAnswer(MethodCall targetMethodCall, MethodCall triggeredMethodCall, StringBuilder abstractMethodPrompt) throws IOException {
        PromptGenerator generator = new PromptGenerator(targetMethodCall, triggeredMethodCall);
        String prompt = generator.generate(abstractMethodPrompt);

        // firstly get GPT answer from cache
        if (QUERY_RECORD_MAP.containsKey(prompt)) {
            return QUERY_RECORD_MAP.get(prompt);
        }


        String answer = null;
        if (CONFIG_PROPERTY.getEnableGpt()) {
            answer = gptServer.askGPT(prompt);
        }

        answer = trimAnswer(answer);
        // validate the answer
        if (!validateAnswer(answer)) {
            answer = null;
        }
        return new Pair<>(prompt, answer);
    }

    public void addGPTAnswerCache(Pair<String, String> gptAnswer) {
        String prompt = gptAnswer.getKey();
        if (!QUERY_RECORD_MAP.containsKey(prompt)) {
            QUERY_RECORD_MAP.put(prompt, gptAnswer);
        }
    }

    private String trimAnswer(String answer) {
        if (answer == null) {
            return null;
        }
        String[] split = answer.split("\n");
        StringBuilder sb = new StringBuilder();
        if (split[0].contains("```json")) {
            int i = 1;
            while (i < split.length && !split[i].contains("```")) {
                sb.append(split[i]);
                sb.append("\n");
                i++;
            }
            return sb.toString();
        } else {
            return answer;
        }
    }

    public void updateGPTServerStatus(String prompt, String answer) {
        this.gptServer.setLastUserAsk(prompt);
        this.gptServer.setLastGPTAnswer(answer);
    }

    private boolean validateAnswer(String answer) {
        if (answer == null) return false;
        try {
            JSONObject jsonObject = JSONUtil.parseObj(answer);
            return jsonObject.containsKey(FIELD_PROPERTIES_KEY) && jsonObject.containsKey(INPUT_PARAMETERS_KEY);
        } catch (Exception e) {
            return false;
        }
    }

    // e.g. Map<String, String> ---> Map
    private String getTrimTypeName(String typeName) {
        int pos = typeName.indexOf('<');
        if (pos == -1) return typeName;
        else return typeName.substring(0, pos);
    }

    public static void SaveGPTAnswerCache(File file) {
        FileWriter writer = new FileWriter(file);
        List<Map<String, String>> result = new ArrayList<>();

        for (Map.Entry<String, Pair<String, String>> entry : QUERY_RECORD_MAP.entrySet()) {
            Map<String, String> map = new HashMap<>();
            Pair<String, String> pair = entry.getValue();

            map.put("prompt", pair.getKey());
            map.put("answer", pair.getValue());
            result.add(map);
        }

        writer.write(JSONUtil.toJsonPrettyStr(result));
    }
}
