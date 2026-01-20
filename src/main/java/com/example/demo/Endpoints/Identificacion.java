package com.example.demo.Endpoints;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Usuarios.Usuarios;
import com.example.demo.Usuarios.UsuariosMapper;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = { "http://127.0.0.1:5500", "http://localhost:5500" }, allowCredentials = "true")
@RestController
@RequestMapping("/identificacion")
public class Identificacion {

	private final JdbcTemplate jdbcTemplate;

	public Identificacion(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostMapping("/registro")
	public String registro(@RequestBody Usuarios nuevo, HttpSession session) {
		try {

			List<Usuarios> existentes = jdbcTemplate.query("SELECT * FROM Usuarios WHERE correo = ?",
					new UsuariosMapper(), nuevo.getCorreo());

			if (!existentes.isEmpty()) {
				return "El usuario ya existe";
			}

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] resumen = md.digest(nuevo.getPassword().getBytes());

			StringBuilder hexPwd = new StringBuilder();
			for (byte b : resumen) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexPwd.append('0');
				hexPwd.append(hex);
			}
			String hashPwd = hexPwd.toString();

			jdbcTemplate.update("INSERT INTO Usuarios (DNI, nombre, apellido, correo, pwd) VALUES (?, ?, ?, ?, ?)",
					nuevo.getDni(), nuevo.getNombre(), nuevo.getApellido(), nuevo.getCorreo(), hashPwd);

			session.setAttribute("usuario", nuevo);
			return "Usuario registrado correctamente";
		} catch (NoSuchAlgorithmException e) {
			return "Error, no se pudo registrar el usuario";
		}
	}

	@PostMapping("/login")
	public String login(@RequestBody Usuarios login, HttpSession session) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] resumen = md.digest(login.getPassword().getBytes());

			StringBuilder hexPwd = new StringBuilder();
			for (byte b : resumen) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexPwd.append('0');
				hexPwd.append(hex);
			}
			String hashPwd = hexPwd.toString();

			List<Usuarios> encontrados = jdbcTemplate.query("SELECT * FROM usuarios WHERE correo = ? AND pwd = ?",
					new UsuariosMapper(), login.getCorreo(), hashPwd);

			if (encontrados.isEmpty()) {
				session.invalidate();
				return "Correo o contraseña incorrectos";
			}

			Usuarios u = encontrados.get(0);
			session.setAttribute("usuario", u);

			return "Login correcto";
		} catch (NoSuchAlgorithmException e) {
			session.invalidate();
			return "Error al hacer login";
		}
	}

	@PostMapping("/cerrar")
	public String logout(HttpSession session) {
		session.invalidate();
		return "Sesion cerrada correctamente";
	}

	@GetMapping("/perfil")
	public Object sesion(HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");

		if (usuario == null) {
			return "No has iniciado sesion";
		}

		return usuario;

	}
}
