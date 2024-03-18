package ece.wisdomgate.MyObjects;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDataManager {

    public static List<User> deserializeUsers(String filePath) {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 7) {
                    String username = data[0];
                    String password = data[1];
                    String firstName = data[2];
                    String lastName = data[3];
                    String IDNumber = data[4];
                    String email = data[5];
                    Integer num_books_borrowed = Integer.valueOf(data[6]);

                    User user = new User(username, password, firstName, lastName, IDNumber, email, num_books_borrowed);
                    users.add(user);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void serializeUsers(List<User> users, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users) {
                String line = String.format("%s;%s;%s;%s;%s;%s;%d",
                        user.getUsername(), user.getPassword(), user.getFirstName(),
                        user.getLastName(), user.getIDNumber(), user.getEmail(), user.getNumBooksBorrowed());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., logging, displaying an error message)
        }
    }

}
