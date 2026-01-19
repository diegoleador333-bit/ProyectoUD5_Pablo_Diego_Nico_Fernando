package com.example.demo.ClienteJava;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminCamisetasEndpoints {

	private final JdbcTemplate jdbcTemplate;

	public AdminCamisetasEndpoints(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//crear camiseta
	@PostMapping("/camisetas")
	public String crearCamiseta(@RequestBody Map<String, Object> datos) {

		String sql = """
				    INSERT INTO Camisetas (equipo, imagen, precio, temporada, liga, parche, nombreDorsal, numeroDorsal)
				    VALUES (?, ?, ?, ?, ?, 0, NULL, NULL)
				""";

		jdbcTemplate.update(sql, datos.get("equipo"), datos.get("imagen"), datos.get("precio"), datos.get("temporada"),
				datos.get("liga"));

		return "Camiseta creada correctamente";
	}

	//cambiar el precio de la camiseta
	@PutMapping("/camisetas/precio/{id}")
	public String cambiarPrecio(@PathVariable int id, @RequestBody Map<String, Double> body) {

		String sql = "UPDATE Camisetas SET precio = ? WHERE id = ?";
		jdbcTemplate.update(sql, body.get("precio"), id);

		return "Precio actualizado";
	}

	//elimnianr la camiseta
	@DeleteMapping("/camisetas/{id}")
	public String eliminarCamiseta(@PathVariable int id) {

		jdbcTemplate.update("DELETE FROM Camisetas WHERE id = ?", id);
		return "Camiseta eliminada";
	}

	//ver el stock y actualizarlo (por hacer)
	@GetMapping("/stock/{idCamiseta}")
	public Object verStock(@PathVariable int idCamiseta) {

		String sql = """
				    SELECT stockS, stockM, stockL, stockXL
				    FROM StockPorTalla
				    WHERE camiseta_Id = ?
				""";

		return jdbcTemplate.queryForMap(sql, idCamiseta);
	}
}
