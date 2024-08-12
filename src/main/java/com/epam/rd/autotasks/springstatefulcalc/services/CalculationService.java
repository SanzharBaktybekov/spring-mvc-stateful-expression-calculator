package com.epam.rd.autotasks.springstatefulcalc.services;

import com.epam.rd.autotasks.springstatefulcalc.services.Utils.ExpressionCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculationService {
    boolean firstTime = true;
    private String expression = null;
    private String originalExpression = null;
    private final Map<String, Integer> variables = new HashMap<>();

    public int setExpression(String expression) {
        if(!isValidFormatOfExpression(expression)) {
            return 400;
        }

        int responseCode = firstTime ? 201 : 200;
        firstTime = false;
        this.expression = expression;
        this.originalExpression = expression;
        return responseCode;
    }

    public int setVariable(String key, String value) {
        int statusCode = variables.containsKey(key) ? 200 : 201;

        try {
            if(Integer.parseInt(value) <= -10000 || Integer.parseInt(value) >= 10000) {
                return 403;
            }
            variables.put(key, Integer.parseInt(value));
        } catch (NumberFormatException e) {
            variables.put(key, variables.get(value));
        }

        return statusCode;
    }

    public void deleteExpression() {
        this.expression = null;
    }

    public void deleteVariable(String key) {
        this.expression = this.originalExpression;
        variables.remove(key);
        setupExpression();
    }

    public int getResult() throws Exception {
        setupExpression();
        if(canCalculate()) {
            System.out.println("can calculate expression:" + this.expression);
            System.out.println("answer is:" + ExpressionCalculator.calculate(this.expression));
            return ExpressionCalculator.calculate(this.expression);
        } else {
            System.out.println("can not calculate expression:" + this.expression);
            throw new Exception("Not all variables are set");
        }
    }

    private void setupExpression() {
        this.expression = this.expression.replace("@", "");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < expression.toCharArray().length; i++) {
            String variableValue = variables.containsKey(String.valueOf(expression.charAt(i))) ?
                    variables.get(String.valueOf(expression.charAt(i))).toString() :
                    String.valueOf(expression.charAt(i));
            if(!variableValue.isBlank()) {
                sb.append(variableValue).append("@");
            }
        }
        this.expression = sb.toString();
    }

    // this is maybe responsibility of another validator class.
    private boolean canCalculate() {
        String expressionRegex = "^[\\d+\\-*/()@ ]+$";
        return this.expression.matches(expressionRegex);
    }

    private boolean isValidFormatOfExpression(String expression) {
        String validCharactersRegex = "^[a-zA-Z0-9()+*/ -]*$";
        String containsOperatorRegex = ".*[+*/-].*";
        if (!expression.matches(validCharactersRegex)) {
            return false;
        }
        return expression.matches(containsOperatorRegex);
    }
}