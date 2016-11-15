package sample;

import java.util.*;

public class Calculate {

    public static final String functionsRegex = "(sqrt|sqr|sin|cos|tan|cube|fact)";
    public static final String operatorRegex = "[-+*/^%]";

    interface Operation {
        double calculate(double a, double b);
    }
    interface Function {
        double calculate(double a);
    }

    public static final Map<String, Operation> operations = new HashMap<>();
    static {
        operations.put("+", (a, b) -> a + b);
        operations.put("-", (a, b) -> a - b);
        operations.put("*", (a, b) -> a * b);
        operations.put("/", (a, b) -> a / b);
        operations.put("^", (a, b) -> Math.pow(a, b));
        operations.put("%", (a, b) -> a % b);
    }
    public static final Map<String, Function> functions = new HashMap<>();
    static {
        functions.put("sqrt", a -> Math.sqrt(a));
        functions.put("sqr", a -> Math.pow(a, 2));
        functions.put("cube", a -> Math.pow(a, 3));
        functions.put("sin", a -> Math.sin(a));
        functions.put("cos", a -> Math.cos(a));
        functions.put("tan", a -> Math.tan(a));
        functions.put("fact", a -> factorial(a));
    }

    private static double factorial(double number) {
        if (number <= 1) {
            return 1;
        } else {
            return number * factorial(number - 1);
        }
    }

    public double getAnswer(String equation) {
        String rpn = getRPN(equation);
        return solveRPN(rpn);
    }

    //Shunting yard algorithm: https://en.wikipedia.org/wiki/Shunting-yard_algorithm
    private String getRPN(String equation) {

        String outputRPN = "";
        Stack<String> operatorStack = new Stack<>();
        int topPrecedence, currentPrecedence;
        boolean leftAssociative;

        String[] tokens = equationToTokens(equation);

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches(functionsRegex)) {
                operatorStack.push(tokens[i]);
            }
            else if (tokens[i].matches("[()]")) {
                if (tokens[i].equals("(")) {
                    operatorStack.push(tokens[i]);
                } else {
                    while (!operatorStack.peek().equals("(")) {
                        outputRPN += operatorStack.pop() + " ";
                    }

                    operatorStack.pop();

                    if (operatorStack.peek().matches(functionsRegex)) {
                        outputRPN += operatorStack.pop() + " ";
                    }
                }
            }
            else if (tokens[i].matches(operatorRegex)) {

                if (operatorStack.isEmpty()) {
                    operatorStack.push(tokens[i]);
                } else {

                    String topToken = operatorStack.peek();

                    if (topToken.equals("^")) topPrecedence = 3;
                    else if (topToken.matches("[*/%]")) topPrecedence = 2;
                    else if (topToken.matches("[+-]")) topPrecedence = 1;
                    else if (topToken.matches("[(]")) topPrecedence = 0;
                    else topPrecedence = 0;

                    if (tokens[i].equals("^")) { currentPrecedence = 3; leftAssociative = false; }
                    else if (tokens[i].matches("[*/%]")) { currentPrecedence = 2; leftAssociative = true; }
                    else if (tokens[i].matches("[+-]")) { currentPrecedence = 1; leftAssociative = true; }
                    else { currentPrecedence = 0; leftAssociative = true; }

                    if ((leftAssociative && (currentPrecedence <= topPrecedence)) ||
                            (!leftAssociative && currentPrecedence < topPrecedence)) {
                        outputRPN += operatorStack.pop() + " ";
                        i--;
                    } else {
                        operatorStack.push(tokens[i]);
                    }
                }
            } else {
                outputRPN += tokens[i] + " ";
            }
        }

        while(!operatorStack.isEmpty()) {
            outputRPN += operatorStack.pop() + " ";
        }

        return outputRPN;
    }

    //Solve Reverse Polish Notation: https://en.wikipedia.org/wiki/Reverse_Polish_notation
    private double solveRPN(String rpn) {

        Stack<String> numberStack = new Stack<>();
        double rightOperand, leftOperand;

        String[] tokens = rpn.split(" ");

        for (String token : tokens) {

            if (token.matches(functionsRegex)) {
                double result = functions.get(token).calculate(Double.parseDouble(numberStack.pop()));
                numberStack.push(Double.toString(result));
            }

            else if (token.matches(operatorRegex)) {

                rightOperand = Double.parseDouble(numberStack.pop());
                leftOperand = Double.parseDouble(numberStack.pop());

                double result = operations.get(token).calculate(leftOperand, rightOperand);

                numberStack.push(Double.toString(result));

            } else {
                numberStack.push(token);
            }
        }
        return Double.parseDouble(numberStack.pop());
    }

    private String[] equationToTokens(String equation) {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(equation.split(
                "(?<!((\\A|[-+*/^(%])-))(?<=[-+*/(^%]|sqrt|\bsqr\b|cube|sin|cos|tan|fact)|(?=[-+*/()^%]|sqrt|\bsqr\b|cube|sin|cos|tan|fact)")));

        return tokens.toArray(new String[tokens.size()]);
    }

}


