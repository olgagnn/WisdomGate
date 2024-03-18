package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.User;
import ece.wisdomgate.MyObjects.UserDataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.List;

public class signupController {

    @FXML
    private TextField UsernameTextField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField FirstNameTextField;
    @FXML
    private TextField LastNameTextField;
    @FXML
    private TextField IDNumberTextField;
    @FXML
    private TextField EmailTextField;
    @FXML
    private Button signupButton;
    @FXML
    private Button cancelButton;

    private final String filePath = "src/main/resources/ece/wisdomgate/medialab/userData.txt";

    @FXML
    private void signupButtonClick() {
        // Get the details from the input fields
        String username = UsernameTextField.getText();
        String password = PasswordField.getText();
        String firstName = FirstNameTextField.getText();
        String lastName = LastNameTextField.getText();
        String iDNumber = IDNumberTextField.getText();
        String email = EmailTextField.getText();

        // Deserialize existing users from the file
        List<User> users = UserDataManager.deserializeUsers(filePath);

        // Check if the email or ID number already exists
        if (isUserExistsWithEmail(users, email)) {
            // Display an error alert
            showErrorAlert("User with the same email already exists!");
        } else if (isUserExistsWithIDNumber(users, iDNumber)) {
            // Display an error alert
            showErrorAlert("User with the same ID number already exists!");
        } else {
            // Create a new user
            User newUser = new User(username, password, firstName, lastName, iDNumber, email);

            // Add the new user to the list
            users.add(newUser);

            // Serialize and save the updated list of users to the file
            UserDataManager.serializeUsers(users, filePath);

            // Display a success message
            showSuccessAlert("User created successfully!");

            // Switch back to the login page
            switchToLoginPage();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Check if a user with the given email already exists
    private boolean isUserExistsWithEmail(List<User> users, String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    // Check if a user with the given ID number already exists
    private boolean isUserExistsWithIDNumber(List<User> users, String iDNumber) {
        return users.stream().anyMatch(user -> user.getIDNumber().equals(iDNumber));
    }

    private void switchToLoginPage() {
        // Load the login FXML file
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/login.fxml"));
        try {
            // Load the login scene
            Scene loginScene = new Scene(loginLoader.load(), 800, 600);

            // Get the current stage
            Stage currentStage = (Stage) signupButton.getScene().getWindow();

            // Set the login scene on the current stage
            currentStage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelButtonClick() {

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/login.fxml"));
        try {

            Scene loginScene = new Scene(loginLoader.load(), 800, 600);

            Stage currentStage = (Stage) cancelButton.getScene().getWindow();

            currentStage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

