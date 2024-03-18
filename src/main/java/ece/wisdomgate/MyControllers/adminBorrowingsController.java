package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class adminBorrowingsController {

    private final String BorrowingsFilePath = "src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt";
    private final String BooksFilePath = "src/main/resources/ece/wisdomgate/medialab/bookData.txt";
    private final String UsersFilePath = "src/main/resources/ece/wisdomgate/medialab/userData.txt";
    private final List<User> users = UserDataManager.deserializeUsers(UsersFilePath);
    private final List<Book> books = BookDataManager.deserializeBooks(BooksFilePath);
    private final List<Borrowings> borrowings = BorrowingsManager.deserializeBorrowings(BorrowingsFilePath);

    @FXML
    private TableView<Borrowings> borrowingsTableView;
    @FXML
    private TableColumn<Borrowings, String> usernameColumn;
    @FXML
    private TableColumn<Borrowings, String> IDNumberColumn;
    @FXML
    private TableColumn<Borrowings, String> bookTitleColumn;
    @FXML
    private TableColumn<Borrowings, String> bookISBNColumn;
    @FXML
    private TableColumn<Borrowings, String> bookCategoryColumn;
    @FXML
    private TableColumn<Borrowings, LocalDate> borrowDateColumn;
    @FXML
    private TableColumn<Borrowings, LocalDate> returnDateColumn;
    @FXML
    private Button exitButton;

    @FXML
    private void initialize() {

        // Initialize TableColumn instances
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));
        IDNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getIDNumber()));
        bookTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        bookISBNColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getISBN()));
        bookCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getCategory()));
        borrowDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBorrowDate()));
        returnDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReturnDate()));

        // Display user borrowings in the table
        borrowingsTableView.getItems().addAll(borrowings);
    }

    @FXML
    private void exitButtonClick() {
        FXMLLoader exitButtonLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/admin.fxml"));
        try {
            Scene exitButtonScene = new Scene(exitButtonLoader.load(), 800, 600);

            Stage currentStage = (Stage) exitButton.getScene().getWindow();

            currentStage.setScene(exitButtonScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void updateTableView() {
        // Clear existing items in the table
        borrowingsTableView.getItems().clear();

        // Add books to the table
        borrowingsTableView.getItems().addAll(borrowings);
    }

    @FXML
    private void returnBookButtonClick() {

        Borrowings selectedBorrowing = borrowingsTableView.getSelectionModel().getSelectedItem();

        // Check if a borrowing is selected
        if (selectedBorrowing != null) {
            // Remove the selected borrowing from the list
            borrowings.remove(selectedBorrowing);

            // Update the TableView to reflect the changes
            updateTableView();

            for (Book book : books) {
                if (book.getISBN().equals(selectedBorrowing.getBook().getISBN())) {
                    int numberOfCopies = book.getNumberOfCopies();
                    book.setNumberOfCopies(numberOfCopies + 1);
                    break;
                }
            }

            for (User user : users) {
                if (user.getIDNumber().equals(selectedBorrowing.getUser().getIDNumber())) {
                    user.setNumBooksBorrowed(user.getNumBooksBorrowed() - 1);
                }
            }

            // Serialize and save the updated lists to the files
            UserDataManager.serializeUsers(users, UsersFilePath);
            BorrowingsManager.serializeBorrowings(borrowings, BorrowingsFilePath);
            BookDataManager.serializeBooks(books, BooksFilePath);
        }
        else {
            // Error alert!
            showErrorAlert("Please select a borrowing to remove!");
            return;
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
