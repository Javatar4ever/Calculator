package sample;

import java.util.*;

/**
 * Created by tedda on 07/11/2016.
 */
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

    public double getAnswer(String equation) {

        String rpn = getRPN(equation);
        double result = solveRPN(rpn);

        return result;
    }

    /*Shunting-yard algorithm
    Returns equation in Reverse Polish Notation (RPN)

    + - : priority 0
    / * : priority 1

    */
    private String getRPN(String equation) {

        String rpn = "";
        Stack<String> stack = new Stack<>();
        int priority = 0;

        String[] tokens = equation.split("(?<=[-+*/])|(?=[-+*/])");

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("[-+*/]")) { //use of regular expression (regex) to see if operator

                if (stack.isEmpty()) {
                    stack.push(tokens[i]);
                } else {

                    boolean loop = true;

                        String topToken = stack.peek(); //peek at stack to get it's priority

                        if (topToken.matches("[*/]")) priority = 1;
                        else if (topToken.matches("[+-]")) priority = 0;

                        if (priority == 1) {
                            if (tokens[i].matches("[+-]")) {
                                rpn += stack.pop() + " "; //adds to output because priority is higher than + and -
                                i--; //makes so the same priority check can be done on the next operator in stack
                            } else if (tokens[i].matches("[*/]")){
                                rpn += stack.pop() + " "; //adds to output because priority is equal to / and *
                                i--;
                            }
                        } else if (priority == 0) {
                            if (tokens[i].matches("[+-]")) {
                                rpn += stack.pop() + " ";
                                i--;
                            } else if (tokens[i].matches("[*/]")) {
                                stack.push(tokens[i]); //pushes because * and / has higher priority than 0
                            }
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

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches("[-+*/]")) {

                rightOperand = Double.parseDouble(stack.pop());
                leftOperand = Double.parseDouble(stack.pop());

                double result = operations.get(tokens[i]).calculate(leftOperand, rightOperand);

                stack.push(Double.toString(result));

            } else {
                stack.push(tokens[i]);
            }
        }

        return Double.parseDouble(stack.pop());

    }

}


