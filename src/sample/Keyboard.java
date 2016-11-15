package sample;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by tedda on 14/11/2016.
 */
public class Keyboard implements EventHandler<KeyEvent> {

    private Controller controller;

    public Keyboard(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void handle(KeyEvent ke) {
        String key = ke.getCharacter();

        if (ke.isShiftDown() && ke.getCode() == KeyCode.DEAD_DIAERESIS) {
            controller.getButton("^").fire();
        }

        if (ke.getCode() == KeyCode.BACK_SPACE) {
            controller.getButton("backspace").fire();
        }
        else if (ke.getCode() == KeyCode.ENTER) {
            controller.getButton("=").fire();
        }
        else if (key.matches("[0-9*/+-=.()%]")) {
            controller.getButton(key).fire();
        }
    }
}
