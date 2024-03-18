package ece.wisdomgate.MyControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class adminController {

    @FXML
    private Button manageBooksButton;

    @FXML
    private Button manageUsersButton;

    @FXML
    private Button manageBorrowingsButton;

    @FXML
    private Button manageCategoriesButton;

    @FXML
    private Button ExitButton;

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
    private void manageBooksButtonClick () {
        ButtonClick(manageBooksButton, "/ece/wisdomgate/adminLibrary.fxml");
    }

    @FXML
    private void manageUsersButtonClick () {
        ButtonClick(manageUsersButton, "/ece/wisdomgate/adminUsers.fxml");
    }

    @FXML
    private void manageBorrowingsButtonClick () {
        ButtonClick(manageBorrowingsButton, "/ece/wisdomgate/adminBorrowings.fxml");
    }

    @FXML
    private void manageCategoriesButtonClick () {
        ButtonClick(manageCategoriesButton, "/ece/wisdomgate/adminCategories.fxml");
    }

    @FXML
    private void ExitButtonClick () {
        ButtonClick(ExitButton, "/ece/wisdomgate/login.fxml");
    }

}
