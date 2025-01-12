/*
 * Copyright (C) 2015, SONATA-NFV, NEC
 * ALL RIGHTS RESERVED
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *
 * Neither the name of the SONATA-NFV and NEC nor the names of 
 * its contributors may be used to endorse or promote products 
 * derived from this software without specific prior written 
 * permission.
 * 
 * This work has been performed in the framework of the SONATA project,
 * funded by the European Commission under Grant number 671517 through 
 * the Horizon 2020 and 5G-PPP programmes. The authors would like to 
 * acknowledge the contributions of their colleagues of the SONATA 
 * partner consortium (www.sonata-nfv.eu).
 */
package eu.sonata.nfv.nec.convert.internal;

import com.google.json.JsonSanitizer;
import eu.sonata.nfv.nec.convert.ConversionService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A default implementation of the conversion service
 * based on the json.org library and snakeYAML.
 *
 * The implementation creates a general map structure
 * from the given JSON/YAML string. It operates on the
 * general map structure, for example to perform
 * comparisons. The map structure can also be transferred
 * to any string representation, i.e. JSON or YAML, when
 * needed.
 *
 * @author Michael Bredel
 */
@Singleton
public class DefaultConverter implements ConversionService {
    /** The logger. */
    private static final Logger Logger = LoggerFactory.getLogger(DefaultConverter.class);

    @Override
    @SuppressWarnings("unchecked")
    public String convertToJson(String yamlString) {
        // First, create a new YAML object.
        Yaml yaml = new Yaml();
        Map<String, Object> map = null;
        try {
            map = (Map) yaml.load(yamlString);
        } catch (ScannerException e) {
            if (Logger.isWarnEnabled()) {
                Logger.warn("Cannot parse the given YAML string. \n" + e.getLocalizedMessage());
            }
            // Propagate the exception.
            throw e;
        }
        // Next, convert the map to the JSON string.
        return JsonSanitizer.sanitize(convertToJson(map));
    }

    @Override
    public String convertToJson(Map<String, Object> map) {
        // Create a JSON object.
        JSONObject jsonObject = toJSONObject(map);
        return JsonSanitizer.sanitize(jsonObject.toString());
    }

    @Override
    public String convertToYaml(String jsonString) throws JSONException {
        // Sanitize the json string.
        jsonString = JsonSanitizer.sanitize(jsonString);
        // First, create a JSONObject.
        JSONObject jsonObject = new JSONObject(jsonString);
        // Next, create a map.
        Map<String, Object> map = toMap(jsonObject);
        // Finally, convert.
        return convertToYaml(map);
    }

    @Override
    public String convertToYaml(Map<String, Object> map) {
        // Set some dump options to beautify the output.
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setExplicitStart(true);
        Yaml yaml = new Yaml(options);
        // Dump the yaml.
        return yaml.dump(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToMap(String string) throws IllegalArgumentException {
        // Apparently, Yaml can be used for JSON and YAML formatted string.
        Yaml yaml = new Yaml();
        Map<String, Object> map;
        try {
            map = (Map) yaml.load(string);
        } catch (ClassCastException e) {
            // Happens iff the input string is not JSON nor YAML.
            throw new IllegalArgumentException("Input is no valid JSON or YAML string.");
        }
        return map;
    }

    @Override
    public boolean compare(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        Map<String, Object> mapA;
        Map<String, Object> mapB;
        try {
            mapA = convertToMap(a);
        } catch (IllegalArgumentException e) {
            if (Logger.isErrorEnabled()) {
                Logger.error("Could not parse first string to a map. " + e.getLocalizedMessage());
            }
            return false;
        }
        try {
            mapB = convertToMap(b);
        } catch (IllegalArgumentException e) {
            if (Logger.isErrorEnabled()) {
                Logger.error("Could not parse second string to a map. " + e.getLocalizedMessage());
            }
            return false;
        }
        return mapA.equals(mapB);
    }

    /**
     * Transfers a JSONObject to a map.
     *
     * @param object The JSONObject that is transferred to a map.
     * @return A Map that represents the JSONObject.
     */
    @Deprecated
    private Map<String, Object> toMap(JSONObject object) {
        if (object == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        for (String key : object.keySet()) {
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value.equals(JSONObject.NULL)) {
                value = null;
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * Transfers a JSONArray object to a list.
     *
     * @param array A JSONArray object that is transferred to a list.
     * @return A list that represents the JSONArray.
     */
    @Deprecated
    private List<Object> toList(JSONArray array) {
        if (array == null) {
            return null;
        }
        List<Object> list = new ArrayList<>();
        for (Object value : array) {
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    /**
     * Creates a JSONObject from a map. In addition to the standard
     * method, it preserves 'null' values.
     *
     * @param map The map that is transferred to a JSONObject.
     * @return A JSONObject that represents the given map.
     */
    @SuppressWarnings("unchecked")
    private JSONObject toJSONObject(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        JSONObject result = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // We have to explicitly check for null and set the value to the sentinel value JSONObject.NULL.
            if (value == null) {
                result.put(key, JSONObject.NULL);
                continue;
            }
            if (value instanceof Map) {
                value = toJSONObject((Map) value);
            } else if (value instanceof Collection) {
                value = new JSONArray((Collection) value);
            }
            result.put(key, value);
        }
        return result;
    }
}
