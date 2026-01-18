package com.example.demo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/identificacion")
public class Identificacion {

	private final JdbcTemplate jdbcTemplate;

	public Identificacion(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostMapping("/registro")
	public String registro(@RequestBody Usuarios nuevo, HttpSession session) {

		List<Usuarios> existentes = jdbcTemplate.query("SELECT * FROM usuarios WHERE correo = ?", new UsuariosMapper(),
				nuevo.getCorreo());

		if (!existentes.isEmpty()) {
			return "El usuario ya existe";
		}

		jdbcTemplate.update(
			    "INSERT INTO usuarios (dni, nombre, apellido, correo, password) VALUES (?, ?, ?, ?, ?)",
			    nuevo.getDni(),
			    nuevo.getNombre(),
			    nuevo.getApellido(),
			    nuevo.getCorreo(),
			    nuevo.getPassword()
			);

		session.setAttribute("usuario", nuevo);
		return "Usuario registrado correctamente";
	}

	@PostMapping("/login")
	public String login(@RequestBody Usuarios login, HttpSession session) {

		List<Usuarios> encontrados = jdbcTemplate.query("SELECT * FROM usuarios WHERE correo = ? AND password = ?",
				new UsuariosMapper(), login.getCorreo(), login.getPassword());

		if (encontrados.isEmpty()) {
			return "Correo o contraseña incorrectos";
		}

		Usuarios u = encontrados.get(0); 
		session.setAttribute("usuario", u);
		
		return "Login correcto";
	}
	
	@PostMapping("/cerrar")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "Sesión cerrada correctamente";
	}
	
	@GetMapping("/perfil")
	public Object sesion(HttpSession session) {

	    Usuarios usuario = (Usuarios) session.getAttribute("usuario");

	    if (usuario == null) {
	        return "No hay ninguna sesion";
	    }

	    return usuario;
	}

}
