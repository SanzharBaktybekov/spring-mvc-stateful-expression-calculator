package com.epam.rd.autotasks.springstatefulcalc.controllers;

import com.epam.rd.autotasks.springstatefulcalc.services.CalculationService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class RequestSupervisor {
    private final Map<String, CalculationService> servicePool = new HashMap<>();
    public int setExpression(String expression, String serviceId) {
        servicePool.putIfAbsent(serviceId, new CalculationService());
        return servicePool.get(serviceId).setExpression(expression);
    }

    public int setVariable(String key, String value, String serviceId) {
        servicePool.putIfAbsent(serviceId, new CalculationService());
        return servicePool.get(serviceId).setVariable(key, value);
    }

    public void deleteExpression(String serviceId) {
        servicePool.get(serviceId).deleteExpression();
    }

    public void deleteVariable(String variable, String serviceId) {
        servicePool.get(serviceId).deleteVariable(variable);
    }

    public int getResult(String serviceId) throws Exception {
        return servicePool.get(serviceId).getResult();
    }
}