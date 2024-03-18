package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.Book;
import ece.wisdomgate.MyObjects.BookDataManager;
import ece.wisdomgate.MyObjects.Borrowings;
import ece.wisdomgate.MyObjects.BorrowingsManager;
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
import java.util.Iterator;
import java.util.List;

public class adminLibraryController {

    @FXML
    private TableView<Book> bookTableView;

    private List<Book> books;
    private final String filePath = "src/main/resources/ece/wisdomgate/medialab/bookData.txt";

    private final String BorrowingsFilePath = "src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt";
    private final List<Borrowings> borrowings = BorrowingsManager.deserializeBorrowings(BorrowingsFilePath);

    @FXML
    private Button exitButton;

    @FXML
    private Button addNewBookButton;
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
    private TableColumn<Book, Integer> UsersRatedColumn;

    @FXML
    public void initialize() {
        // Load books from file
        books = BookDataManager.deserializeBooks(filePath);

        // Initialize TableColumn instances
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        publisherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublisher()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        ISBNColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        yearPublishedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getYearPublished()).asObject());
        numberOfCopiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberOfCopies()).asObject());
        ratingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRating()).asObject());
        UsersRatedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUsersRated()).asObject());
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
    private void addNewBookButtonClick() {
        // Load the add_book FXML file
        FXMLLoader addNewBookButtonLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/addBook.fxml"));
        try {
            // Load the add_book scene
            Scene addNewBookButtonScene = new Scene(addNewBookButtonLoader.load(), 800, 600);

            // Get the controller from the FXMLLoader
            addBookController addBookController = addNewBookButtonLoader.getController();

            // Get the selected book from the TableView
            Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();

            // Check if a book is selected for editing
            if (selectedBook != null) {
                // Editing an existing book
                addBookController.initialize(books, selectedBook);
            } else {
                // Adding a new book
                addBookController.initialize(books, null);
            }

            // Get the current stage
            Stage currentStage = (Stage) addNewBookButton.getScene().getWindow();

            // Set the add_book scene on the current stage
            currentStage.setScene(addNewBookButtonScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeSelectedBook() {
        // Get the selected book from the TableView
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();

        // Check if a book is selected
        if (selectedBook != null) {
            // Use Iterator to avoid ConcurrentModificationException
            Iterator<Borrowings> iterator = borrowings.iterator();
            while (iterator.hasNext()) {
                Borrowings borrowing = iterator.next();
                if (borrowing.getBook().getISBN().equals(selectedBook.getISBN())) {
                    iterator.remove(); // Remove borrowing from borrowings list
                }
            }

            // Remove the selected book from the list
            books.remove(selectedBook);

            // Update the TableView to reflect the changes
            updateTableView();

            // Serialize and save the updated list of books to the file
            BookDataManager.serializeBooks(books, filePath);
            BorrowingsManager.serializeBorrowings(borrowings, BorrowingsFilePath);
        } else {
            // Error alert!
            showErrorAlert("Please select a book to remove!");
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