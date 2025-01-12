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

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import eu.sonata.nfv.nec.validate.modules.ValidationModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * @author Michael Bredel
 */
@RunWith(GuiceTestRunner.class)
@GuiceModules(ValidationModule.class)
public class ValidationServiceTest extends BasicValidatorTest {
    /** Use Guice to inject the conversion service API. */
    @Inject
    @SuppressWarnings("unused")
    private ValidationService validationService;

    @Before
    public void init() {
        super.validationService = this.validationService;
    }

    @Test
    public void injection() {
        assertNotNull("The injected validation service should not be null!", this.validationService);
        assertNotNull("The injected validation service should not be null!", super.validationService);
    }
}
