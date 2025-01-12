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

import org.everit.json.schema.SchemaException;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import java.io.UncheckedIOException;

/**
 * The validation service offers the interface
 * specification to validate a JSON string against
 * a given schema.
 *
 * @author Michael Bredel
 */
public interface ValidationService {

    /**
     * Checks if a given JSON string is valid according
     * to the given schema file.
     *
     * @param jsonString A JSON string that is checked.
     * @param schemaString The JSON schema to check against.
     * @return True iff the given JSON string meets the given schema.
     */
    boolean isValid(String jsonString, String schemaString);

    /**
     * Validates a given JSON string is valid according
     * to the given schema file.
     *
     * @param jsonString A JSON string that is validated.
     * @param schemaString The JSON schema to check against.
     * @throws ValidationException The validation exception message contains hints why the validation has failed.
     */
    void validate(String jsonString, String schemaString) throws ValidationException, JSONException, UncheckedIOException, SchemaException;
}
