package sample;

import java.util.*;

public class Calculate {

    interface Operation {
        double calculate(double a, double b);
    }

    //Hashmap to dynamically access functions
    public static final Map<String, Operation> operations = new HashMap<>();
    static {
        operations.put("+", (a, b) -> a + b);
        operations.put("-", (a, b) -> a - b);
        operations.put("*", (a, b) -> a * b);
        operations.put("/", (a, b) -> a / b);
    }

    public Calculate() {
        System.out.println(getRPN("10*(10+9)-5"));
    }

    public double getAnswer(String equation) {

        String rpn = getRPN(equation);
        return solveRPN(rpn);
    }

    /*Shunting-yard algorithm
    Returns equation in Reverse Polish Notation (RPN)

    ( ) : priority 0
    + - : priority 1
    / * : priority 2

    */
    private String getRPN(String equation) {

        String rpn = "";
        Stack<String> stack = new Stack<>();
        int priority = 0;

        String[] tokens = equation.split("(?<=[-+*/()])|(?=[-+*/()])");

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("[-+*/()]")) { //use of regular expression (regex) to see if operator

                if (stack.isEmpty()) {
                    stack.push(tokens[i]);
                } else {

                    //Handle parantheses

                    if (tokens[i].matches("[()]")) {
                        if (tokens[i].equals("(")) {
                            stack.push(tokens[i]);

                        } else {
                            while (!stack.isEmpty() && !stack.peek().equals("(")) {
                                rpn += stack.pop() + " ";
                            }

                            stack.pop(); //disregard left paranthesis
                        }
                        continue;
                    }


                    String topToken = stack.peek(); //peek at stack to get it's priority

                    if (topToken.matches("[*/]")) priority = 2;
                    else if (topToken.matches("[+-]")) priority = 1;
                    else if (topToken.matches("[(]")) priority = 0;

                    if (priority == 2) {
                        if (tokens[i].matches("[+-]")) {
                            rpn += stack.pop() + " "; //adds to output because priority is higher than + and -
                            i--; //makes so the same priority check can be done on the next operator in stack
                        } else if (tokens[i].matches("[*/]")){
                            rpn += stack.pop() + " "; //adds to output because priority is equal to / and *
                            i--;
                        }
                    } else if (priority == 1) {
                        if (tokens[i].matches("[+-]")) {
                            rpn += stack.pop() + " ";
                            i--;
                        } else if (tokens[i].matches("[*/]")) {
                            stack.push(tokens[i]); //pushes because * and / has higher priority than 0
                        }
                    } else { //condition == 0
                        stack.push(tokens[i]); //paranthesis can't be added to output queue
                    }
                }

            } else { //token is a number
                rpn += tokens[i] + " "; //space to be able to split
            }
        } // end for

        while(!stack.isEmpty()) { //fill rpn with remaining operators
            rpn += stack.pop() + " ";
        }

        return rpn;

    }

    private double solveRPN(String rpn) {

        Stack<String> stack = new Stack<>();
        double rightOperand, leftOperand;

        String[] tokens = rpn.split(" ");

        for (String token : tokens) {
            if (token.matches("[-+*/]")) {

                rightOperand = Double.parseDouble(stack.pop());
                leftOperand = Double.parseDouble(stack.pop());

                double result = operations.get(token).calculate(leftOperand, rightOperand);

                stack.push(Double.toString(result));

            } else {
                stack.push(token);
            }
        }

        return Double.parseDouble(stack.pop());

    }

}


