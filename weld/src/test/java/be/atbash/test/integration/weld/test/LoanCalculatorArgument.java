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

import org.junit.jupiter.params.provider.Arguments;

import java.util.StringJoiner;

public class LoanCalculatorArgument  {

    private final double value;
    private final int year;
    private final double interestRate;
    private final Double expected;

    public LoanCalculatorArgument(double value, int year, double interestRate, Double expected) {
        this.value = value;
        this.year = year;
        this.interestRate = interestRate;
        this.expected = expected;
    }

    public double getValue() {
        return value;
    }

    public int getYear() {
        return year;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public Double getExpected() {
        return expected;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoanCalculatorArgument.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .add("year=" + year)
                .add("interestRate=" + interestRate)
                .add("expected=" + expected)
                .toString();
    }
}
