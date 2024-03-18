package ece.wisdomgate.MyObjects;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookCommentManager {

    public static List<BookComment> deserializeComments(String filePath) {
        List<BookComment> comments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 3) { // Ensure at least 3 fields are present
                    String username = data[0].trim();
                    String commentText = data[1].trim();
                    String bookISBN = data[2].trim();

                    User user = new User(username);
                    Book book = new Book(bookISBN); // Assuming Book has a constructor that takes an ISBN
                    BookComment comment = new BookComment(user, book, commentText);
                    comments.add(comment);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return comments;
    }

    public static void serializeComments(List<BookComment> comments, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (BookComment comment : comments) {
                String line = String.format("%s;%s;%s",
                        comment.getUser().getUsername(),
                                comment.getComment(),
                                comment.getBook().getISBN()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., logging, displaying an error message)
        }
    }

}
