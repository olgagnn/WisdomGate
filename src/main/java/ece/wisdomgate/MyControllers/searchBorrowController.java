package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class searchBorrowController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField yearField;
    @FXML
    private Button exitButton;

    @FXML
    private TableView<Book> searchResultsTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, String> ISBNColumn;
    @FXML
    private TableColumn<Book, Integer> yearPublishedColumn;
    @FXML
    private TableColumn<Book, Integer> numberOfCopiesColumn;
    @FXML
    private TableColumn<Book, Integer> ratingColumn;
    @FXML
    private TableColumn<Book, Integer> usersRatedColumn;

    @FXML
    private Label borrowDateLabel;
    @FXML
    private Label returnDateLabel;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    @FXML
    private void initialize() {
        // Initialize TableColumn instances
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        publisherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublisher()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        ISBNColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        yearPublishedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getYearPublished()).asObject());
        numberOfCopiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberOfCopies()).asObject());
        ratingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRating()).asObject());
        usersRatedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUsersRated()).asObject());

        // Set defaults
        borrowDate = LocalDate.now();
        borrowDateLabel.setText("Borrow Date: " + borrowDate);
        updateReturnDateLabel(borrowDate);

        // Event handler to open the comments dialog when a row is clicked
        searchResultsTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Book book = row.getItem();
                    openCommentsDialog(book);
                }
            });
            return row;
        });
    }

    private void updateReturnDateLabel(LocalDate borrowDate) {

        returnDate = borrowDate.plusDays(5); // 5 days borrowing period

        returnDateLabel.setText("Return Date:  " + returnDate);
    }

    private void openCommentsDialog(Book book) {
        try {
            // Load the dialog FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/bookComments.fxml"));
            Parent root = loader.load();

            // Get the controller
            bookCommentsController bookCommentsController = loader.getController();

            // Retrieve comments for the book
            List<BookComment> comments = BookCommentManager.deserializeComments("src/main/resources/ece/wisdomgate/medialab/commentsData.txt");

            // Filter comments for the selected book
            List<BookComment> bookComments = comments.stream()
                    .filter(comment -> comment.getBook().getISBN().equals(book.getISBN()))
                    .collect(Collectors.toList());

            // Set book comments in the dialog
            bookCommentsController.setComments(getCommentsAsString(bookComments));

            // Create the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Book Comments");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));

            // Show the dialog
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCommentsAsString(List<BookComment> comments) {
        if (comments.isEmpty()) {
            return "No comments";
        } else {
            return comments.stream()
                    .map(comment -> comment.getUser().getUsername() + ": " + comment.getComment())
                    .collect(Collectors.joining("\n"));
        }
    }

    @FXML
    private void searchButtonClick() {
        // Retrieve user input
        String searchTitle = titleField.getText();
        String searchAuthor = authorField.getText();
        String searchYear = yearField.getText();

        // Search
        List<Book> searchResults = searchBooks(searchTitle, searchAuthor, searchYear);

        displaySearchResults(searchResults);
    }

    private List<Book> searchBooks(String title, String author, String year) {
        // Deserialize existing books from the file
        List<Book> books = BookDataManager.deserializeBooks("src/main/resources/ece/wisdomgate/medialab/bookData.txt");

        // Filter books based on search criteria
        List<Book> searchResults = books.stream()
                .filter(book -> title.isEmpty() || book.getTitle().toLowerCase().contains(title.trim().toLowerCase()))
                .filter(book -> author.isEmpty() || book.getAuthor().toLowerCase().contains(author.trim().toLowerCase()))
                .filter(book -> year.isEmpty() || String.valueOf(book.getYearPublished()).contains(year.trim()))
                .collect(Collectors.toList());

        return searchResults;
    }

    private void displaySearchResults(List<Book> searchResults) {
        // Clear existing items in the table
        searchResultsTable.getItems().clear();

        // Add search results to the table
        searchResultsTable.getItems().addAll(searchResults);
    }

    @FXML
    private void borrowButtonClick() {
        // Check if a book is selected
        Book selectedBook = searchResultsTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showErrorAlert("Please select a book to borrow!");
            return;
        }

        // Check if there are available copies
        if (selectedBook.getNumberOfCopies() <= 0) {
            showErrorAlert("No available copies of this book!");
            return;
        }

        // Check if user has borrowed less than 2 books
        Integer booksBorrowed = UserSession.getLoggedInUser().getNumBooksBorrowed();
        User loggedInUser = UserSession.getLoggedInUser();

        if (booksBorrowed >= 2) {
            showErrorAlert("You have already reached the maximum limit of 2 books!");
            return;
        }

        // Update the number of copies
        selectedBook.setNumberOfCopies(selectedBook.getNumberOfCopies() - 1);

        // Update the list of books
        List<Book> books = BookDataManager.deserializeBooks("src/main/resources/ece/wisdomgate/medialab/bookData.txt");
        for (Book book : books) {
            if (selectedBook.getISBN().equals(book.getISBN())) {
                book.setNumberOfCopies(book.getNumberOfCopies() - 1);
                break; // No need to continue searching once the book is found! :)
            }
        }

        // Update the user's number of books borrowed
        List<User> users = UserDataManager.deserializeUsers("src/main/resources/ece/wisdomgate/medialab/userData.txt");
        for (User user : users) {
            if (user.getIDNumber().equals(loggedInUser.getIDNumber())) {
                user.setNumBooksBorrowed(user.getNumBooksBorrowed() + 1);
                break; // No need to continue searching once the user is found! :)
            }
        }

        // Update the list of borrowings
        Borrowings newBorrowing = new Borrowings(loggedInUser, selectedBook, borrowDate, returnDate);
        List<Borrowings> borrowings = BorrowingsManager.deserializeBorrowings("src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt");
        borrowings.add(newBorrowing);

        // Serialization
        UserDataManager.serializeUsers(users, "src/main/resources/ece/wisdomgate/medialab/userData.txt");
        BorrowingsManager.serializeBorrowings(borrowings, "src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt");
        BookDataManager.serializeBooks(books, "src/main/resources/ece/wisdomgate/medialab/bookData.txt");

        showSuccessAlert("Book borrowed successfully!");
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
}