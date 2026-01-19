package com.example.demo.Endpoints;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Carrito.Carrito;
import com.example.demo.Carrito.CarritoMapper;
import com.example.demo.CarritoContenido.CarritoContenido;
import com.example.demo.CarritoContenido.contenidoMapper;
import com.example.demo.Usuarios.Usuarios;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/carrito")
public class CarritoEndpoints {
	private final JdbcTemplate jdbcTemplate;

	public CarritoEndpoints(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/productos")
	public Object verCarrito(HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");

		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		List<CarritoContenido> contenido = jdbcTemplate.query("SELECT * FROM CarritoContenido WHERE carrito_Id = ?",
				new contenidoMapper(), carrito.getId());

		return contenido;
	}

	@PostMapping("/eliminar/{idCamiseta}")
	@Transactional
	public String eliminarDelCarrito(@PathVariable int idCamiseta, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		int cantidad = jdbcTemplate.queryForObject(
				"SELECT cantidad FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ?", int.class,
				carrito.getId(), idCamiseta);

		double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		double totalResta = precioUnidad * cantidad;

		jdbcTemplate.update("DELETE FROM CarritoContenido WHERE camiseta_Id = ? AND carrito_Id = ?", idCamiseta,
				carrito.getId());

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", totalResta,
				carrito.getId());

		return "Producto eliminado";
	}

	@PostMapping("/comprar")
	@Transactional
	public String comprar(HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		jdbcTemplate.update("DELETE FROM CarritoContenido WHERE carrito_Id = ?", carrito.getId());

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = 0 WHERE id = ?", carrito.getId());

		return "Compra realizada correctamente";
	}

	@PostMapping("/sumar/{idCamiseta}")
	@Transactional
	public String aumentarCantidad(@PathVariable int idCamiseta, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}
		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		jdbcTemplate.update(
				"UPDATE CarritoContenido SET cantidad = cantidad + 1 WHERE carrito_Id = ? AND camiseta_Id = ?",
				carrito.getId(), idCamiseta);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal + ? WHERE id = ?", precioUnidad,
				carrito.getId());

		return "Cantidad aumentada";
	}

	@PostMapping("/restar/{idCamiseta}")
	@Transactional
	public String disminuirCantidad(@PathVariable int idCamiseta, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		jdbcTemplate.update(
				"UPDATE CarritoContenido SET cantidad = cantidad - 1 WHERE carrito_Id = ? AND camiseta_Id = ? AND cantidad > 1",
				usuario.getId(), idCamiseta);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", precioUnidad,
				carrito.getId());

		return "Cantidad disminuida";
	}

	@PostMapping("/parche/{idCamiseta}")
	@Transactional
	public String cambioParche(@PathVariable int idCamiseta, HttpSession session) {
		return "Parche añadido";
	}

}