package com.example.demo;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClienteApi {

	private static final String BASE_URL = "http://localhost:8080/admin/camisetas";

	public static String getCamisetas(String liga, String temporada) throws Exception {

		String urlStr = BASE_URL + "?liga=" + URLEncoder.encode(liga, "UTF-8") + "&temporada=" + temporada;

		HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();

		conn.setRequestMethod("GET");

		return leerRespuesta(conn);
	}

	// crear
	public static void crearCamiseta(String json) throws Exception {

		HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		try (OutputStream os = conn.getOutputStream()) {
			os.write(json.getBytes(StandardCharsets.UTF_8));
		}

		leerRespuesta(conn);
	}

	// borrar camiseta
	public static void eliminarCamiseta(int id) throws Exception {

		HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();

		conn.setRequestMethod("DELETE");
		leerRespuesta(conn);
	}

	private static String leerRespuesta(HttpURLConnection conn) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String linea;
		while ((linea = br.readLine()) != null) {
			sb.append(linea).append("\n");
		}
		return sb.toString();
	}
}
