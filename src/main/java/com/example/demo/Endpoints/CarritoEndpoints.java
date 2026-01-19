package com.example.demo.Endpoints;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PostMapping("/eliminar/{idCamiseta}/{talla}")
	public String eliminarDelCarrito(@PathVariable int idCamiseta, @PathVariable String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND talla = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		Integer cantidad = jdbcTemplate.queryForObject(
				"SELECT cantidad FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ?", Integer.class,
				carrito.getId(), idCamiseta);

		double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		double totalResta = precioUnidad * cantidad;

		if (parche) {
			totalResta = totalResta + 5.00 * cantidad;
		}

		jdbcTemplate.update("DELETE FROM CarritoContenido WHERE camiseta_Id = ? AND carrito_Id = ? AND talla = ?",
				idCamiseta, carrito.getId(), talla);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", totalResta,
				carrito.getId());

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

		jdbcTemplate.update("DELETE FROM CarritoContenido WHERE carrito_Id = ?", carrito.getId());

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = 0 WHERE id = ?", carrito.getId());

		return "Compra realizada correctamente";
	}

	@PostMapping("/sumar/{idCamiseta}/{talla}")
	public String aumentarCantidad(@PathVariable int idCamiseta, @PathVariable String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}
		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND talla = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		String columnaStock = "stock" + talla;
		Integer stockDisponible = jdbcTemplate.queryForObject(
				"SELECT " + columnaStock + " FROM StockPorTalla WHERE camiseta_Id = ?", Integer.class, idCamiseta);

		Integer cantidadCarrito = jdbcTemplate.queryForObject(
				"SELECT cantidad FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ? AND talla = ?",
				Integer.class, carrito.getId(), idCamiseta, talla);

		if (cantidadCarrito >= stockDisponible) {
			return "No hay mas stock disponible para la talla " + talla;
		}

		double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);
		if (parche) {
			precioUnidad = precioUnidad + 5.00;
		}

		jdbcTemplate.update(
				"UPDATE CarritoContenido SET cantidad = cantidad + 1 WHERE carrito_Id = ? AND camiseta_Id = ? AND talla = ?",
				carrito.getId(), idCamiseta, talla);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal + ? WHERE id = ?", precioUnidad,
				carrito.getId());

		return "Cantidad aumentada";
	}

	@PostMapping("/restar/{idCamiseta}/{talla}")
	public String disminuirCantidad(@PathVariable int idCamiseta, @PathVariable String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND talla = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		double precioUnidad = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		if (parche) {
			precioUnidad = precioUnidad + 5.00;
		}

		jdbcTemplate.update(
				"UPDATE CarritoContenido SET cantidad = cantidad - 1 WHERE carrito_Id = ? AND camiseta_Id = ? AND cantidad > 1 AND talla = ?",
				usuario.getId(), idCamiseta, talla);

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", precioUnidad,
				carrito.getId());

		return "Cantidad disminuida";
	}

	@PostMapping("/parche/{idCamiseta}/{talla}")
	public String cambioParche(@PathVariable int idCamiseta, @PathVariable String talla, HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		boolean parche = jdbcTemplate.queryForObject(
				"SELECT llevaParche FROM CarritoContenido WHERE camiseta_Id = ? AND talla = ? AND carrito_Id = ?",
				Boolean.class, idCamiseta, talla, carrito.getId());

		Integer cantidadCarrito = jdbcTemplate.queryForObject(
				"SELECT cantidad FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ? AND talla = ?",
				Integer.class, carrito.getId(), idCamiseta, talla);

		double precioFinal = 5.00 * cantidadCarrito;

		if (parche) {
			jdbcTemplate.update(
					"UPDATE CarritoContenido SET llevaParche = 0 WHERE carrito_Id = ? AND camiseta_Id = ? AND talla = ?",
					carrito.getId(), idCamiseta, talla);
			jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal - ? WHERE id = ?", precioFinal,
					carrito.getId());
			return "Parche quitado";
		} else {
			jdbcTemplate.update(
					"UPDATE CarritoContenido SET llevaParche = 1 WHERE carrito_Id = ? AND camiseta_Id = ? AND talla = ?",
					carrito.getId(), idCamiseta, talla);
			jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal + ? WHERE id = ?", precioFinal,
					carrito.getId());
			return "Parche añadido";
		}
	}

	@PostMapping("/datos/{idCamiseta}/{talla}")
	public String cambioNombre(@RequestParam(value = "nombrePersonalizado") String nombrePersonalizado,
			@RequestParam(value = "numeroPersonalizado") String numeroPersonalizado, @PathVariable int idCamiseta,
			@PathVariable String talla, HttpSession session) {

		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null) {
			return "No has iniciado sesion";
		}

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		jdbcTemplate.update(
				"UPDATE CarritoContenido SET nombrePersonalizado = ?, numeroPersonalizado = ? WHERE carrito_Id = ? AND camiseta_Id = ? AND talla = ?",
				nombrePersonalizado, numeroPersonalizado, carrito.getId(), idCamiseta, talla);

		return "Datos cambiados";

	}

}