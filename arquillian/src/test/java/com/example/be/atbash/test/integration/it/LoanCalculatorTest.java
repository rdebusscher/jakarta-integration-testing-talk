/*
 * Copyright 2022-2023 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.be.atbash.test.integration.it;

import be.atbash.test.integration.arquillian.AnnualInterestRateService;
import be.atbash.test.integration.arquillian.LoanCalculator;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import java.util.logging.Logger;

@ExtendWith(ArquillianExtension.class)
public class LoanCalculatorTest {

    private final static Logger LOGGER = Logger.getLogger(LoanCalculatorTest.class.getName());

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(LoanCalculator.class, AnnualInterestRateService.class, AnnualInterestRateFake.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    AnnualInterestRateFake myAlternativeBean;

    @Inject
    LoanCalculator service;

    @Test
    @DisplayName("testing loanCalculator")
    public void testCalc() {
        myAlternativeBean.setRate(6.99);
        double message = service.calculateMonthlyAmount(15000.00, 6);
        // We don't have AssertJ available on server, or create War and add dependency!
        //Assertions.assertThat(message).isEqualTo(123.0);
        Assertions.assertEquals(255.66, message, 0.005);
    }
}
