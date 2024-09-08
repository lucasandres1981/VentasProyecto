package com.misarchivos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

    public static void main(String[] args) {
        int numberOfSalesmen = 5;  // Número de vendedores
        int numberOfProducts = 10; // Número de productos
        
        // Generar información de productos
        createProductsFile(numberOfProducts);
        
        // Generar informacion de vendedores y archivs de ventas aleatorios
        createSalesManInfoFile(numberOfSalesmen);
        
        System.out.println("Archivos de prueba generados con éxito.");
    }

    // Método para generar archivos de ventas para cada vendedor
    public static void createSalesMenFile(long id, int numberOfSales) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales_" + id + ".txt"))) {
            Random rand = new Random();
            for (int i = 0; i < numberOfSales; i++) {
                int productId = rand.nextInt(10) + 1;  // ID de producto entre 1 y 10
                int quantitySold = rand.nextInt(50) + 1;  // Cantidad entre 1 y 50
                writer.write(productId + ";" + quantitySold + ";\n");
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivo de ventas para el vendedor con ID " + id + ": " + e.getMessage());
        }
    }

    // Método para crear un archivo con información de productos
    public static void createProductsFile(int productsCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            Random rand = new Random();
            String[] productNames = {"Café Orgánico", "Leche en Polvo", "Pan Tajado", "Queso Doble Crema", "Jamón York"};
            for (int i = 0; i < productsCount; i++) {
                int productId = i + 1;
                String productName = productNames[i % productNames.length];
                double price = 10.0 + (100.0 - 10.0) * rand.nextDouble(); // Precio aleatorio entre 10 y 100
                writer.write(productId + ";" + productName + ";" + String.format("%.2f", price) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivo de productos: " + e.getMessage());
        }
    }

    // Método para crear un archivo con información de vendedores y generar sus archivos de ventas
    public static void createSalesManInfoFile(int salesmanCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("salesmen_info.txt"))) {
            String[] names = {"Carlos", "Alejandro", "Maria", "Luis", "Ana"};
            String[] surnames = {"Olivares", "Gomez", "Arango", "Diaz", "Martinez"};
            Random rand = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                String docType = "CC";
                long docNumber = 100000000L + rand.nextInt(900000000); // ID único para cada vendedor
                String name = names[rand.nextInt(names.length)];
                String surname = surnames[rand.nextInt(surnames.length)];
                
                // Escribir información del vendedor en el archivo
                writer.write(docType + ";" + docNumber + ";" + name + ";" + surname + "\n");
                
                // Generar archivo de ventas aleatorias para este vendedor
                createSalesMenFile(docNumber, 5);  // Crea 5 ventas aleatorias por vendedor
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivo de información de vendedores: " + e.getMessage());
        }
    }
}
