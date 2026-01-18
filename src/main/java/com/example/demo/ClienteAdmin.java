package com.example.demo;

import java.util.Scanner;

public class ClienteAdmin {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int opcion;

		do {
			System.out.println("=== CLIENTE ADMIN ===");
			System.out.println("1. Listar camisetas por liga y temporada");
			System.out.println("2. Crear camiseta");
			System.out.println("3. Modificar camiseta");
			System.out.println("4. Eliminar camiseta");
			System.out.println("5. Ver stock por talla");
			System.out.println("6. Actualizar stock");
			System.out.println("7. Salir");

			opcion = sc.nextInt();
			sc.nextLine();

			try {
				switch (opcion) {

				case 1:
					System.out.print("Liga: ");
					String liga1 = sc.nextLine();

					System.out.print("Temporada (año): ");
					String temporada1 = sc.nextLine();

					String respuesta = ClienteApi.getCamisetas(liga1, temporada1);

					System.out.println(respuesta);
					break;

				case 2:
					System.out.print("Equipo: ");
					String equipo = sc.nextLine();

					System.out.print("Imagen: ");
					String imagen = sc.nextLine();

					System.out.print("Precio: ");
					double precio = sc.nextDouble();
					sc.nextLine();

					System.out.print("Temporada: ");
					String temporada2 = sc.nextLine();

					System.out.print("Liga: ");
					String liga2 = sc.nextLine();

					String json = String.format("""
							{
							  "equipo": "%s",
							  "imagen": "%s",
							  "precio": %.2f,
							  "temporada": "%s",
							  "liga": "%s",
							  "parche": false,
							  "nombreDorsal": "",
							  "numeroDorsal": 0
							}
							""", equipo, imagen, precio, temporada2, liga2);

					ClienteApi.crearCamiseta(json);
					System.out.println("Camiseta creada correctamente");
					break;

				case 3:
					System.out.print("ID de la camiseta: ");
					int id = sc.nextInt();
					sc.nextLine();

					ClienteApi.eliminarCamiseta(id);
					System.out.println("Camiseta eliminada correctamente");
					break;

				case 0:
					System.out.println("Saliendo del cliente admin...");
					break;

				default:
					System.out.println("Opción no válida");
					break;
				}

			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

		} while (opcion != 0);

		sc.close();
	}
}
