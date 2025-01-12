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
package eu.sonata.nfv.nec.validate.internal;

import com.google.json.JsonSanitizer;
import eu.sonata.nfv.nec.validate.ValidationService;
import org.everit.json.schema.Schema;
import org.everit.json.schema.SchemaException;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.UncheckedIOException;

/**
 * Default implementation of the validation service. Validates
 * JSON strings against a given schema. The schema itself has
 * to be a valid JSON string that matches the JSON schema
 * draft-04 specification.
 *
 * The official JSON schema files to test for valid JSON can be
 * found at: https://github.com/json-schema/json-schema
 *
 * @author Michael Bredel
 */
public class DefaultValidator implements ValidationService {
    /** The logger. */
    private static final Logger Logger = LoggerFactory.getLogger(DefaultValidator.class);

    @Override
    public boolean isValid(String jsonString, String schemaString) {
        if (jsonString == null || schemaString == null) {
            return false;
        }

        try {
            this.validate(jsonString, schemaString);
            return true;
        } catch (ValidationException e) {
            if (Logger.isWarnEnabled()) {
                Logger.warn("Validation failed. See reasons below:\n  " + e.getMessage());
            }
            return false;
        } catch (JSONException e) {
            if (Logger.isWarnEnabled()) {
                Logger.warn("Validation failed due to JSON exception. " + e.getMessage());
            }
            return false;
        } catch (UncheckedIOException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                if (Logger.isWarnEnabled()) {
                    Logger.warn("Validation failed. Reference not found." + e.getMessage());
                }
                return false;
            } else {
                throw e;
            }
        } catch (SchemaException e) {
            if (Logger.isWarnEnabled()) {
                Logger.warn("Validation failed due to SCHEMA exception. \n" + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public void validate(String jsonString, String schemaString) throws ValidationException, JSONException, UncheckedIOException, SchemaException {
        // Sanitize the json string.
        jsonString = JsonSanitizer.sanitize(jsonString);
        // Sanitize the schema string.
        schemaString =  JsonSanitizer.sanitize(schemaString);
        // Create a JSONObject.
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new JSONException("Parsing the json failed: " + e.getMessage(), e.getCause());
        }
        // Sanitize the schema string.
        schemaString = JsonSanitizer.sanitize(schemaString);
        // Create a JSONObject from the schema string.
        JSONObject schemaObject;
        try {
            schemaObject = new JSONObject(schemaString);
        } catch (JSONException e) {
            throw new JSONException("Parsing the schema failed: " + e.getMessage(), e.getCause());
        }

        // Create the schema.
        Schema schema = SchemaLoader.load(schemaObject);
        schema.validate(jsonObject);
    }
}
