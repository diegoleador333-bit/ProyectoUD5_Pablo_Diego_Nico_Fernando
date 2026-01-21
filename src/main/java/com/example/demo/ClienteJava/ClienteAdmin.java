package com.example.demo.ClienteJava;

import java.util.Scanner;
import com.example.demo.Stock.StockPorTalla;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class ClienteAdmin {

	static class Design {
		public static final String RESET = "\033[0m";
		public static final String RED = "\033[0;31m";
		public static final String GREEN = "\033[0;32m";
		public static final String YELLOW = "\033[0;33m";
		public static final String BLUE = "\033[0;34m";
		public static final String CYAN = "\033[0;36m";
		public static final String BOLD = "\033[1m";

		public static void printHeader(String titulo) {
			System.out.println(CYAN + "╔════════════════════════════════════════════╗");
			System.out.printf("║ %-42s ║%n", centerString(titulo, 42));
			System.out.println("╚════════════════════════════════════════════╝" + RESET);
		}

		public static void printSuccess(String msg) {
			System.out.println(GREEN + "✔ " + msg + RESET);
		}

		public static void printError(String msg) {
			System.out.println(RED + "✘ ERROR: " + msg + RESET);
		}

		public static String centerString(String s, int width) {
			int padSize = width - s.length();
			int padStart = s.length() + padSize / 2;
			s = String.format("%" + padStart + "s", s);
			return String.format("%-" + width + "s", s);
		}
	}

	private static final ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int opcion = 0;

		do {
			System.out.println("\n" + Design.BOLD + "--- MENÚ PRINCIPAL ---" + Design.RESET);
			System.out.println(Design.CYAN + "1." + Design.RESET + " Crear camiseta");
			System.out.println(Design.CYAN + "2." + Design.RESET + " Cambiar precio camiseta");
			System.out.println(Design.CYAN + "3." + Design.RESET + " Eliminar camiseta");
			System.out.println(Design.CYAN + "4." + Design.RESET + " Ver stock por talla");
			System.out.println("----------------------");
			System.out.println(Design.CYAN + "5." + Design.RESET + " Mostrar camisetas (Tabla)");
			System.out.println(Design.CYAN + "6." + Design.RESET + " Mostrar usuarios");
			System.out.println(Design.CYAN + "7." + Design.RESET + " Mostrar pedidos de usuario");
			System.out.println(Design.CYAN + "8." + Design.RESET + " Ver contenido pedidos");
			System.out.println(Design.RED + "9. Salir" + Design.RESET);
			System.out.print("\n👉 " + Design.BOLD + "Elige una opción: " + Design.RESET);

			try {
				String input = sc.nextLine();
				if (input.isEmpty())
					continue;
				opcion = Integer.parseInt(input);

				System.out.println();

				switch (opcion) {

				case 1:
					Design.printHeader("NUEVA CAMISETA");
					System.out.print("Equipo: ");
					String equipo = sc.nextLine();
					System.out.print("Imagen (URL): ");
					String imagen = sc.nextLine();
					System.out.print("Precio: ");
					double precio = Double.parseDouble(sc.nextLine());
					System.out.print("Temporada: ");
					String temporada = sc.nextLine();
					System.out.print("Liga: ");
					String liga = sc.nextLine();

					String json = """
							{
							  "equipo": "%s",
							  "imagen": "%s",
							  "precio": %s,
							  "temporada": "%s",
							  "liga": "%s"
							}
							""".formatted(equipo, imagen, String.valueOf(precio), temporada, liga);

					ClienteApi.crearCamiseta(json);
					Design.printSuccess("Camiseta creada correctamente.");
					break;

				case 2:
					Design.printHeader("ACTUALIZAR PRECIO");
					System.out.print("ID camiseta: ");
					int idPrecio = Integer.parseInt(sc.nextLine());
					System.out.print("Nuevo precio: ");
					double nuevoPrecio = Double.parseDouble(sc.nextLine());

					ClienteApi.cambiarPrecio(idPrecio, nuevoPrecio);
					Design.printSuccess("Precio actualizado.");
					break;

				case 3:
					Design.printHeader("ELIMINAR CAMISETA");
					System.out.print("ID camiseta a borrar: ");
					int idEliminar = Integer.parseInt(sc.nextLine());
					ClienteApi.eliminarCamiseta(idEliminar);
					Design.printSuccess("Registro eliminado.");
					break;

				case 4:
					Design.printHeader("GESTIÓN DE STOCK");
					System.out.print("ID camiseta: ");
					int idStock = Integer.parseInt(sc.nextLine());

					StockPorTalla stockObj = ClienteApi.verStockObjeto(idStock);

					if (stockObj == null) {
						Design.printError("No se encontró stock.");
						break;
					}

					System.out.println(Design.YELLOW + "\n📊 STOCK ACTUAL (ID: " + idStock + ")" + Design.RESET);
					System.out.println("┌──────┬──────┬──────┬──────┐");
					System.out.println("│  S   │  M   │  L   │  XL  │");
					System.out.println("├──────┼──────┼──────┼──────┤");
					System.out.printf("│ %-4d │ %-4d │ %-4d │ %-4d │%n", stockObj.getStockS(), stockObj.getStockM(),
							stockObj.getStockL(), stockObj.getStockXL());
					System.out.println("└──────┴──────┴──────┴──────┘");

					System.out.println("\n¿Deseas actualizar el stock? (1. Si / 2. No)");
					int respuesta = Integer.parseInt(sc.nextLine());

					if (respuesta == 1) {
						System.out.print("Nuevo stock S: ");
						stockObj.setStockS(Integer.parseInt(sc.nextLine()));
						System.out.print("Nuevo stock M: ");
						stockObj.setStockM(Integer.parseInt(sc.nextLine()));
						System.out.print("Nuevo stock L: ");
						stockObj.setStockL(Integer.parseInt(sc.nextLine()));
						System.out.print("Nuevo stock XL: ");
						stockObj.setStockXL(Integer.parseInt(sc.nextLine()));

						ClienteApi.actualizarStock(idStock, stockObj);
						Design.printSuccess("Inventario actualizado.");
					}
					break;

				case 5:
					Design.printHeader("CATÁLOGO DE CAMISETAS");
					String jsonCamisetas = ClienteApi.mostrarCamisetas();
					imprimirTablaCamisetas(jsonCamisetas);
					break;

				case 6:
					Design.printHeader("LISTADO DE USUARIOS");
					String jsonUsuarios = ClienteApi.mostrarUsuarios();
					imprimirTablaUsuarios(jsonUsuarios);
					break;

				case 7:
					Design.printHeader("BUSCAR PEDIDOS DE USUARIO");
					System.out.print("ID usuario: ");
					int idUsuario = Integer.parseInt(sc.nextLine());
					String jsonPedidos = ClienteApi.mostrarPedidos(idUsuario);

					imprimirTablaPedidosUsuario(jsonPedidos);
					break;

				case 8:
					Design.printHeader("DETALLE GLOBAL PEDIDOS");
					String jsonDetalles = ClienteApi.mostrarContenidoPedidos();

					imprimirTablaDetalles(jsonDetalles);
					break;

				case 9:
					System.out.println(Design.BLUE + "Cerrando sesión... ¡Hasta pronto!" + Design.RESET);
					break;

				default:
					Design.printError("Opción no válida.");
				}

			} catch (NumberFormatException nfe) {
				Design.printError("Debes introducir un número válido.");
			} catch (Exception e) {
				Design.printError(e.getMessage());
			}

		} while (opcion != 9);

		sc.close();
	}

	private static void imprimirTablaCamisetas(String json) {
		try {
			JsonNode root = mapper.readTree(json);
			String formato = "| %-4s | %-20s | %-8s | %-9s | %-15s |%n";
			System.out
					.println(Design.YELLOW + "----------------------------------------------------------------------");
			System.out.printf(formato, "ID", "EQUIPO", "PRECIO", "TEMPORADA", "LIGA");
			System.out.println("----------------------------------------------------------------------" + Design.RESET);

			if (root.isArray()) {
				for (JsonNode nodo : root) {
					int id = nodo.path("id").asInt();
					String equipo = nodo.path("equipo").asText("N/A");
					double precio = nodo.path("precio").asDouble();
					String temp = nodo.path("temporada").asText("N/A");
					String liga = nodo.path("liga").asText("N/A");

					if (equipo.length() > 20)
						equipo = equipo.substring(0, 17) + "...";
					if (liga.length() > 15)
						liga = liga.substring(0, 12) + "...";

					System.out.printf(formato, id, equipo, precio + "€", temp, liga);
				}
			}
			System.out.println(Design.YELLOW + "----------------------------------------------------------------------"
					+ Design.RESET);
		} catch (Exception e) {
			System.out.println("Error tabla camisetas: " + e.getMessage());
		}
	}

	private static void imprimirTablaUsuarios(String json) {
		try {
			JsonNode root = mapper.readTree(json);

			String formato = "| %-4s | %-25s | %-35s |%n";
			
			System.out.println(Design.YELLOW + "------------------------------------------------------------------------");
			System.out.printf(formato, "ID", "NOMBRE", "EMAIL / CONTACTO");
			System.out.println("------------------------------------------------------------------------" + Design.RESET);

			if (root.isArray()) {
				for (JsonNode nodo : root) {
					int id = nodo.path("id").asInt(0);
					String nombre = nodo.path("nombre").asText("Sin nombre");
					String email = nodo.path("email").asText(nodo.path("correo").asText("N/A")); 

					if (nombre.length() > 25) nombre = nombre.substring(0, 22) + "...";
					if (email.length() > 35) email = email.substring(0, 32) + "...";

					System.out.printf(formato, id, nombre, email);
				}
			} else if (root.isEmpty()) {
				System.out.println(Design.RED + "   No hay usuarios registrados." + Design.RESET);
			}

			System.out.println(Design.YELLOW + "------------------------------------------------------------------------" + Design.RESET);

		} catch (Exception e) {
			System.out.println(Design.RED + "Error formateando usuarios: " + e.getMessage() + Design.RESET);
		}
	}

	private static void imprimirTablaPedidosUsuario(String json) {
		try {
			JsonNode root = mapper.readTree(json);

			if (root.isEmpty() || (root.isArray() && root.size() == 0)) {
				System.out.println(Design.YELLOW + "Este usuario no tiene pedidos registrados." + Design.RESET);
				return;
			}

			String formato = "| %-6s | %-8s | %-25s | %-12s |%n";

			System.out.println(Design.YELLOW + "----------------------------------------------------------------");
			System.out.printf(formato, "ID PED", "ID USU", "FECHA", "TOTAL");
			System.out.println("----------------------------------------------------------------" + Design.RESET);

			if (root.isArray()) {
				for (JsonNode nodo : root) {
					int id = nodo.path("id").asInt();
					int idUsu = nodo.path("usuario_Id").asInt(0); 
					String fecha = nodo.path("fechaPedido").asText("N/A");
					double total = nodo.path("precioTotal").asDouble(0.0);

					System.out.printf(formato, id, idUsu, fecha, total + "€");
				}
			}
			System.out.println(
					Design.YELLOW + "----------------------------------------------------------------" + Design.RESET);

		} catch (Exception e) {
			System.out.println(Design.RED + "Error formateando pedidos: " + e.getMessage() + Design.RESET);
		}
	}

	private static void imprimirTablaDetalles(String json) {
		try {
			JsonNode root = mapper.readTree(json);

			if (root.isEmpty()) {
				System.out.println(Design.YELLOW + "No hay detalles de pedidos disponibles." + Design.RESET);
				return;
			}

			String formato = "| %-5s | %-8s | %-8s | %-5s | %-6s | %-10s |%n";

			System.out.println(Design.YELLOW + "---------------------------------------------------------------------");
			System.out.printf(formato, "ID", "ID PEDIDO", "ID CAMIS", "CANT", "TALLA", "PRECIO U.");
			System.out.println("---------------------------------------------------------------------" + Design.RESET);

			if (root.isArray()) {
				for (JsonNode nodo : root) {
					int id = nodo.path("id").asInt();
					int idPedido = nodo.path("pedidoId").asInt(nodo.path("pedido_Id").asInt(0));
					int idCamiseta = nodo.path("camisetaId").asInt(nodo.path("camiseta_Id").asInt(0));
					int cantidad = nodo.path("cantidad").asInt();
					String talla = nodo.path("talla").asText("-");
					double precio = nodo.path("precioUnitario").asDouble(0.0);

					System.out.printf(formato, id, idPedido, idCamiseta, cantidad, talla, precio + "€");
				}
			}
			System.out.println(Design.YELLOW + "---------------------------------------------------------------------"
					+ Design.RESET);

		} catch (Exception e) {
			System.out.println(Design.RED + "Error formateando detalles: " + e.getMessage() + Design.RESET);
		}
	}

}