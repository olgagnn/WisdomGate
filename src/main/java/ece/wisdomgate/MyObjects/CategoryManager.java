package ece.wisdomgate.MyObjects;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {

    public static List<Category> deserializeCategories(String filePath) {
        List<Category> categories = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String categoryName = line.trim();
                Category category = new Category(categoryName);
                categories.add(category);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public static void serializeCategories(List<Category> categories, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < categories.size(); i++) {
                writer.write(categories.get(i).getCategory());
                if (i < categories.size() - 1) {
                    writer.write(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
