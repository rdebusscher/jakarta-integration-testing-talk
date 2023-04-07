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
package be.atbash.test.integration.atbash;

import be.atbash.test.integration.atbash.remote.LoanService;
import be.atbash.testing.integration.jupiter.ContainerIntegrationTest;
import be.atbash.testing.integration.jupiter.SupportedRuntime;
import be.atbash.testing.integration.test.AbstractContainerIntegrationTest;
import be.atbash.testing.integration.wiremock.WireMockContainer;
import be.atbash.testing.integration.wiremock.model.mappings.MappingBuilder;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;


@ContainerIntegrationTest(runtime = SupportedRuntime.PAYARA_MICRO/*, debug = true*/)
public class LoanResourceIT extends AbstractContainerIntegrationTest {

    @Container
    public static WireMockContainer wireMockContainer = WireMockContainer.forHost("remote");

    @RestClient
    public LoanService loanService;

    @Test
    public void testLoan() {
        MappingBuilder mappingBuilder = new MappingBuilder().forURL("/interest")
                .withBody("6.99");
        wireMockContainer.configureResponse(mappingBuilder);
        double data = loanService.calculate(15000.0, 6);

        Assertions.assertThat(data).isEqualTo(255.66, Offset.offset(0.005));

    }

}