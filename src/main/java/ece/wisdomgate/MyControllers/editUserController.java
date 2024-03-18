package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.Borrowings;
import ece.wisdomgate.MyObjects.BorrowingsManager;
import ece.wisdomgate.MyObjects.User;
import ece.wisdomgate.MyObjects.UserDataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class editUserController {

    private List<User> users = UserDataManager.deserializeUsers("src/main/resources/ece/wisdomgate/medialab/userData.txt");

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField IDnumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField numberOfBooksBorrowedTextField;

    @FXML
    private Button editButton;

    @FXML
    private Button cancelButton;

    private User selectedUser;
    private final String filePath = "src/main/resources/ece/wisdomgate/medialab/userData.txt";

    private final String BorrowingsFilePath = "src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt";
    private final List<Borrowings> borrowings = BorrowingsManager.deserializeBorrowings(BorrowingsFilePath);

    // Updated initialize method to handle both adding and editing
    public void initialize(List<User> users, User selectedUser) {
        this.users = users;
        this.selectedUser = selectedUser;

        if (selectedUser != null) {
            // If selectedUser is not null, populate the fields for editing
            populateFields(selectedUser);
        }
    }

    private void populateFields(User user) {
        usernameTextField.setText(user.getUsername());
        passwordTextField.setText(user.getPassword());
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        IDnumberTextField.setText(String.valueOf(user.getIDNumber()));
        emailTextField.setText(user.getEmail());
        numberOfBooksBorrowedTextField.setText(String.valueOf(user.getNumBooksBorrowed()));
    }

    @FXML
    private void switchToAdminLibraryPage (Button button) {
        FXMLLoader ButtonLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/adminUsers.fxml"));
        try {
            // Load the scene
            Scene ButtonScene = new Scene(ButtonLoader.load(), 800, 600);

            // Get the current stage
            Stage currentStage = (Stage) button.getScene().getWindow();

            // Set the sign-up scene on the current stage
            currentStage.setScene(ButtonScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editUser() {
        // Get the details from the input fields

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String IDnumber = IDnumberTextField.getText();
        String email = emailTextField.getText();
        int numberOfBooksBorrowed = Integer.parseInt(numberOfBooksBorrowedTextField.getText());

        if(selectedUser != null) {
            for (User user : users) {
                if (user.getIDNumber().equals(selectedUser.getIDNumber())) {
                    for (Borrowings borrowing : borrowings) {
                        if (borrowing.getUser().getIDNumber().equals(selectedUser.getIDNumber())) {
                            borrowing.getUser().setIDNumber(IDnumber);
                            borrowing.getUser().setUsername(username);

                            break; // No need to continue searching once the borrowing is found! :)
                        }
                    }
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setIDNumber(IDnumber);
                    user.setEmail(email);
                    user.setNumBooksBorrowed(numberOfBooksBorrowed);

                    break; // No need to continue searching once the user is found! :)
                }
            }
        }
        else {
            // Create a new book or update the existing one if editing
            User edittedUser = new User(username, password, firstName, lastName, IDnumber, email, numberOfBooksBorrowed);
            // Add the new book to the list
            users.add(edittedUser);
        }

        // Serialize and save the updated list of user info to the files
        UserDataManager.serializeUsers(users, filePath);
        BorrowingsManager.serializeBorrowings(borrowings, BorrowingsFilePath);

        // Switch back to the adminLibrary page
        switchToAdminLibraryPage(editButton);
    }

    @FXML
    private void cancel() {
        // Switch back to the adminLibrary page without changing anything
        switchToAdminLibraryPage(cancelButton);
    }
}
