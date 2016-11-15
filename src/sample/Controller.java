package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.*;

public class Controller {

    private FXMLLoader loader;

    @FXML
    private Label display, historyDisplay;
    @FXML
    private TextArea logTextArea;
    private String displayText = "", historyDisplayText = "";
    private Calculate calculate;
    private double memory;
    private Stack<Integer> parentheses = new Stack<>();
    public static final Map<String, Button> buttons = new HashMap<>();
    private static Map<String, String> functions = new HashMap<>();
    static {
        functions.put("x^2", "sqr");
        functions.put("x^3", "cube");
        functions.put("sin", "sin");
        functions.put("cos", "cos");
        functions.put("tan", "tan");
        functions.put("sqrt", "sqrt");
        functions.put("n!", "fact");
    }


    public Controller() {
        calculate = new Calculate();

    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
        fillButtons();
    }

    private void fillButtons() {
        buttons.put("1", (Button)loader.getNamespace().get("one"));
        buttons.put("2", (Button)loader.getNamespace().get("two"));
        buttons.put("3", (Button)loader.getNamespace().get("three"));
        buttons.put("4", (Button)loader.getNamespace().get("four"));
        buttons.put("5", (Button)loader.getNamespace().get("five"));
        buttons.put("6", (Button)loader.getNamespace().get("six"));
        buttons.put("7", (Button)loader.getNamespace().get("seven"));
        buttons.put("8", (Button)loader.getNamespace().get("eight"));
        buttons.put("9", (Button)loader.getNamespace().get("nine"));
        buttons.put("0", (Button)loader.getNamespace().get("zero"));
        buttons.put(".", (Button)loader.getNamespace().get("dot"));
        buttons.put("=", (Button)loader.getNamespace().get("equals"));
        buttons.put("*", (Button)loader.getNamespace().get("multiply"));
        buttons.put("/", (Button)loader.getNamespace().get("divide"));
        buttons.put("+", (Button)loader.getNamespace().get("add"));
        buttons.put("-", (Button)loader.getNamespace().get("subtract"));
        buttons.put("(", (Button)loader.getNamespace().get("leftParenthesis"));
        buttons.put(")", (Button)loader.getNamespace().get("rightParenthesis"));
        buttons.put("^", (Button)loader.getNamespace().get("exponent"));
        buttons.put("backspace", (Button)loader.getNamespace().get("backspace"));
        buttons.put("!", (Button)loader.getNamespace().get("factorial"));
    }

    public Button getButton(String key) {
        return buttons.get(key);
    }

    public void numPressed(ActionEvent e) {

        if (((Button)e.getSource()).getText().equals("0")) {
            if(!displayText.equals("0") && !displayText.equals(""))
                setDisplayText(displayText += "0");
        } else if (((Button)e.getSource()).getText().equals(".")) {
            if (displayText.equals(""))
                setDisplayText("0.");
            if (!displayText.contains("."))
                setDisplayText(displayText += ".");
        } else
            setDisplayText(displayText += (((Button)e.getSource()).getText()));

    }

    public void operationPressed(ActionEvent e) {

        if (!displayText.equals("") || historyDisplayText.endsWith(")")) {

            if (parentheses.isEmpty())
                solveEquation(historyDisplayText + displayText);
            else
                solveEquation(historyDisplayText.substring(historyDisplayText.lastIndexOf("(") + 1, historyDisplayText.length()) + displayText);

            historyDisplayText += displayText + ((Button) e.getSource()).getText();
            historyDisplay.setText(historyDisplayText);

            displayText = "";
        }
    }

    public void equalsPressed(ActionEvent e) {

        if (!parentheses.isEmpty()) {
            setDisplayText("Not enough closing parentheses");
            displayText = "";
        } else {
            double result = calculate.getAnswer(historyDisplayText + displayText);

            historyDisplayText += displayText;
            historyDisplay.setText(historyDisplayText);

            if (result % 1 == 0)
                display.setText((int)result + "");
            else
                display.setText(result + "");

            logTextArea.appendText(historyDisplayText + " = " + result + "\n");


            historyDisplayText = "";
            displayText = "";
        }
    }

