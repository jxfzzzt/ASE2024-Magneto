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
package eu.sonata.nfv.nec.convert;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains the actual tests to test the converter
 * implementation.
 *
 * @author Michael Bredel
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class BasicConverterTest {
    /** The path to the YAML test file. */
    public static final String YAML_FILE = "./src/test/resources/test.yaml";
    /** The path to the JSON test file. */
    public static final String JSON_FILE = "./src/test/resources/test.json";

    /** A very simple YAML string for tests. */
    protected String simpleYamlString = "---\n  foo: \"bar\"\n  baz: \n    - \"qux\"\n    - \"quxx\"";
    /** A very simple JSON string for tests. */
    protected String simpleJsonString = "{\"foo\": \"bar\", \"baz\": [\"qux\",\"quxx\"]}";
    /** The YAML test string. */
    protected String yamlString;
    /** The JSON test string. */
    protected String jsonString;
    /** The conversion service API to test. */
    protected ConversionService conversionService;

    /*
    TODO: Test bogus inputs.
    TODO: Test comparison of YAML and YAML with bogus input. Bogus input might violate the specifications.
    TODO: Test comparison of JSON and JSON with bogus input.
    TODO: Test comparison of JSON and YAML with bogus input.
     */

    /**
     * Read the JSON and YAML test files.
     */
    @Before
    public void readTestFiles() {
        File yamlFile = new File(YAML_FILE);
        File jsonFile = new File(JSON_FILE);

        // Read the yaml file and populate the yaml string.
        try {
            FileInputStream fisYamlFile = new FileInputStream(yamlFile);
            yamlString = IOUtils.toString(fisYamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Read the yaml file and populate the yaml string.
        try {
            FileInputStream fisJsonFile = new FileInputStream(jsonFile);
            jsonString = IOUtils.toString(fisJsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compareValidInputs() {
        assertTrue("The JSON and the YAML string should be the same.", conversionService.compare(jsonString, yamlString));
        assertTrue("Both JSON strings should be the same.", conversionService.compare(jsonString, jsonString));
        assertTrue("Both YAML strings should be the same.", conversionService.compare(yamlString, yamlString));

        assertFalse("The two JSON strings should NOT be the same", conversionService.compare(simpleJsonString, jsonString));
        assertFalse("The JSON and the YAML string should NOT be the same", conversionService.compare(simpleJsonString, yamlString));

        assertFalse("The two YAML strings should NOT be the same", conversionService.compare(simpleYamlString, yamlString));
        assertFalse("The JSON and the YAML string should NOT be the same", conversionService.compare(simpleYamlString, jsonString));

        assertFalse("Second string should be null.", conversionService.compare(yamlString, null));
        assertFalse("First string should be null.", conversionService.compare(null, jsonString));
        assertFalse("Both strings should be null.", conversionService.compare(null, null));
    }

    public void compareMalformedInputs() {
        // TODO Implement.
    }

    @Test
    public void convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("corge", null);
        map.put("grault", 1);
        map.put("garply", true);
        map.put("waldo", "false");
        map.put("fred", "undefined");
        map.put("emptyString", "");
        map.put("emptyArray", new ArrayList<>(0));
        map.put("emptyObject", new HashMap<>(0));
        map.put("baz", new ArrayList<>(Arrays.asList("qux", "quxx")));

        Map<String, Object> jsonMap = conversionService.convertToMap(jsonString);
        Map<String, Object> yamlMap = conversionService.convertToMap(yamlString);

        /*
        // Can be used to debug the test method.
        System.out.println("TEST1: " + map);
        System.out.println("TEST2: " + jsonMap);
        System.out.println("TEST3: " + jsonMap);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Object jsonValue = jsonMap.get(key);

            if (value != null && !value.equals(jsonValue)) {
                System.out.println("TEST4: " + key + " : " + value + " : " + jsonValue);
                System.out.println("TEST5: " + value.getClass());
                System.out.println("TEST6: " + jsonValue.getClass());
            }
        }
        */

        assertTrue("The JSON map should be equal. ", map.equals(jsonMap));
        assertTrue("The YAML map should be equal. ", map.equals(yamlMap));
    }

    @Test
    public void convertYamlToJson() {
        String convertedJsonString = conversionService.convertToJson(yamlString);
        Map<String, Object> mapOriginal = conversionService.convertToMap(yamlString);
        Map<String, Object> mapConverted = conversionService.convertToMap(convertedJsonString);
        assertTrue("The maps should be equal. ", mapOriginal.equals(mapConverted));
    }

    @Test
    public void convertJsonToJson() {
        String convertedJsonString = conversionService.convertToJson(jsonString);
        Map<String, Object> mapOriginal = conversionService.convertToMap(jsonString);
        Map<String, Object> mapConverted = conversionService.convertToMap(convertedJsonString);
        assertTrue("The maps should be equal. ", mapOriginal.equals(mapConverted));
    }

    @Test
    public void convertJsonToYaml() {
        String convertedYamlString = conversionService.convertToYaml(jsonString);
        Map<String, Object> mapOriginal = conversionService.convertToMap(yamlString);
        Map<String, Object> mapConverted = conversionService.convertToMap(convertedYamlString);
        assertTrue("The maps should be equal. ", mapOriginal.equals(mapConverted));
    }

    @Test
    public void convertYamlToYaml() {
        try {
            String convertedYamlString = conversionService.convertToYaml(yamlString);
        } catch (JSONException e) {
            assertTrue(true);
            return;
        }
        assertFalse("We should see an exception.", true);
    }
}
