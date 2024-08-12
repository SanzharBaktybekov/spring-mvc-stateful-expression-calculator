package com.epam.rd.autotasks.springstatefulcalc.services.Utils;

import java.util.List;
import java.util.Stack;

public class ExpressionCalculator {
    private static final Stack<String> operands = new Stack<>();
    private static final Stack<String> operators = new Stack<>();
    private static int getPriority(String operator) {
        switch (operator) {
            case "+": case "-":
                return 1;
            case "*": case "/":
                return 2;
            default:
                return 0;
        }
    }
    private static boolean isOperator(String operatorCandidate) {
        return List.of("+", "-", "/", "*", "(", ")").contains(operatorCandidate);
    }

    private static int calculate(String operand1, String operand2, String operator) {
        int a = Integer.parseInt(operand2);
        int b = Integer.parseInt(operand1);

        int result = 0;
        switch (operator) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                result = a / b;
                break;
        }
        return result;
    }

    public synchronized static int calculate(String expression) {
        String[] expressionItems = expression.split("@");
        for(int i = 0; i < expressionItems.length; i++) {
            if(isOperator(expressionItems[i])) {
                handleOperator(expressionItems[i]);
            } else {
                handleOperand(expressionItems[i]);
            }
        }
        while (!operators.isEmpty()) {
            operands.push(operators.pop());
        }
        StringBuilder sb = new StringBuilder();
        operands.forEach(expItem -> sb.append(expItem).append("@"));
        return calcPostfixNotation(sb.toString());
    }

    private synchronized static int calcPostfixNotation(String postfixNotationExpression) {
        Stack<String> result = new Stack<>();
        String[] expItems = postfixNotationExpression.split("@");

        for(int i = 0; i < expItems.length; i++) {
            if(!isOperator(expItems[i])) {
                result.push(expItems[i]);
            } else {
                int currentResult = calculate(result.pop(), result.pop(), expItems[i]);
                result.push(String.valueOf(currentResult));
            }
        }
        clear();
        return Integer.parseInt(result.pop());
    }

    private static void clear() {
        while (!operands.isEmpty()) {
            operands.pop();
        }
        while (!operators.isEmpty()) {
            operators.pop();
        }
    }

    private static void handleOperand(String operand) {

        operands.push(operand);
    }

    private static void handleOperator(String operator) {
        if(operator.equals("(")) {
            operators.push(operator);
            return;
        }
        if(operator.equals(")")) {
            while (!operators.peek().equals("(")) {
                operands.push(operators.pop());
            }
            operators.pop();
            return;
        }

        if(operators.isEmpty()) {
            operators.push(operator);
        } else {
            if (getPriority(operator) <= getPriority(operators.peek())) {
                operands.push(operators.pop());
            }
            operators.push(operator);
        }
    }
}