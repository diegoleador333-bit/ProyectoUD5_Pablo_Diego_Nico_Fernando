package com.example.demo.ClienteJava;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClienteApi {

	private static final String BASE_URL = "http://localhost:8080/admin";

	//crear camiseta
	public static void crearCamiseta(String json) throws Exception {

		URL url = new URL(BASE_URL + "/camisetas");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		try (OutputStream os = con.getOutputStream()) {
			os.write(json.getBytes());
		}

		con.getInputStream();
		con.disconnect();
	}

	//cambiar el precio
	public static void cambiarPrecio(int id, double precio) throws Exception {

		URL url = new URL(BASE_URL + "/camisetas/precio/" + id);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		String json = "{ \"precio\": " + precio + " }";

		try (OutputStream os = con.getOutputStream()) {
			os.write(json.getBytes());
		}

		con.getInputStream();
		con.disconnect();
	}

	//eliminar camiseta por id
	public static void eliminarCamiseta(int id) throws Exception {

		URL url = new URL(BASE_URL + "/camisetas/" + id);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("DELETE");
		con.getInputStream();
		con.disconnect();
	}

	//ve el stock 
	public static String verStock(int id) throws Exception {

		URL url = new URL(BASE_URL + "/stock/" + id);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		StringBuilder sb = new StringBuilder();
		String linea;

		while ((linea = br.readLine()) != null) {
			sb.append(linea).append("\n");
		}

		con.disconnect();
		return sb.toString();
	}
}
