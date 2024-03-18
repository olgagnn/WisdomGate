package ece.wisdomgate.MyControllers;

import ece.wisdomgate.MyObjects.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryController {

    private final String categoriesFilePath = "src/main/resources/ece/wisdomgate/medialab/categoriesData.txt";
    private final String booksFilePath = "src/main/resources/ece/wisdomgate/medialab/bookData.txt";
    private final String BorrowingsFilePath = "src/main/resources/ece/wisdomgate/medialab/borrowingsData.txt";
    private final String UsersFilePath = "src/main/resources/ece/wisdomgate/medialab/userData.txt";
    private final List<Book> books = BookDataManager.deserializeBooks(booksFilePath);
    private final List<User> users = UserDataManager.deserializeUsers(UsersFilePath);
    private final List<Category> categories = CategoryManager.deserializeCategories(categoriesFilePath);
    private final List<Borrowings> borrowings = BorrowingsManager.deserializeBorrowings(BorrowingsFilePath);

    @FXML
    private TableView<Category> categoryTableView;
    @FXML
    private TableColumn<Category, String> categoryNameColumn;

    @FXML
    private TableView<Book> bookTableView;
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
    private TextField categoryField;

    @FXML
    private Button exitButton;

    @FXML
    private void initialize() {
        // Initialize TableColumn instances
        categoryNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));

        // Load existing categories using ObservableList
        ObservableList<Category> observableCategories = FXCollections.observableArrayList(categories);
        categoryTableView.setItems(observableCategories);

        // Add a listener to update the bookTableView when a category is selected
        categoryTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateBooksInSelectedCategory(newValue);
            }
        });

        // Display books in the table
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
        bookTableView.setItems(observableBooks);

        // Set cell value factories for book table columns
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
        publisherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPublisher()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        ISBNColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        yearPublishedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getYearPublished()).asObject());
        numberOfCopiesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberOfCopies()).asObject());
        ratingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRating()).asObject());
    }

    private void updateBooksInSelectedCategory(Category selectedCategory) {
        // Clear existing items in the bookTableView
        bookTableView.getItems().clear();

        // Get the books associated with the selected category
        List<Book> associatedBooks = getBooksForCategory(selectedCategory);

        // Add associated books to the bookTableView
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(associatedBooks);
        bookTableView.setItems(observableBooks);
    }

    private List<Book> getBooksForCategory(Category category) {
        List<Book> associatedBooks = new ArrayList<>();

        // Iterate through all books and find those associated with the selected category
        for (Book book : books) {
            if (category.getCategory().equals(book.getCategory())) {
                associatedBooks.add(book);
            }
        }

        return associatedBooks;
    }

    @FXML
    private void addCategory() {
        String newCategory = categoryField.getText().trim();
        if (!newCategory.isEmpty()) {
            // Create a new category
            Category category = new Category(newCategory);

            // Add the category to the UI
            categories.add(category);

            // Serialize the updated categories list
            CategoryManager.serializeCategories(categories, categoriesFilePath);

            // Refresh the categoryTableView
            ObservableList<Category> observableCategories = FXCollections.observableArrayList(categories);
            categoryTableView.setItems(observableCategories);

            // Clear the input field
            categoryField.clear();
        }
    }

    @FXML
    private void removeCategory() {
        // Get the selected category
        Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Remove associated books from the list
            List<Book> booksToRemove = getBooksForCategory(selectedCategory);
            List<Borrowings> borrowingsToRemove = getBorrowingsForCategory(selectedCategory);

            // Remove borrowings associated with the removed books
            borrowings.removeAll(borrowingsToRemove);

            books.removeAll(booksToRemove);

            // Remove the selected category from the list
            categories.remove(selectedCategory);

            // Serialize the updated books list
            BookDataManager.serializeBooks(books, booksFilePath);

            // Serialize the updated categories list
            CategoryManager.serializeCategories(categories, categoriesFilePath);

            // Serialize the updated borrowings list
            BorrowingsManager.serializeBorrowings(borrowings, BorrowingsFilePath);

            // Refresh the categoryTableView
            ObservableList<Category> observableCategories = FXCollections.observableArrayList(categories);
            categoryTableView.setItems(observableCategories);

            // Refresh the bookTableView
            ObservableList<Book> observableBooks = FXCollections.observableArrayList(books);
            bookTableView.setItems(observableBooks);
        }
        else {
            // Error alert!
            showErrorAlert("Please select a user to remove!");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<Borrowings> getBorrowingsForCategory(Category category) {
        List<Borrowings> associatedBorrowings = new ArrayList<>();

        // Iterate through all books and find those associated with the selected category
        for (Borrowings borrowing : borrowings) {
            if (category.getCategory().equals(borrowing.getBook().getCategory())) {
                associatedBorrowings.add(borrowing);
            }
            for (User user : users) {
                if (user.getIDNumber().equals(borrowing.getUser().getIDNumber())) {
                    user.setNumBooksBorrowed(user.getNumBooksBorrowed() - 1);
                }
            }
        }

        // Serialize
        UserDataManager.serializeUsers(users, UsersFilePath);

        return associatedBorrowings;
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
}




