package com.misarchivos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

    public static void main(String[] args) {
        int numberOfSalesmen = 5;  // Number of salesmen to be generated
        int numberOfProducts = 10; // Number of products to be generated
        
        // Generate product information
        createProductsFile(numberOfProducts);
        
        // Generate salesman information and random sales files
        createSalesManInfoFile(numberOfSalesmen);
        
        System.out.println("Test files generated successfully.");
    }

    /**
     * Generates a file with random sales for a specific salesman.
     *
     * @param id Salesman ID
     * @param numberOfSales Number of sales to generate
     */
    public static void createSalesMenFile(long id, int numberOfSales) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales_" + id + ".txt"))) {
            Random rand = new Random();
            // Generate random sales
            for (int i = 0; i < numberOfSales; i++) {
                int productId = rand.nextInt(10) + 1;  // Product ID between 1 and 10
                int quantitySold = rand.nextInt(50) + 1;  // Quantity sold between 1 and 50
                // Write each sale to the file
                writer.write(productId + ";" + quantitySold + ";\n");
            }
        } catch (IOException e) {
            System.out.println("Error creating sales file for salesman with ID " + id + ": " + e.getMessage());
        }
    }

    /**
     * Generates a file with product information.
     *
     * @param productsCount Number of products to generate
     */
    public static void createProductsFile(int productsCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            Random rand = new Random();
            // List of product names
            String[] productNames = {"Organic Coffee", "Powdered Milk", "Sliced Bread", "Double Cream Cheese", "York Ham"};
            
            // Generate product information
            for (int i = 0; i < productsCount; i++) {
                int productId = i + 1;  // Product ID
                String productName = productNames[i % productNames.length];  // Product name
                double price = 10.0 + (100.0 - 10.0) * rand.nextDouble();  // Random price between 10 and 100
                // Write each product to the file
                writer.write(productId + ";" + productName + ";" + String.format("%.2f", price) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error creating products file: " + e.getMessage());
        }
    }

    /**
     * Generates a file with salesman information and, for each salesman, generates a sales file.
     *
     * @param salesmanCount Number of salesmen to generate
     */
    public static void createSalesManInfoFile(int salesmanCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("salesmen_info.txt"))) {
            // Lists of salesman names and surnames
            String[] names = {"Carlos", "Alejandro", "Maria", "Luis", "Ana"};
            String[] surnames = {"Olivares", "Gomez", "Arango", "Diaz", "Martinez"};
            Random rand = new Random();
            
            // Generate salesman information
            for (int i = 0; i < salesmanCount; i++) {
                String docType = "CC";  // Document type
                long docNumber = 100000000L + rand.nextInt(900000000); // Unique ID for each salesman
                String name = names[rand.nextInt(names.length)];  // Random first name
                String surname = surnames[rand.nextInt(surnames.length)];  // Random surname
                
                // Write salesman information to the file
                writer.write(docType + ";" + docNumber + ";" + name + ";" + surname + "\n");
                
                // Generate random sales file for this salesman
                createSalesMenFile(docNumber, 5);  // Generate 5 random sales per salesman
            }
        } catch (IOException e) {
            System.out.println("Error creating salesman information file: " + e.getMessage());
        }
    }
}

