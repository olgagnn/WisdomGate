package ece.wisdomgate.MyControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class bookCommentsController {

    @FXML
    private TextArea commentsTextArea;

    public void setComments(String comments) {
        commentsTextArea.setText(comments);
        // Disable editing
        commentsTextArea.setEditable(false);
    }

}
