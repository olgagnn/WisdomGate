package ece.wisdomgate.MyObjects;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BorrowingsManager {

    public static List<Borrowings> deserializeBorrowings(String filePath) {
        List<Borrowings> borrowings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 7) { // Ensure at least 6 fields are present
                    String username = data[0].trim();
                    String IDNumber = data[1].trim();
                    String bookTitle = data[2].trim();
                    String bookISBN = data[3].trim();
                    String bookCategory = data[4].trim();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate borrowDate = LocalDate.parse(data[5].trim(), formatter);
                    LocalDate returnDate = LocalDate.parse(data[6].trim(), formatter);

                    User user = new User(username, IDNumber);
                    Book book = new Book(bookTitle, bookISBN, bookCategory);

                    Borrowings borrowing = new Borrowings(user, book, borrowDate, returnDate);
                    borrowings.add(borrowing);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return borrowings;
    }

    public static void serializeBorrowings(List<Borrowings> borrowings, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Borrowings borrowing : borrowings) {
                String line = String.format("%s;%s;%s;%s;%s;%s;%s",
                        borrowing.getUser().getUsername(),
                        borrowing.getUser().getIDNumber(),
                        borrowing.getBook().getTitle(),
                        borrowing.getBook().getISBN(),
                        borrowing.getBook().getCategory(),
                        borrowing.getBorrowDate(),
                        borrowing.getReturnDate()

                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
