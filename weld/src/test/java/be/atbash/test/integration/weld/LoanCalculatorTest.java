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
package be.atbash.test.integration.weld;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

@ExtendWith(WeldJunit5Extension.class)
class LoanCalculatorTest {

    @Inject
    LoanCalculator loanCalculator;

    @WeldSetup
    public WeldInitiator weld =
            WeldInitiator.from(LoanCalculator.class)
                    .addBeans(createMockBean())
                    .build();

    static Bean<?> createMockBean() {
        return MockBean.builder()
                .types(AnnualInterestRateService.class)
                .scope(ApplicationScoped.class)
                .creating(
                        // Mock object provided by Mockito
                        Mockito.when(Mockito.mock(AnnualInterestRateService.class).getCurrentValue())
                                .thenReturn(5.99).getMock())
                .build();
    }

    @Test
    void calculateMonthlyAmount() {
        double monthlyAmount = loanCalculator.calculateMonthlyAmount(15000.00, 6);

        Assertions.assertThat(monthlyAmount).isEqualTo(255.66, Offset.offset(0.005));
    }
}