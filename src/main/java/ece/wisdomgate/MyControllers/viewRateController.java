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

public class viewRateController {

    @FXML
    private Button exitButton;

    @FXML
    private TableView<Borrowings> borrowingsTable;
    @FXML
    private TableColumn<Borrowings, String> bookTitleColumn;
    @FXML
    private TableColumn<Borrowings, String> bookISBNColumn;
    @FXML
    private TableColumn<Borrowings, LocalDate> borrowDateColumn;
    @FXML
    private TableColumn<Borrowings, LocalDate> returnDateColumn;
    @FXML
    private ComboBox<Integer> ratingComboBox;
    @FXML
    private TextArea commentsTextArea;

    @FXML
    private void initialize() {
        // Deserialize borrowings from the file
        List<Borrowings> allBorrowings = BorrowingsManager.deserializeBorrowings("src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt");

        // Retrieve the IDNumber of the logged-in user
        String loggedInUserID = UserSession.getLoggedInUser().getIDNumber();

        // Filter borrowings based on the IDNumber of the logged-in user
        List<Borrowings> userBorrowings = filterBorrowingsByUserID(allBorrowings, loggedInUserID);

        // Initialize TableColumn instances
        bookTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        bookISBNColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBook().getISBN()));
        borrowDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBorrowDate()));
        returnDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReturnDate()));

        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);

        // Display user borrowings in the table
        borrowingsTable.getItems().addAll(userBorrowings);
    }

    private List<Borrowings> filterBorrowingsByUserID(List<Borrowings> borrowings, String userID) {
        return borrowings.stream()
                .filter(borrowing -> borrowing.getUser().getIDNumber().equals(userID))
                .toList();
    }

    @FXML
    private void rateButtonClick() {
        Borrowings selectedBorrowing = borrowingsTable.getSelectionModel().getSelectedItem();
        if (selectedBorrowing != null) {
            // Get selected rating and comments
            Integer rating = ratingComboBox.getValue();
            String comments = commentsTextArea.getText();

            if (rating == null) {
                showErrorAlert("Please provide a rating for the selected book!");
                return;
            }

            // Check if comments include ;
            if (comments.contains(";")) {
                showErrorAlert("Comments not include the \";\" character!");
                return;
            }

            // Check if comments include end of line character
            if (comments.contains("\n")) {
                showErrorAlert("Comments must be only one line");
                return;
            }

            // Add user's comment
            User loggedInUser = UserSession.getLoggedInUser();
            BookComment bookComment = new BookComment(loggedInUser, selectedBorrowing.getBook(), comments);

            // Deserialize existing books from the file
            List<BookComment> bookComments = BookCommentManager.deserializeComments("src/main/resources/ece/wisdomgate/medialab/commentsData.txt");

            bookComments.add(bookComment);

            // Update the book's rating in the borrowing object
            selectedBorrowing.getBook().rateBook(rating);

            // Deserialize existing books from the file
            List<Book> books = BookDataManager.deserializeBooks("src/main/resources/ece/wisdomgate/medialab/bookData.txt");

            // Find the selected book in the list
            for (Book book : books) {
                if (book.getISBN().equals(selectedBorrowing.getBook().getISBN())) {
                    // Update the selected book's rating
                    book.rateBook(rating);

                    break; // No need to continue searching once the book is found! :)
                }
            }

            // Serialize the updated list of books back to the file
            BookDataManager.serializeBooks(books, "src/main/resources/ece/wisdomgate/medialab/bookData.txt");
            BookCommentManager.serializeComments(bookComments,"src/main/resources/ece/wisdomgate/medialab/commentsData.txt");

            showSuccessAlert("Book rating and comment added successfully!");

            commentsTextArea.clear();

            ratingComboBox.setValue(null);

        } else {
            showErrorAlert("Please select a borrowing to rate and comment.");
        }
    }

    @FXML
    private void exitButtonClick() {

        FXMLLoader exitButtonLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/user.fxml"));
        try {

            Scene exitButtonScene = new Scene(exitButtonLoader.load(), 800, 600);

            Stage currentStage = (Stage) exitButton.getScene().getWindow();

            currentStage.setScene(exitButtonScene);
        } catch (IOException e) {
            e.printStackTrace();
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
}