    public void memoryPressed(ActionEvent e) {
        switch (((Button)e.getSource()).getText()) {

            case ("MS"): //Memory Store
                memory = Double.parseDouble(displayText);
                break;
            case ("M+"): //Memory Add
                memory += Double.parseDouble(displayText);
                break;
            case ("MC"): //Memory Clear
                memory = 0;
                break;
            case ("MR"): //Memory Recall
                if (memory % 1 == 0) {
                    setDisplayText(Integer.toString((int)memory));
                } else {
                    setDisplayText(Double.toString(memory));
                }
                break;
        }
    }

    public void controlPressed(ActionEvent e) {

        switch (((Button)e.getSource()).getText()) {

            case ("<--"):
                if (displayText.length() != 0)
                    setDisplayText(displayText.substring(0, displayText.length() - 1));
                break;
            case ("CE"):
                historyDisplayText = "";
                historyDisplay.setText(historyDisplayText);
                setDisplayText("");
                parentheses.clear();
                break;
            case ("C"):
                setDisplayText("");
                break;
        }

    }

    public void advancePressed(ActionEvent e) {

        String button = ((Button)e.getSource()).getText();

        if (button.matches("(x^2|x^3|sin|cos|tan|sqrt|n!)")) {
            callFunction(functions.get(button));
            displayText = "";
        }



        switch (button) {

            case ("+/-"):
                setDisplayText(Double.toString(Double.parseDouble(displayText) * -1));
                break;
            case ("x^y"):
                historyDisplayText += displayText + "^";
                historyDisplay.setText(historyDisplayText);

                setDisplayText("");
                break;
            case ("("):
                parentheses.push(1);
                System.out.println(parentheses.size());

                historyDisplayText += "(";
                historyDisplay.setText(historyDisplayText);
                displayText = "";
                break;
            case (")"):
                if (parentheses.size() == 0) {
                    setDisplayText("Use opening parenthesis first!");
                    displayText = "";
                } else {
                    parentheses.pop();

                    String equation = historyDisplayText + displayText;
                    int beginningOfEquation = findOpeningParenthesis(equation, equation.length()) + 1;
                    solveEquation(equation.substring(beginningOfEquation, equation.length()));

                    historyDisplayText += displayText + ")";
                    historyDisplay.setText(historyDisplayText);

                    displayText = "";
                }
                break;
            case ("π"):
                setDisplayText(Double.toString(Math.PI));
                break;
        }
    }

    private int findOpeningParenthesis(String equation, int closeIndex) {
        int openIndex = closeIndex;
        char[] charArr = equation.toCharArray();
        int counter = 1;

        while (counter > 0) {
            char c = charArr[--openIndex];

            if (c == '(') counter--;
            else if (c == ')') counter++;
        }
        return openIndex;
    }

    private void callFunction(String name) {
        if (historyDisplayText.endsWith(")")) {
            List<String> historyDisplayList = Arrays.asList(historyDisplayText.split(""));

            int beginningOfFunction = findOpeningParenthesis(historyDisplayText, historyDisplayText.length() - 1);

            historyDisplayList.set(beginningOfFunction, name + "(");
            historyDisplayText = String.join("", historyDisplayList);
            historyDisplay.setText(historyDisplayText);

            solveEquation(historyDisplayText.substring(beginningOfFunction, historyDisplayText.length()));
            displayText = "";
        } else {
            historyDisplayText += name + "(" + displayText + ")";
            historyDisplay.setText(historyDisplayText);

            solveEquation(name + "(" + displayText + ")");
        }
    }

    private void solveEquation(String equation) {

        double result = calculate.getAnswer(equation);
        if (result % 1 == 0)
            display.setText((int)result + "");
        else
            display.setText(result + "");

    }


    private void setDisplayText(String text) {
        if (text.equals("")) {
            displayText = text;
            display.setText("0");
        } else {
            displayText = text;
            display.setText(displayText);
        }


    }


}
