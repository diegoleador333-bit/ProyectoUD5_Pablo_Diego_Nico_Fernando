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
			System.out.println("5. Mostrar camisetas");
			System.out.println("6. Mostrar usuarios");
			System.out.println("7. Mostrar pedidos");
			System.out.println("8. Salir");
			System.out.print("Elige una opción: ");

			opcion = sc.nextInt();
			sc.nextLine();

			try {
				switch (opcion) {

				case 1:
					// Pedimos los datos uno a uno
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

					// Convertimos los datos a json para el servidor
					String json = """
							{
							  "equipo": "%s",
							  "imagen": "%s",
							  "precio": %s,
							  "temporada": "%s",
							  "liga": "%s"
							}
							""".formatted(equipo, imagen, String.valueOf(precio), temporada, liga);

					// Le digo a ClienteApi que envie el json al metodo crearCamiseta
					ClienteApi.crearCamiseta(json);
					System.out.println("Camiseta creada");
					break;

				case 2:
					// Pedimos los datos
					System.out.print("ID camiseta: ");
					int idPrecio = sc.nextInt();

					System.out.print("Nuevo precio: ");
					double nuevoPrecio = sc.nextDouble();

					// Envio a cambiarPrecio los valores idPrecio y nuevoPrecio
					ClienteApi.cambiarPrecio(idPrecio, nuevoPrecio);
					System.out.println("Precio actualizado");
					break;

				case 3:
					System.out.print("ID camiseta: ");
					int idEliminar = sc.nextInt();

					// Envio al metodo eliminarCamiseta el idEliminar
					ClienteApi.eliminarCamiseta(idEliminar);
					System.out.println("Camiseta eliminada");
					break;

				case 4:

					System.out.print("ID camiseta: ");
					int idStock = sc.nextInt();

					// Enseñamos el stock y mandamos el id al metodo verStock
					String stock = ClienteApi.verStock(idStock);
					System.out.println(stock);
					break;
				}

			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

		} while (opcion != 8);

		sc.close();
	}
}
