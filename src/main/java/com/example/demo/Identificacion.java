package com.example.demo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identificacion")
public class Identificacion {

	private final JdbcTemplate jdbcTemplate;

	public Identificacion(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostMapping("/registro")
	public String registro(@RequestBody Usuarios nuevo) {

		List<Usuarios> existentes = jdbcTemplate.query("SELECT * FROM usuarios WHERE correo = ?", new UsuariosMapper(),
				nuevo.getCorreo());

		if (!existentes.isEmpty()) {
			return "El usuario ya existe";
		}

		jdbcTemplate.update("INSERT INTO usuarios (nombre, password) VALUES (?, ?)", nuevo.getCorreo(),
				nuevo.getPassword());

		return "Usuario registrado correctamente";
	}

	@PostMapping("/login")
	public String login(@RequestBody Usuarios login) {

		List<Usuarios> encontrados = jdbcTemplate.query("SELECT * FROM usuarios WHERE correo = ? AND password = ?",
				new UsuariosMapper(), login.getCorreo(), login.getPassword());

		if (encontrados.isEmpty()) {
			return "Correo o contraseña incorrectos";
		}

		Usuarios u = encontrados.get(0); 
		return "Login correcto";
	}
}
