/**
 * The BSD License
 *
 * Copyright (c) 2010-2018 RIPE NCC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the RIPE NCC nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.ripe.rpki.commons.provisioning.x509;


import net.ripe.rpki.commons.validation.ValidationResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static net.ripe.rpki.commons.validation.ValidationString.*;
import static org.junit.Assert.*;

public class ProvisioningCmsCertificateParserTest {

    private ProvisioningCmsCertificateParser subject;


    @Before
    public void setUp() {
        subject = new ProvisioningCmsCertificateParser();
    }

    @Test
    public void shouldParseValidObject() {
        subject.parse("placeholder location", ProvisioningCmsCertificateBuilderTest.TEST_CMS_CERT.getEncoded());
        Assert.assertEquals(ProvisioningCmsCertificateBuilderTest.TEST_CMS_CERT, subject.getCertificate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnInvalidObject() {
        subject.parse("placeholder location", new byte[]{0});

        ValidationResult validationResult = subject.getValidationResult();
        assertTrue(validationResult.hasFailures());
        assertEquals(1, validationResult.getFailuresForCurrentLocation().size());
        assertEquals(CERTIFICATE_PARSED, validationResult.getFailuresForCurrentLocation().iterator().next().getKey());

        subject.getCertificate(); // results in an exception
    }
}
