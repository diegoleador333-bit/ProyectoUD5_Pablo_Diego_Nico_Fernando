package com.example.demo.Endpoints;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Camisetas.CamisetasMapper;
import com.example.demo.Carrito.Carrito;
import com.example.demo.Carrito.CarritoMapper;
import com.example.demo.CarritoContenido.CarritoContenido;
import com.example.demo.Stock.StockMapper;
import com.example.demo.Stock.StockPorTalla;
import com.example.demo.Usuarios.Usuarios;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = { "http://127.0.0.1:5500", "http://localhost:5500" }, allowCredentials = "true")
@RestController
@RequestMapping("/mostrarCamisetas")
public class camisetasEndpoints {
	private final JdbcTemplate jdbcTemplate;

	public camisetasEndpoints(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/todas")
	public List<Camisetas> mostrarCamisetas(HttpSession session) {
		// todo en minúsculas
		String sql = "SELECT * FROM camisetas";
		return jdbcTemplate.query(sql, new CamisetasMapper());
	}

	@PostMapping("/subirCamiseta")
	public String subirCamisetas(HttpSession session, @RequestBody Camisetas camiseta) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null)
			return "No has iniciado sesion";

		String sql = "INSERT INTO camisetas (equipo, imagen, precio, temporada, liga) VALUES (?, ?, ?, ?, ?)";

		int resultado = jdbcTemplate.update(sql, camiseta.getEquipo(), camiseta.getImagen(), camiseta.getPrecio(),
				camiseta.getTemporada(), camiseta.getLiga());
		if (resultado == 1)
			return "Camiseta introducida con exito";
		else
			return "No se ha podido añadir la camiseta";
	}

	@PostMapping("/actualizarCamiseta")
	public String cambiarPrecioCamisetas(HttpSession session, @RequestBody Camisetas camiseta) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null)
			return "No has iniciado sesion";

		String sql = "UPDATE camisetas SET precio = ? WHERE id = ?";

		int resultado = jdbcTemplate.update(sql, camiseta.getPrecio(), camiseta.getId());
		if (resultado == 1)
			return "Camiseta actualizada con exito";
		else
			return "No se ha podido cambiar el precio a la camiseta";
	}

	@PostMapping("/eliminarCamiseta/{id}")
	public String eliminarCamisetas(HttpSession session, @PathVariable int id) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null)
			return "No has iniciado sesion";

		String sql = "DELETE FROM camisetas WHERE id = ?";

		try {
			int resultado = jdbcTemplate.update(sql, id);
			if (resultado == 1)
				return "Camiseta eliminada con exito";
			else
				return "No se ha encontrado la camiseta con ese ID";
		} catch (Exception e) {
			return "Error al eliminar: Es posible que la camiseta esté en algún pedido histórico.";
		}
	}

	@GetMapping("/verStock")
	public List<StockPorTalla> verStockPorTalla() {
		return jdbcTemplate.query("select * from stockportalla", new StockMapper());
	}

	@PostMapping("/actualizarStock/{idCamiseta}")
	public String actualizarStockCamisetas(HttpSession session, @PathVariable int idCamiseta,
			@RequestBody StockPorTalla stock) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null)
			return "No has iniciado sesion";

		// CORREGIDO: todo minúsculas (stockportalla, camiseta_id)
		String sql = "UPDATE stockportalla SET stockS = ?, stockM = ?, stockL = ?, stockXL = ? WHERE camiseta_id = ?";
		int resultado = jdbcTemplate.update(sql, stock.getStockS(), stock.getStockM(), stock.getStockL(),
				stock.getStockXL(), idCamiseta);
		if (resultado == 1)
			return "Stock actualizado con exito";
		else
			return "No se pudo actualizar el stock (¿ID de camiseta incorrecto?)";
	}

	@PostMapping("/camisetas/{idCamiseta}")
	public String anadirSimplePeroCorrecto(@PathVariable int idCamiseta, @RequestBody CarritoContenido item,
			HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null)
			return "No has iniciado sesion";

		Integer cantObj = item.getCantidad();
		int cantidad = (cantObj == null) ? 1 : cantObj;

		// CORREGIDO: carrito (minúscula), usuario_id (minúscula)
		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM carrito WHERE usuario_id = ?", new CarritoMapper(),
				usuario.getId());

		String talla = item.getTallaSeleccionada();

		// CORREGIDO: carritocontenido, carrito_id, camiseta_id (TODO MINÚSCULA)
		Integer existe = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM carritocontenido WHERE carrito_id = ? AND camiseta_id = ? AND tallaSeleccionada = ?",
				Integer.class, carrito.getId(), idCamiseta, talla);

		if (existe != null && existe > 0) {
			// CORREGIDO: todo minúscula
			jdbcTemplate.update(
					"UPDATE carritocontenido SET cantidad = cantidad + ? WHERE carrito_id = ? AND camiseta_id = ? AND tallaSeleccionada = ?",
					cantidad, carrito.getId(), idCamiseta, talla);
		} else {
			// CORREGIDO: todo minúscula
			jdbcTemplate.update(
					"INSERT INTO carritocontenido (carrito_id, camiseta_id, cantidad, tallaSeleccionada, nombrePersonalizado, numeroPersonalizado, llevaParche) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
					carrito.getId(), idCamiseta, cantidad, talla, item.getNombrePersonalizado(),
					item.getNumeroPersonalizado(), item.isLlevaParche());
		}

		double precio = jdbcTemplate.queryForObject("SELECT precio FROM camisetas WHERE id = ?", Double.class,
				idCamiseta);

		if (item.isLlevaParche())
			precio += 5.0;

		jdbcTemplate.update("UPDATE carrito SET precioTotal = precioTotal + ? WHERE id = ?", precio * cantidad,
				carrito.getId());

		return "Añadido al carrito";
	}
}