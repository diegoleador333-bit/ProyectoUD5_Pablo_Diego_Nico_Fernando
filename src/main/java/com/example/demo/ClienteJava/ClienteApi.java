package com.example.demo.ClienteJava;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.demo.Stock.StockPorTalla;

import tools.jackson.databind.ObjectMapper;

public class ClienteApi {

	private static final String BASE_URL = "http://localhost:8080/admin";

	// envia datos para crear el POST
	// Recibe el json de ClienteAdmin
	public static void crearCamiseta(String json) throws Exception {

		URL url = new URL(BASE_URL + "/camisetas");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// Enviar los datos con POST
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true); // habilita enviar datos

		// enviamos el json por el cable
		try (OutputStream os = con.getOutputStream()) {
			os.write(json.getBytes());
		}

		// recibir respuesta
		con.getInputStream();
		con.disconnect();
	}

	// recibe el id y el nuevo precio
	public static void cambiarPrecio(int id, double precio) throws Exception {

		URL url = new URL(BASE_URL + "/camisetas/precio/" + id);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// para actualizar
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		// concatenamos el precio al json
		String json = "{ \"precio\": " + precio + " }";

		try (OutputStream os = con.getOutputStream()) {
			os.write(json.getBytes());
		}

		con.getInputStream();
		con.disconnect();
	}

	public static void actualizarStock(int idCamiseta, StockPorTalla stock) throws Exception {

		URL url = new URL(BASE_URL + "/actualizarStock/" + idCamiseta);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		String json = String.format("{\"stockS\":%d, \"stockM\":%d, \"stockL\":%d, \"stockXL\":%d}", stock.getStockS(),
				stock.getStockM(), stock.getStockL(), stock.getStockXL());

		System.out.println("JSON ENVIADO: " + json);

		try (OutputStream os = con.getOutputStream()) {
			os.write(json.getBytes("UTF-8"));
		}

		int responseCode = con.getResponseCode();

		if (responseCode == 200) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String response = br.readLine();
				System.out.println("Servidor dice: " + response);
			}
		} else {
			System.out.println("Error en la petición. Código: " + responseCode);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
				String line;
				StringBuilder errorMsg = new StringBuilder();
				while ((line = br.readLine()) != null) {
					errorMsg.append(line);
				}
				System.out.println("DETALLE DEL ERROR DEL SERVIDOR: " + errorMsg.toString());
			} catch (Exception e) {
				System.out.println("No se pudo leer el detalle del error.");
			}
		}
	}

	// recibe el id
	public static void eliminarCamiseta(int id) throws Exception {

		URL url = new URL(BASE_URL + "/camisetas/" + id);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("DELETE");
		con.getInputStream();
		con.disconnect();
	}

	// pasamos el id para ver stock
	public static String verStock(int id) throws Exception {

		URL url = new URL(BASE_URL + "/stock/" + id);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET"); // para leer datos

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		StringBuilder sb = new StringBuilder();
		String linea;

		// bucle para leer el stock
		while ((linea = br.readLine()) != null) {
			sb.append(linea).append("\n");
		}

		con.disconnect();
		return sb.toString();
	}

	public static StockPorTalla verStockObjeto(int id) throws Exception {
		URL url = new URL(BASE_URL + "/stock/" + id);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		ObjectMapper mapper = new ObjectMapper();
		try (InputStream is = con.getInputStream()) {
			StockPorTalla miStock = mapper.readValue(is, StockPorTalla.class);
			return miStock;
		}
	}

	private static String leerRespuesta(HttpURLConnection con) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String linea;
		while ((linea = br.readLine()) != null) {
			sb.append(linea).append("\n");
		}
		return sb.toString();
	}

	public static String mostrarCamisetas() throws Exception {
		URL url = new URL(BASE_URL + "/camisetas");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		String respuesta = leerRespuesta(con);
		con.disconnect();
		return respuesta;
	}

	public static String mostrarUsuarios() throws Exception {
		URL url = new URL(BASE_URL + "/usuarios");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		String respuesta = leerRespuesta(con);
		con.disconnect();
		return respuesta;
	}

	public static String mostrarPedidos(int idUsuario) throws Exception {
		URL url = new URL(BASE_URL + "/pedidos/" + idUsuario);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		String respuesta = leerRespuesta(con);
		con.disconnect();
		return respuesta;
	}

	public static String mostrarContenidoPedidos() throws Exception {
		URL url = new URL(BASE_URL + "/verTodosLosPedidos");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		String respuesta = leerRespuesta(con);
		con.disconnect();
		return respuesta;
	}

}
