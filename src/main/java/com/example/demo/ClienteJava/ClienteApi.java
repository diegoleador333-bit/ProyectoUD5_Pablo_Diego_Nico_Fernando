package com.example.demo.ClienteJava;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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

	public static String mostrarPedidos() throws Exception {
		URL url = new URL(BASE_URL + "/pedidos");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		String respuesta = leerRespuesta(con);
		con.disconnect();
		return respuesta;
	}

}
