package com.misarchivos;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Vendedor> vendedores = cargarVendedores("salesmen_info.txt");
        Map<Integer, Producto> productos = cargarProductos("products.txt");
        calcularVentasVendedores(vendedores, productos);
        generarReporteVendedores(vendedores);
        generarReporteProductos(productos);
        System.out.println("Reportes generados con éxito.");
    }

    static class Vendedor {
        String nombre;
        long id;
        double totalVentas;

        Vendedor(String nombre, long id) {
            this.nombre = nombre;
            this.id = id;
            this.totalVentas = 0.0;
        }
    }

    static class Producto {
        int id;
        String nombre;
        double precio;
        int cantidadVendida;

        Producto(int id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.cantidadVendida = 0;
        }
    }

    public static List<Vendedor> cargarVendedores(String fileName) {
        List<Vendedor> vendedores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                vendedores.add(new Vendedor(datos[2] + " " + datos[3], Long.parseLong(datos[1])));
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo de vendedores: " + e.getMessage());
        }
        return vendedores;
    }

    public static Map<Integer, Producto> cargarProductos(String fileName) {
        Map<Integer, Producto> productos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] datos = line.split(";");
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                double precio = Double.parseDouble(datos[2]);
                productos.put(id, new Producto(id, nombre, precio));
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo de productos: " + e.getMessage());
        }
        return productos;
    }

    public static void calcularVentasVendedores(List<Vendedor> vendedores, Map<Integer, Producto> productos) {
        for (Vendedor vendedor : vendedores) {
            String fileName = "sales_" + vendedor.id + ".txt";
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] datos = line.split(";");
                    int productId = Integer.parseInt(datos[0]);
                    int cantidad = Integer.parseInt(datos[1]);
                    Producto producto = productos.get(productId);
                    if (producto != null) {
                        double venta = cantidad * producto.precio;
                        vendedor.totalVentas += venta;
                        producto.cantidadVendida += cantidad;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer archivo de ventas de " + vendedor.nombre + ": " + e.getMessage());
            }
        }
    }

    public static void generarReporteVendedores(List<Vendedor> vendedores) {
        vendedores.sort((v1, v2) -> Double.compare(v2.totalVentas, v1.totalVentas));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_vendedores.txt"))) {
            for (Vendedor vendedor : vendedores) {
                bw.write(vendedor.nombre + ";" + vendedor.totalVentas + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al escribir archivo de reporte de vendedores: " + e.getMessage());
        }
    }

    public static void generarReporteProductos(Map<Integer, Producto> productos) {
        List<Producto> listaProductos = new ArrayList<>(productos.values());
        listaProductos.sort((p1, p2) -> Integer.compare(p2.cantidadVendida, p1.cantidadVendida));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_productos.txt"))) {
            for (Producto producto : listaProductos) {
                bw.write(producto.nombre + ";" + producto.precio + ";" + producto.cantidadVendida + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al escribir archivo de reporte de productos: " + e.getMessage());
        }
    }
}
