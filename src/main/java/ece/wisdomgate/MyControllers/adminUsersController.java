package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class adminUsersController {

    @FXML
    private TableView<User> userTableView;
    private List<User> users;
    private List<Book> books;
    private List<Borrowings> borrowings;

    private final String UserFilePath = "src/main/resources/ece/wisdomgate/medialab/userData.txt";
    private final String BorrowingsFilePath = "src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt";
    private final String BooksFilePath = "src/main/resources/ece/wisdomgate/medialab/bookData.txt";

    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> IDnumberColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Integer> numberOfBooksBorrowedColumn;
    @FXML
    private Button editUserButton;
    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {

        users = UserDataManager.deserializeUsers(UserFilePath);
        books =  BookDataManager.deserializeBooks(BooksFilePath);
        borrowings = BorrowingsManager.deserializeBorrowings(BorrowingsFilePath);

        // Initialize TableColumn instances
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        IDnumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIDNumber()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        numberOfBooksBorrowedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumBooksBorrowed()).asObject());

        // Set the items directly from the list
        updateTableView();
    }

    @FXML
    protected void updateTableView() {
        // Clear existing items in the table
        userTableView.getItems().clear();

        // Add books to the table
        userTableView.getItems().addAll(users);
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
    private void editUserButtonClick() {
        // Load the FXML file
        FXMLLoader editUserButtonLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/editUser.fxml"));
        try {
            // Load the scene
            Scene editUserButtonScene = new Scene(editUserButtonLoader.load(), 800, 600);

            // Get the controller from the FXMLLoader
            editUserController editUserController = editUserButtonLoader.getController();

            // Get the selected user from the ListView
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();

            // Check if a user is selected for editing
            if (selectedUser != null) {
                // Editing an existing user
                editUserController.initialize(users, selectedUser);
            } else {
                // Error alert!
                showErrorAlert("Please select a user to edit!");
                return;
            }

            // Get the current stage
            Stage currentStage = (Stage) editUserButton.getScene().getWindow();

            // Set the scene on the current stage
            currentStage.setScene(editUserButtonScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeSelectedUser() {
        // Get the selected user from the TableView
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        // Check if a user is selected
        if (selectedUser != null) {
            // Remove the selected user from the list
            users.remove(selectedUser);

            // Update the TableView to reflect the changes
            updateTableView();

            // Initialize borrowedBooks list
            List<Book> borrowedBooks = new ArrayList<>();

            // Use Iterator to avoid ConcurrentModificationException
            Iterator<Borrowings> iterator = borrowings.iterator();
            while (iterator.hasNext()) {
                Borrowings borrowing = iterator.next();
                if (borrowing.getUser().getIDNumber().equals(selectedUser.getIDNumber())) {
                    iterator.remove(); // Remove borrowing from borrowings list
                    borrowedBooks.add(borrowing.getBook());
                }
            }

            // Update number of copies for each borrowed book
            for (Book borrowedBook : borrowedBooks) {
                for (Book book : books) {
                    if (book.getISBN().equals(borrowedBook.getISBN())) {
                        int numberOfCopies = book.getNumberOfCopies();
                        book.setNumberOfCopies(numberOfCopies + 1);
                        break;
                    }
                }
            }

            // Serialize and save the updated lists to the files
            BorrowingsManager.serializeBorrowings(borrowings, BorrowingsFilePath);
            BookDataManager.serializeBooks(books, BooksFilePath);
            UserDataManager.serializeUsers(users, UserFilePath);
        } else {
            // Error alert!
            showErrorAlert("Please select a user to remove!");
        }
    }

    @FXML
    private void exitButtonClick () {
        ButtonClick(exitButton, "/ece/wisdomgate/admin.fxml");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}