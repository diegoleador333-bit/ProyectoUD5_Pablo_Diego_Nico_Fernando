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
		String sql = "SELECT * FROM Camisetas";
		return jdbcTemplate.query(sql, new CamisetasMapper());
	}

	@PostMapping("/camisetas/{idCamiseta}")
	public String anadirSimplePeroCorrecto(@PathVariable int idCamiseta, @RequestBody CarritoContenido item,
			HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		if (usuario == null)
			return "No has iniciado sesion";

		Integer cantObj = item.getCantidad();
		int cantidad = (cantObj == null) ? 1 : cantObj;

		Carrito carrito = jdbcTemplate.queryForObject("SELECT * FROM Carrito WHERE usuario_Id = ?", new CarritoMapper(),
				usuario.getId());

		String talla = item.getTallaSeleccionada();

		Integer existe = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM CarritoContenido WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
				Integer.class, carrito.getId(), idCamiseta, talla);

		if (existe != null && existe > 0) {
			jdbcTemplate.update(
					"UPDATE CarritoContenido SET cantidad = cantidad + ? WHERE carrito_Id = ? AND camiseta_Id = ? AND tallaSeleccionada = ?",
					cantidad, carrito.getId(), idCamiseta, talla);
		} else {
			jdbcTemplate.update(
					"INSERT INTO CarritoContenido (carrito_Id, camiseta_Id, cantidad, tallaSeleccionada, nombrePersonalizado, numeroPersonalizado, llevaParche) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?)",
					carrito.getId(), idCamiseta, cantidad, talla, item.getNombrePersonalizado(),
					item.getNumeroPersonalizado(), item.isLlevaParche());
		}

		double precio = jdbcTemplate.queryForObject("SELECT precio FROM Camisetas WHERE id = ?", Double.class,
				idCamiseta);

		if (item.isLlevaParche())
			precio += 5.0;

		jdbcTemplate.update("UPDATE Carrito SET precioTotal = precioTotal + ? WHERE id = ?", precio * cantidad,
				carrito.getId());

		return "Añadido al carrito";
	}

}
