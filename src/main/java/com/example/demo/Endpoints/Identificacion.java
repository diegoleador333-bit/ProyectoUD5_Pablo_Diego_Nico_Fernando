package com.example.demo.Endpoints;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Carrito.CarritoMapper;
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

	// Endpoint para el registro del usuario
	@PostMapping("/registro")
	public String registro(@RequestBody Usuarios nuevo, HttpSession session) {
		try {
			Date fecha = new Date();
			// Primero miramos que el correo no este registrado en la base de datos
			List<Usuarios> existentes = jdbcTemplate.query("SELECT * FROM Usuarios WHERE correo = ?",
					new UsuariosMapper(), nuevo.getCorreo());

			if (!existentes.isEmpty()) {
				return "El usuario ya existe";
			}
			// Continuamos haciendo un hash basico de la contraseña para guardarla de manera
			// correcta
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

			// Hacemos un insert del usuario en a base de datos y conseguimos su informacion
			jdbcTemplate.update("INSERT INTO Usuarios (DNI, nombre, apellido, correo, pwd) VALUES (?, ?, ?, ?, ?)",
					nuevo.getDni(), nuevo.getNombre(), nuevo.getApellido(), nuevo.getCorreo(), hashPwd);

			Usuarios x = jdbcTemplate.queryForObject("SELECT * FROM Usuarios WHERE correo = ?", new UsuariosMapper(),
					nuevo.getCorreo());

			// Finalmente con la informacion del usuario se le crea y asigna un carrito
			jdbcTemplate.update("INSERT INTO Carrito (usuario_Id, precioTotal, fechaCreacion) VALUES (?, ?, ?)",
					x.getId(), 0, fecha);

			// Creamos ademas la sesion del usuario
			session.setAttribute("usuario", x);
			return "Usuario registrado correctamente";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return "No se pudo registrar correctamente";
		}
	}

	// Endpoint para hacer login
	@PostMapping("/login")
	public String login(@RequestBody Usuarios login, HttpSession session) {
		try {

			// Hacemos el hash de la contraseña para poder compararla con la base de datos
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

			// Hacemos una lista de todos los usuarios para luego saber si tiene alguno con
			// los datos traidos
			List<Usuarios> encontrados = jdbcTemplate.query("SELECT * FROM usuarios WHERE correo = ? AND pwd = ?",
					new UsuariosMapper(), login.getCorreo(), hashPwd);

			if (encontrados.isEmpty()) {
				session.invalidate();
				return "Correo o contraseña incorrectos";
			}

			// Creamos la sesion del usuario
			Usuarios u = encontrados.get(0);
			session.setAttribute("usuario", u);

			return "Login correcto";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return "No se pudo iniciar sesion correctamente";
		}
	}

	// Endpoint para cerrar la sesion
	@PostMapping("/cerrar")
	public String logout(HttpSession session) {
		session.invalidate();
		return "Sesion cerrada correctamente";
	}

	// Endpoint para sacar toda la informacion del usuario
	@GetMapping("/perfil")
	public Object sesion(HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");

		if (usuario == null) {
			return "No has iniciado sesion";
		}

		return usuario;

	}
}
