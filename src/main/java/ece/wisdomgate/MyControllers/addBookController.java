package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.Book;
import ece.wisdomgate.MyObjects.BookDataManager;
import ece.wisdomgate.MyObjects.Borrowings;
import ece.wisdomgate.MyObjects.BorrowingsManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class addBookController {

    @FXML
    private TextField titleTextField;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField publisherTextField;
    @FXML
    private TextField ISBNTextField;
    @FXML
    private TextField yearPublishedTextField;
    @FXML
    private TextField categoryTextField;
    @FXML
    private TextField numberOfCopiesTextField;

    @FXML
    private Button addButton;

    private List<Book> books;
    private Book selectedBook;
    private final String filePath = "src/main/resources/ece/wisdomgate/medialab/bookData.txt";
    private final String BorrowingsFilePath = "src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt";
    private final List<Borrowings> borrowings = BorrowingsManager.deserializeBorrowings(BorrowingsFilePath);


    // Updated initialize method to handle both adding and editing
    public void initialize(List<Book> books, Book selectedBook) {
        this.books = books;
        this.selectedBook = selectedBook;

        if (selectedBook != null) {
            // If selectedBook is not null, populate the fields for editing
            populateFields(selectedBook);
        }
    }

    private void populateFields(Book book) {
        titleTextField.setText(book.getTitle());
        authorTextField.setText(book.getAuthor());
        publisherTextField.setText(book.getPublisher());
        ISBNTextField.setText(book.getISBN());
        yearPublishedTextField.setText(String.valueOf(book.getYearPublished()));
        categoryTextField.setText(book.getCategory());
        numberOfCopiesTextField.setText(String.valueOf(book.getNumberOfCopies()));
    }

    @FXML
    private void addBook() {
        // Get the details from the input fields
        String title = titleTextField.getText();
        String author = authorTextField.getText();
        String publisher = publisherTextField.getText();
        String ISBN = ISBNTextField.getText();
        int yearPublished = Integer.parseInt(yearPublishedTextField.getText());
        String category = categoryTextField.getText();
        int numberOfCopies = Integer.parseInt(numberOfCopiesTextField.getText());

        if (selectedBook != null) {
            for (Book book : books) {
                if (book.getISBN().equals(selectedBook.getISBN())) {
                    for (Borrowings borrowing : borrowings) {
                        if (borrowing.getBook().getISBN().equals(selectedBook.getISBN())) {
                            borrowing.getBook().setTitle(title);
                            borrowing.getBook().setISBN(ISBN);
                            borrowing.getBook().setCategory(category);

                            break; // No need to continue searching once the borrowing is found! :)
                        }
                    }
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setPublisher(publisher);
                    book.setISBN(ISBN);
                    book.setYearPublished(yearPublished);
                    book.setCategory(category);
                    book.setNumberOfCopies(numberOfCopies);

                    break; // No need to continue searching once the book is found! :)
                }
            }
        }
        else {
            // Create a new book or update the existing one if editing
            Book newBook = new Book(title, author, publisher, category, ISBN, yearPublished, numberOfCopies);
            // Add the new book to the list
            books.add(newBook);
        }

        // Serialize and save the updated list of book info to the files
        BookDataManager.serializeBooks(books, filePath);
        BorrowingsManager.serializeBorrowings(borrowings, BorrowingsFilePath);

        // Switch back to the adminLibrary page
        switchToAdminLibraryPage();
    }

    @FXML
    private void cancel() {
        // Switch back to the adminLibrary page without changing anything
        switchToAdminLibraryPage();
    }

    private void switchToAdminLibraryPage() {
        // Load the admin_library FXML file
        FXMLLoader adminLibraryLoader = new FXMLLoader(getClass().getResource("/ece/wisdomgate/adminLibrary.fxml"));
        try {
            // Load the admin_library scene
            Scene adminLibraryScene = new Scene(adminLibraryLoader.load(), 800, 600);

            // Get the current stage
            Stage currentStage = (Stage) addButton.getScene().getWindow();

            // Get the controller from the FXMLLoader
            adminLibraryController adminLibraryController = adminLibraryLoader.getController();

            // Pass the list of books to BookController
            adminLibraryController.initialize();

            // Set the admin_library scene on the current stage
            currentStage.setScene(adminLibraryScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
