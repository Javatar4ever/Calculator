package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;


public class Controller {

    @FXML
    private Button one, two, three, four, five, six, seven, eight, nine, zero, dot, equals, subtract, add, divide, multiply;
    @FXML
    private Label display, historydisplay;

    private String displayText = "", historydisplayText = "";

    private Calculate calculate;



    public Controller() {
        calculate = new Calculate();
    }



    public void numPressed(ActionEvent e) {

        if(e.getSource() == zero) { //makes sure the user cannot enter zeroes before other numbers
            if(!displayText.equals("0") && !displayText.equals("")) {
                displayText += "0";
                display.setText(displayText);
            }
        } else if (e.getSource() == dot) {
            if (displayText.equals("")) {
                displayText = "0.";
                display.setText(displayText);
            }
            if (!displayText.contains(".")) {
                displayText += ".";
                display.setText(displayText);
            }
        } else {
            displayText += (((Button)e.getSource()).getText());
            display.setText(displayText);
        }

    }

    public void operationPressed(ActionEvent e) {

        if (!displayText.equals("")) {
            historydisplayText += displayText + ((Button) e.getSource()).getText();
            historydisplay.setText(historydisplayText);

            displayText = "";
            display.setText(displayText);
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

            historydisplayText = "";
            displayText = "";
        }

    }
}
