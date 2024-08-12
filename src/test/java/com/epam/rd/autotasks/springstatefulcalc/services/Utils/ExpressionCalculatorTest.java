package com.epam.rd.autotasks.springstatefulcalc.services.Utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionCalculatorTest {
    @Test
    public void test1() {
        assertEquals(
                5,
                ExpressionCalculator.calculate("3@+@2@/@1@")
        );
    }

    @Test
    public void test2() {
        assertEquals(
                19,
                ExpressionCalculator.calculate("8941@/@13@/@7@/@5@")
        );
    }
}