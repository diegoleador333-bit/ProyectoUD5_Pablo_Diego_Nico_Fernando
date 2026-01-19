package com.example.demo.ClienteJava;

import java.util.Scanner;

public class ClienteAdmin {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("CLIENTE ADMIN");
			System.out.println("1. Crear camiseta");
			System.out.println("2. Cambiar precio camiseta");
			System.out.println("3. Eliminar camiseta");
			System.out.println("4. Ver stock por talla");
			System.out.println("5. Salir");
			System.out.print("Elige una opción: ");

			opcion = sc.nextInt();
			sc.nextLine();

			try {
				switch (opcion) {

				case 1:
					System.out.print("Equipo: ");
					String equipo = sc.nextLine();

					System.out.print("Imagen: ");
					String imagen = sc.nextLine();

					System.out.print("Precio: ");
					double precio = sc.nextDouble();
					sc.nextLine();

					System.out.print("Temporada (2000-2005...): ");
					String temporada = sc.nextLine();

					System.out.print("Liga: ");
					String liga = sc.nextLine();

					String json = """
							{
							  "equipo": "%s",
							  "imagen": "%s",
							  "precio": %.2f,
							  "temporada": "%s",
							  "liga": "%s"
							}
							""".formatted(equipo, imagen, precio, temporada, liga);

					ClienteApi.crearCamiseta(json);
					System.out.println("Camiseta creada");
					break;

				case 2:
					System.out.print("ID camiseta: ");
					int idPrecio = sc.nextInt();

					System.out.print("Nuevo precio: ");
					double nuevoPrecio = sc.nextDouble();

					ClienteApi.cambiarPrecio(idPrecio, nuevoPrecio);
					System.out.println("Precio actualizado");
					break;

				case 3:
					System.out.print("ID camiseta: ");
					int idEliminar = sc.nextInt();

					ClienteApi.eliminarCamiseta(idEliminar);
					System.out.println("Camiseta eliminada");
					break;

				case 4:
					System.out.print("ID camiseta: ");
					int idStock = sc.nextInt();

					String stock = ClienteApi.verStock(idStock);
					System.out.println(stock);
					break;
				}

			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

		} while (opcion != 5);

		sc.close();
	}
}
