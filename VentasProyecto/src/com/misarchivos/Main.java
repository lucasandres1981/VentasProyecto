package com.misarchivos;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Cargar la lista de vendedores desde un archivo.
        List<Vendedor> vendedores = cargarVendedores("salesmen_info.txt");

        // Cargar los productos desde un archivo y almacenarlos en un mapa con el ID del producto como clave.
        Map<Integer, Producto> productos = cargarProductos("products.txt");

        // Calcular las ventas totales de cada vendedor basado en los archivos de ventas individuales.
        calcularVentasVendedores(vendedores, productos);

        // Generar el reporte de vendedores ordenado por ventas.
        generarReporteVendedores(vendedores);

        // Generar el reporte de productos ordenado por cantidad vendida.
        generarReporteProductos(productos);
        
        // Mostrar resultados en consola.
        imprimirResultados(vendedores, productos);     
        
        // Mostrar mensaje en consola de que los reportes se han generado con éxito.
        System.out.println("Reportes generados con éxito.");
    }

    // Clase que representa un Vendedor
    static class Vendedor {
        String nombre;
        long id;
        double totalVentas; // Total de ventas realizadas por el vendedor

        // Constructor de la clase Vendedor
        Vendedor(String nombre, long id) {
            this.nombre = nombre;
            this.id = id;
            this.totalVentas = 0.0; // Se inicializa el total de ventas en 0
        }
    }

    // Clase que representa un Producto
    static class Producto {
        int id;
        String nombre;
        double precio;
        int cantidadVendida; // Cantidad de unidades vendidas

        // Constructor de la clase Producto
        Producto(int id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.cantidadVendida = 0; // Se inicializa la cantidad vendida en 0
        }
    }

    // Método que carga los vendedores desde un archivo de texto
    public static List<Vendedor> cargarVendedores(String fileName) {
        List<Vendedor> vendedores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Leer cada línea del archivo y procesar los datos
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                // Crear un nuevo Vendedor con los datos del archivo y agregarlo a la lista
                vendedores.add(new Vendedor(datos[2] + " " + datos[3], Long.parseLong(datos[1])));
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo de vendedores: " + e.getMessage());
        }
        return vendedores;
    }

    // Método que carga los productos desde un archivo de texto
    public static Map<Integer, Producto> cargarProductos(String fileName) {
        Map<Integer, Producto> productos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Leer cada línea del archivo y procesar los datos
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                double precio = Double.parseDouble(datos[2]);
                // Crear un nuevo Producto y agregarlo al mapa de productos
                productos.put(id, new Producto(id, nombre, precio));
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo de productos: " + e.getMessage());
        }
        return productos;
    }

    // Método que calcula las ventas de cada vendedor
    public static void calcularVentasVendedores(List<Vendedor> vendedores, Map<Integer, Producto> productos) {
        // Procesar las ventas de cada vendedor
        for (Vendedor vendedor : vendedores) {
            String fileName = "sales_" + vendedor.id + ".txt"; // Nombre del archivo de ventas del vendedor
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                // Leer cada línea del archivo de ventas y procesar las transacciones
                while ((line = br.readLine()) != null) {
                    String[] datos = line.split(";");
                    int productId = Integer.parseInt(datos[0]); // ID del producto vendido
                    int cantidad = Integer.parseInt(datos[1]); // Cantidad vendida
                    Producto producto = productos.get(productId); // Buscar el producto en el mapa
                    if (producto != null) {
                        double venta = cantidad * producto.precio; // Calcular el valor de la venta
                        vendedor.totalVentas += venta; // Actualizar el total de ventas del vendedor
                        producto.cantidadVendida += cantidad; // Actualizar la cantidad vendida del producto
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer archivo de ventas de " + vendedor.nombre + ": " + e.getMessage());
            }
        }
    }

    // Método que genera el reporte de ventas por vendedor
    public static void generarReporteVendedores(List<Vendedor> vendedores) {
        // Ordenar los vendedores por total de ventas (de mayor a menor)
        vendedores.sort((v1, v2) -> Double.compare(v2.totalVentas, v1.totalVentas));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_vendedores.csv"))) {
            // Escribir cada vendedor en el archivo de reporte
            for (Vendedor vendedor : vendedores) {
                bw.write(vendedor.nombre + ";" + "$" + vendedor.totalVentas + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al escribir archivo de reporte de vendedores: " + e.getMessage());
        }
    }

    // Método que genera el reporte de productos más vendidos
    public static void generarReporteProductos(Map<Integer, Producto> productos) {
        // Convertir los productos en una lista y ordenarlos por cantidad vendida (de mayor a menor)
        List<Producto> listaProductos = new ArrayList<>(productos.values());
        listaProductos.sort((p1, p2) -> Integer.compare(p2.cantidadVendida, p1.cantidadVendida));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_productos.csv"))) {
            // Escribir cada producto en el archivo de reporte
            for (Producto producto : listaProductos) {
                bw.write(producto.nombre + ";" + producto.precio + ";" + producto.cantidadVendida + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al escribir archivo de reporte de productos: " + e.getMessage());
        }
    }
    

    public static void imprimirResultados(List<Vendedor> vendedores, Map<Integer, Producto> productos) {
        System.out.println("----- Reporte de Ventas por Vendedor -----");
        for (Vendedor vendedor : vendedores) {
            System.out.println("Vendedor: " + vendedor.nombre);
            System.out.println("Total Ventas: $" + vendedor.totalVentas);
            System.out.println("----------------------------------------");
        }

        System.out.println("----- Reporte de Productos más Vendidos -----");
        for (Producto producto : productos.values()) {
            System.out.println("Producto: " + producto.nombre);
            System.out.println("Cantidad Vendida: " + producto.cantidadVendida);
            System.out.println("--------------------------------------------");
        }
    }
    
}
