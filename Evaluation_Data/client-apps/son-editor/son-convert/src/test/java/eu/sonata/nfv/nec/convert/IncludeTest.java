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

import eu.sonata.nfv.nec.convert.internal.IncludeConstructor;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Michael Bredel
 */
public class IncludeTest {
    /** The path to the YAML test file. */
    public static final String YAML_FILE = "./src/test/resources/include.yaml";

    /** The YAML test string. */
    protected String yamlString;

    /**
     * Read the JSON and YAML test files.
     */
    @Before
    public void readTestFiles() {
        File yamlFile = new File(YAML_FILE);

        // Read the yaml file and populate the yaml string.
        try {
            FileInputStream fisYamlFile = new FileInputStream(yamlFile);
            yamlString = IOUtils.toString(fisYamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Yaml yaml = new Yaml(new IncludeConstructor());
        Object data = yaml.load(yamlString);
        Map<String, Object> map = (Map<String, Object>) data;
        System.out.println("TEST: " + map);
    }
}
