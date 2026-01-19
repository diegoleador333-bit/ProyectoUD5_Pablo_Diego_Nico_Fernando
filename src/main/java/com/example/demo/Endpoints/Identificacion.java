package com.example.demo.Endpoints;

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

		List<Usuarios> existentes = jdbcTemplate.query("SELECT * FROM Usuarios WHERE correo = ?", new UsuariosMapper(),
				nuevo.getCorreo());

		if (!existentes.isEmpty()) {
			return "El usuario ya existe";
		}

		jdbcTemplate.update("INSERT INTO Usuarios (DNI, nombre, apellido, correo, pwd) VALUES (?, ?, ?, ?, ?)",
				nuevo.getDni(), nuevo.getNombre(), nuevo.getApellido(), nuevo.getCorreo(), nuevo.getPassword());

		session.setAttribute("usuario", nuevo);
		return "Usuario registrado correctamente";
	}

	@PostMapping("/login")
	public String login(@RequestBody Usuarios login, HttpSession session) {

		List<Usuarios> encontrados = jdbcTemplate.query("SELECT * FROM usuarios WHERE correo = ? AND pwd = ?",
				new UsuariosMapper(), login.getCorreo(), login.getPassword());

		if (encontrados.isEmpty()) {
			session.invalidate(); //elimina sesión previa
			return "Correo o contraseña incorrectos";
		}

		Usuarios u = encontrados.get(0);
		session.setAttribute("usuario", u);

		return "Login correcto";
	}

	@PostMapping("/cerrar")
	public String logout(HttpSession session) {
		session.invalidate();
		return "Sesion cerrada correctamente";
	}

	@GetMapping("/perfil")
	public Usuarios sesion(HttpSession session) {
		return (Usuarios) session.getAttribute("usuario"); // null si no hay sesion
	}

}