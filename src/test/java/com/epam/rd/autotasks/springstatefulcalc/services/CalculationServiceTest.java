package com.epam.rd.autotasks.springstatefulcalc.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CalculationServiceTest {
    CalculationService service = new CalculationService();
    @Test
    public void testSetExpressionForFirstTime() {
        assertEquals(201, service.setExpression("a+b+c"));
    }
    @Test
    public void testUpdateExpression() {
        assertEquals(201, service.setExpression("a+b+c"));
        assertEquals(200, service.setExpression("x+y+z"));
    }
    @Test
    public void testGetResultWithNoEnoughData() {
        assertThrows(Exception.class, () -> {
            service.setExpression("a+b");
            service.getResult();
        });
    }
    @Test
    public void testGetResultWithEnoughData() {
        service.setExpression("a+b");
        service.setVariable("a", "-1");
        service.setVariable("b", "1");
        try {
            int result = service.getResult();
            assertEquals(0, result);
        } catch (Exception ignored) {}
    }
}