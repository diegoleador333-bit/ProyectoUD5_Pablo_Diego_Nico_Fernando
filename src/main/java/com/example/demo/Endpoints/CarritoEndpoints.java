package com.example.demo.Endpoints;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
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
	public String eliminarDelCarrito(@PathVariable int idCamiseta, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		jdbcTemplate.update(
				"DELETE xxx FROM CarritoContenido xxx JOIN Carrito yyy ON xxx.carrito_Id = yyy.id WHERE yyy.usuario_Id = ? AND xxx.camiseta_Id = ?",
				usuario.getId(), idCamiseta);

		return "Producto eliminado";
	}

	@PostMapping("/comprar")
	public String comprar(HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		jdbcTemplate.update("DELETE FROM CarritoContendio WHERE carrito_Id = ?", carrito.getId());

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = 0 WHERE id = ?", carrito.getId());

		return "Compra realizada correctamente";
	}

	@PostMapping("/anhadir/{idCamiseta}")
	public String aumentarCantidad(@PathVariable int idCamiseta, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		jdbcTemplate.update(
				"UPDATE CarritoContendio xxx JOIN Carrito yyy ON xxx.carrito_Id = yyy.id SET xxx.cantidad = xxx.cantidad + 1 WHERE yyy.usuario_Id = ? AND xxx.camiseta_Id = ?",
				usuario.getId(), idCamiseta);

		return "Cantidad aumentada";
	}
	
	@PostMapping("/restar/{idCamiseta}")
	public String disminuirCantidad(@PathVariable int idCamiseta, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		jdbcTemplate.update(
				"UPDATE CarritoContendio xxx JOIN Carrito yyy ON xxx.carrito_Id = yyy.id SET xxx.cantidad = xxx.cantidad - 1 WHERE yyy.usuario_Id = ? AND xxx.camiseta_Id = ? AND xxx.cantidad > 1",
				usuario.getId(), idCamiseta);

		return "Cantidad disminuida";
	}

}