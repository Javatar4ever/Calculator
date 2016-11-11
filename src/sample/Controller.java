package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.HashMap;
import java.util.Map;


public class Controller {

    @FXML
    private Button one, two, three, four, five, six, seven, eight, nine, zero, dot, equals, subtract, add, divide, multiply;
    @FXML
    private Label display, historydisplay;
    @FXML
    private TextArea logTextArea;

    private String displayText = "", historydisplayText = "";

    private Calculate calculate;
    private double memory;



    public Controller() {
        calculate = new Calculate();
    }



    public void numPressed(ActionEvent e) {

        if(e.getSource() == zero) { //makes sure the user cannot enter zeroes before other numbers
            if(!displayText.equals("0") && !displayText.equals(""))
                setDisplayText(displayText += "0");
        } else if (e.getSource() == dot) {
            if (displayText.equals(""))
                setDisplayText("0.");
            if (!displayText.contains("."))
                setDisplayText(displayText += ".");
        } else
            setDisplayText(displayText += (((Button)e.getSource()).getText()));

    }

    public void operationPressed(ActionEvent e) {

        if (!displayText.equals("") || historydisplayText.endsWith(")")) {
            historydisplayText += displayText + ((Button) e.getSource()).getText();
            historydisplay.setText(historydisplayText);

            setDisplayText("");
        }
    }

    public void equalsPressed(ActionEvent e) {


        if (displayText.equals("")) display.setText("ERROR");
        else {
            double result = calculate.getAnswer(historydisplayText + displayText);

            historydisplayText += displayText;
            historydisplay.setText(historydisplayText);

            if (result % 1 == 0)
                display.setText((int)result + "");
            else
                display.setText(result + "");

            //Write to log window
            logTextArea.appendText(historydisplayText + " = " + result + "\n");


            historydisplayText = "";
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
                historydisplayText = "";
                historydisplay.setText(historydisplayText);
                setDisplayText("");
                break;
            case ("C"):
                setDisplayText("");
                break;
        }

    }

    public void advancePressed(ActionEvent e) {
        switch (((Button)e.getSource()).getText()) {

            case ("x^2"):

                break;
            case ("x^3"):

                break;
            case ("x^y"):

                break;
            case ("sin"):

                break;
            case ("cos"):

                break;
            case ("tan"):

                break;
            case ("sqrt"):

                break;
            case ("%"):

                break;
            case ("bin"):

                break;
            case ("("):
                historydisplayText += "(";
                historydisplay.setText(historydisplayText);
                displayText = "";
                break;
            case (")"):
                historydisplayText += displayText + ")";
                historydisplay.setText(historydisplayText);
                setDisplayText("");
                break;
            case ("π"):
                setDisplayText(Double.toString(Math.PI));
                break;
        }
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
