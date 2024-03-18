package ece.wisdomgate.MyObjects;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookDataManager {

    public static List<Book> deserializeBooks(String filePath) {
        List<Book> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 9) {
                    String title = data[0];
                    String author = data[1];
                    String publisher = data[2];
                    String category = data[3];
                    String ISBN = data[4];
                    int yearPublished = Integer.parseInt(data[5]);
                    int numberOfCopies = Integer.parseInt(data[6]);
                    int rating = Integer.parseInt(data[7]);
                    int users_rated = Integer.parseInt(data[8]);

                    Book book = new Book(title, author, publisher, category, ISBN, yearPublished, numberOfCopies, rating, users_rated);
                    books.add(book);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static void serializeBooks(List<Book> books, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                String line = String.format("%s;%s;%s;%s;%s;%d;%d;%d;%d",
                        book.getTitle(), book.getAuthor(), book.getPublisher(),
                        book.getCategory(), book.getISBN(), book.getYearPublished(), book.getNumberOfCopies(), book.getRating(), book.getUsersRated());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., logging, displaying an error message)
        }
    }

}