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

import eu.sonata.nfv.nec.validate.internal.DefaultValidator;
import org.everit.json.schema.SchemaException;
import org.everit.json.schema.ValidationException;
import java.io.UncheckedIOException;

/**
 * The validator is a singleton implementation of the
 * validator service that uses the default validator in
 * a hard-coded way. Thus, the validator can be used
 * without Guice Dependency Injection.
 *
 * @author Michael Bredel
 */
public class Validator implements ValidationService {
    /** The private static validator instance for singleton. */
    private static Validator instance;
    /** A validation service interface to an validation service instance. */
    private ValidationService validationService;

    /**
     * A private constructor for singleton.
     */
    private Validator() {
        this.validationService = new DefaultValidator();
    }

    /**
     * Gets a singleton validator instance.
     *
     * @return A singleton validator instance.
     */
    public static Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    @Override
    public boolean isValid(String jsonString, String schemaString) {
        return validationService.isValid(jsonString, schemaString);
    }

    @Override
    public void validate(String jsonString, String schemaString) throws ValidationException, UncheckedIOException, SchemaException {
        validationService.validate(jsonString, schemaString);
    }
}
