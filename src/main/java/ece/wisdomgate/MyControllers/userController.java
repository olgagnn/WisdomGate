package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.User;
import ece.wisdomgate.MyObjects.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class userController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button SearchBorrowButton;

    @FXML
    private Button ViewRateButton;

    @FXML
    private Button LogOutButton;

    @FXML
    private void initialize() {
        // Retrieve the logged-in user from the UserSession
        User loggedInUser = UserSession.getLoggedInUser();

        // Set the welcome message using the logged-in user's username
        welcomeLabel.setText("Welcome " + loggedInUser.getFirstName() + "!");

    }

    @FXML
    private void ButtonClick (Button button, String filepath) {
        FXMLLoader ButtonLoader = new FXMLLoader(getClass().getResource(filepath));
        try {
            // Load the scene
            Scene ButtonScene = new Scene(ButtonLoader.load(), 800, 600);

            // Get the current stage
            Stage currentStage = (Stage) button.getScene().getWindow();

            // Set the scene on the current stage
            currentStage.setScene(ButtonScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void SearchBorrowButtonClick() { ButtonClick (SearchBorrowButton, "/ece/wisdomgate/searchBorrow.fxml"); }

    @FXML
    private void ViewRateButtonClick() { ButtonClick (ViewRateButton, "/ece/wisdomgate/viewRate.fxml"); }

    @FXML
    private void LogOutButtonClick() {  ButtonClick (LogOutButton, "/ece/wisdomgate/login.fxml"); }

}
