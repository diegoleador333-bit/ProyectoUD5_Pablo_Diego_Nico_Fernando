package com.example.demo.ClienteJava;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Camisetas.CamisetasMapper;
import com.example.demo.Stock.StockMapper;
import com.example.demo.Stock.StockPorTalla;
import com.example.demo.Usuarios.Usuarios;
import com.example.demo.Usuarios.UsuariosMapper;

import Pedidos.Pedidos;
import Pedidos.PedidosMapper;

@RestController
@RequestMapping("/admin")
public class AdminCamisetasEndpoints {

	private final JdbcTemplate jdbcTemplate;

	public AdminCamisetasEndpoints(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// crear camiseta
	@PostMapping("/camisetas")
	public String crearCamiseta(@RequestBody Map<String, Object> datos) {

		try {
			String sql = """
					    INSERT INTO Camisetas (equipo, imagen, precio, temporada, liga, parche, nombreDorsal, numeroDorsal)
					    VALUES (?, ?, ?, ?, ?, 0, NULL, NULL)
					""";

			String equipo = (String) datos.get("equipo");
			String imagen = (String) datos.get("imagen");
			String temporada = (String) datos.get("temporada");
			String liga = (String) datos.get("liga");
			double precio = ((Number) datos.get("precio")).doubleValue();

			// ejecutamos la consulta a la bbdd
			jdbcTemplate.update(sql, equipo, imagen, precio, temporada, liga);

			return "Camiseta creada correctamente";

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// cambiar el precio de la camiseta
	@PutMapping("/camisetas/precio/{id}")
	public String cambiarPrecio(@PathVariable int id, @RequestBody Map<String, Double> body) {

		// accedemos a la base de datos para actualizar
		String sql = "UPDATE Camisetas SET precio = ? WHERE id = ?";
		jdbcTemplate.update(sql, body.get("precio"), id);

		return "Precio actualizado";
	}

	// elimnianr la camiseta
	@DeleteMapping("/camisetas/{id}")
	public String eliminarCamiseta(@PathVariable int id) {

		// borramos la camiseta por id
		jdbcTemplate.update("DELETE FROM Camisetas WHERE id = ?", id);
		return "Camiseta eliminada";
	}

	// ver el stock y actualizarlo (por hacer)
	@GetMapping("/stock/{idCamiseta}")
	public List<StockPorTalla> verStock(@PathVariable int idCamiseta) {

		String sql = """
				    SELECT stockS, stockM, stockL, stockXL
				    FROM StockPorTalla
				    WHERE camiseta_Id = ?
				""";

		return jdbcTemplate.query(sql, new StockMapper(), idCamiseta);
	}

	@GetMapping("/camisetas")
	public List<Camisetas> mostrarCamisetas() {
		String sql = """
				    SELECT id, equipo, imagen, precio, temporada, liga
				    FROM Camisetas
				""";
		return jdbcTemplate.query(sql, new CamisetasMapper());
	}

	@GetMapping("/usuarios")
	public List<Usuarios> mostrarUsuarios() {
		String sql = """
				    SELECT *
				    FROM Usuarios
				""";
		return jdbcTemplate.query(sql, new UsuariosMapper());
	}

	@GetMapping("/pedidos")
	public List<Pedidos> mostrarPedidos() {
		String sql = """
				    SELECT id, usuario_Id, fechaPedido, precioTotal
				    FROM Pedidos
				""";
		return jdbcTemplate.query(sql, new PedidosMapper());
	}

}
