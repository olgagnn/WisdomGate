package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class loginController {

    @FXML
    private TableView<Book> bookTableView;

    private List<Book> books;
    private final String filePath = "src/main/resources/ece/wisdomgate/medialab/bookData.txt";

    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> ISBNColumn;
    @FXML
    private TableColumn<Book, Integer> avgRatingColumn;
    @FXML
    private TableColumn<Book, Integer> numberOfUsersRatedColumn;

    @FXML
    private TextField LoginUsernameField;

    @FXML
    private PasswordField loginPassField;

    @FXML
    private Button loginButton;

    @FXML
    private Button SignUpButton;

    @FXML
    public void initialize() {
        // Load books from file
        books = BookDataManager.deserializeBooks(filePath);

        // Initialize your TableColumn instances
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        ISBNColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        avgRatingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRating()).asObject());
        numberOfUsersRatedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUsersRated()).asObject());

        // Sort the books by rating in descending order and keep only the first 5
        sortByRatingDescending(books);
        if (books.size() > 5) {
            books = books.subList(0, 5);
        }

        // Set the items directly from the list
        updateTableView();
    }

    @FXML
    protected void updateTableView() {
        // Clear existing items in the table
        bookTableView.getItems().clear();

        // Add books to the table
        bookTableView.getItems().addAll(books);
    }

    private void sortByRatingDescending(List<Book> books) {
        // Define a comparator to compare books based on their ratings in descending order
        Comparator<Book> ratingComparator = Comparator.comparing(Book::getRating, Comparator.reverseOrder());

        // Sort the list using the comparator
        Collections.sort(books, ratingComparator);
    }

    @FXML
    private void LoginButtonClick() {
        // Get the entered username and password
        String enteredUsername = LoginUsernameField.getText();
        String enteredPassword = loginPassField.getText();

        // Check if the entered credentials match the admin's credentials
        if (enteredUsername.equals(Admin.getUsername()) && enteredPassword.equals(Admin.getPassword())) {

            // Load the admin FXML file
            FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/admin.fxml"));

            try {
                // Load the admin scene
                Scene adminScene = new Scene(adminLoader.load(), 800, 600);

                // Get the current stage
                Stage currentStage = (Stage) loginButton.getScene().getWindow();

                // Set the admin scene on the current stage
                currentStage.setScene(adminScene);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            // Deserialize existing users from the file
            List<User> users = UserDataManager.deserializeUsers("src/main/resources/ece/wisdomgate/medialab/userData.txt");

            // Check if the entered credentials match any user's credentials
            for (User user : users) {
                if (enteredUsername.equals(user.getUsername()) && enteredPassword.equals(user.getPassword())) {

                    // Set the logged-in user in the UserSession
                    UserSession.setLoggedInUser(user);

                    // Load the user.fxml file
                    FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/user.fxml"));

                    try {
                        // Load the user scene
                        Scene userScene = new Scene(userLoader.load(), 800, 600);

                        // Get the current stage
                        Stage currentStage = (Stage) loginButton.getScene().getWindow();

                        // Set the user scene on the current stage
                        currentStage.setScene(userScene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return; // Exit the method after successful login
                }
            }

            showErrorAlert("Login failed. Please check your credentials.");
        }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void SignUpButtonClick() {
        // Load the sign-up FXML file
        FXMLLoader signUpLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/signup.fxml"));
        try {
            // Load the sign-up scene
            Scene signUpScene = new Scene(signUpLoader.load(), 800, 600);

            // Get the current stage
            Stage currentStage = (Stage) SignUpButton.getScene().getWindow();

            // Set the sign-up scene on the current stage
            currentStage.setScene(signUpScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitButtonClick() {
        Platform.exit();
    }
}
