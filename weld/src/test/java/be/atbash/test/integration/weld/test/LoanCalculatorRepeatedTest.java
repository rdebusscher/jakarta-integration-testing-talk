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
package be.atbash.test.integration.weld.test;

import be.atbash.test.integration.weld.AnnualInterestRateService;
import be.atbash.test.integration.weld.LoanCalculator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.ExplicitParamInjection;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

@ExtendWith(WeldJunit5Extension.class)
@ExplicitParamInjection  // Otherwise Weld tries to resolve the parameter of the Test method
class LoanCalculatorRepeatedTest {

    @Inject
    LoanCalculator loanCalculator;

    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.from(LoanCalculator.class).addBeans(createMockBean()).build();

    private static AnnualInterestRateServiceMock annualInterestRateServiceMock;

    static Bean<?> createMockBean() {
        annualInterestRateServiceMock = new AnnualInterestRateServiceMock();
        return MockBean.builder()
                .types(AnnualInterestRateService.class)
                .scope(ApplicationScoped.class)
                .creating(annualInterestRateServiceMock)
                .build();
    }


    @ParameterizedTest()
    @ArgumentsSource(LoanCalculatorArgumentsProvider.class)
    void calculateMonthlyAmount(LoanCalculatorArgument argument) {
        annualInterestRateServiceMock.setInterestValue(argument.getInterestRate());
        double monthlyAmount = loanCalculator.calculateMonthlyAmount(argument.getValue(), argument.getYear());

        Assertions.assertThat(monthlyAmount).isEqualTo(argument.getExpected(), Offset.offset(0.005));
    }

    private static class AnnualInterestRateServiceMock extends AnnualInterestRateService {
        private double interestValue;

        @Override
        public double getCurrentValue() {
            return interestValue;
        }

        public void setInterestValue(double interestValue) {
            this.interestValue = interestValue;
        }
    }
}