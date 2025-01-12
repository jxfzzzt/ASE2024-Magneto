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
package eu.sonata.nfv.nec.validate.modules;

import com.google.inject.AbstractModule;
import eu.sonata.nfv.nec.validate.ValidationService;
import eu.sonata.nfv.nec.validate.internal.DefaultValidator;

/**
 * The Guice module that binds the validation service to
 * a default validation implementation.
 *
 * @author Michael Bredel
 */
public class ValidationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ValidationService.class).to(DefaultValidator.class);
    }
}
