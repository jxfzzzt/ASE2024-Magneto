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
package eu.sonata.nfv.nec.validate;

import org.apache.commons.io.IOUtils;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Michael Bredel
 */
public abstract class BasicValidatorTest {
    /** The validation service API to test. */
    protected ValidationService validationService;
    /** The schema test string. */
    protected String schemaString;
    /** The model test string. */
    protected String modelString;

    /**
     * Reads the given schema and model file.
     *
     * @param pathToSchemaFile The path to the schema file.
     * @param pathToModelFile The path to the model file.
     */
    private void readTestFiles(String pathToSchemaFile, String pathToModelFile) {
        File schemaFile = new File(pathToSchemaFile);
        File modelFile = new File(pathToModelFile);

        // Read a schema file and populate the schema string.
        try {
            FileInputStream fisYamlFile = new FileInputStream(schemaFile);
            schemaString = IOUtils.toString(fisYamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Read the model file and populate the model string.
        try {
            FileInputStream fisJsonFile = new FileInputStream(modelFile);
            modelString = IOUtils.toString(fisJsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compareValidInputs() {
        readTestFiles("./src/test/resources/address.schema.json", "./src/test/resources/address.json");
        assertTrue("The model should match the schema.", validationService.isValid(modelString, schemaString));
        try {
            validationService.validate(modelString, schemaString);
        } catch (ValidationException e) {
            assertTrue("The model should match the schema.\n" + e.getMessage(), false);
        } catch (Exception e) {
            assertTrue("Something really bad happened.\n" + e.getMessage(), false);
        }
    }

    @Test
    public void compareInvalidInputs() {
        readTestFiles("./src/test/resources/address.schema.json", "./src/test/resources/address_invalid.json");
        assertFalse("The model should NOT match the schema.", validationService.isValid(modelString, schemaString));
        try {
            validationService.validate(modelString, schemaString);
        } catch (ValidationException e) {
            assertTrue("The model should NOT match the schema.\n" + e.getMessage(), true);
        } catch (JSONException e) {
            assertTrue("The json could NOT be parsed.\n" + e.getMessage(), true);
        } catch (Exception e) {
            assertTrue("Something really bad happened.\n" + e.getMessage(), false);
        }
    }

    @Test
    public void nullInputs() {
        assertFalse("The model should NOT match the schema.", validationService.isValid(null, null));
        try {
            validationService.validate(null, null);
        } catch (ValidationException e) {
            assertTrue("The model should NOT match the schema.\n" + e.getMessage(), true);
        } catch (JSONException e) {
            assertTrue("The json could NOT be parsed.\n" + e.getMessage(), true);
        } catch (Exception e) {
            assertTrue("Something really bad happened.\n" + e.getMessage(), false);
        }
    }

    @Test
    public void bogusInputs() {
        assertFalse("The model should NOT match the schema.", validationService.isValid("noJson", "noSchema"));
        try {
            validationService.validate("noJson", "noSchema");
        } catch (ValidationException e) {
            assertTrue("The model should NOT match the schema.\n" + e.getMessage(), true);
        } catch (JSONException e) {
            assertTrue("The json could NOT be parsed.\n" + e.getMessage(), true);
        } catch (Exception e) {
            assertTrue("Something really bad happened.\n" + e.getMessage(), false);
        }
    }

    @Test
    public void test01() {
        // Test the valid test01 json file.
        readTestFiles("./src/test/resources/test01.schema.json", "./src/test/resources/test01.json");
        assertTrue("The model should match the schema.", validationService.isValid(modelString, schemaString));
        try {
            validationService.validate(modelString, schemaString);
        } catch (ValidationException e) {
            assertTrue("The model should match the schema.\n" + e.getMessage(), false);
        } catch (Exception e) {
            assertTrue("Something really bad happened.\n" + e.getMessage(), false);
        }

        // Test the invalid test01 json file.
        readTestFiles("./src/test/resources/test01.schema.json", "./src/test/resources/test01_invalid.json");
        assertFalse("The model should match the schema.", validationService.isValid(modelString, schemaString));
    }
}
