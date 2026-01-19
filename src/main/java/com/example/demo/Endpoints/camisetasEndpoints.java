package com.example.demo.Endpoints;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Camisetas.CamisetasMapper;
import com.example.demo.Usuarios.Usuarios;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/mostrarCamisetas")
public class camisetasEndpoints {
	private final JdbcTemplate jdbcTemplate;

	public camisetasEndpoints(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/todas")
	public List<Camisetas> mostrarCamisetas(HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		String sql = "SELECT id, imagen, equipo, precio, liga FROM camisetas";

		List<Camisetas> misCamisetas = jdbcTemplate.query(sql, new CamisetasMapper());

		return misCamisetas;
	}

	@GetMapping("camisetas/{unId}")
	public List<Camisetas> mostrarCamisetasById(@PathVariable int unId, HttpSession session) {
		Usuarios usuario = (Usuarios) session.getAttribute("usuario");
		String sql = "SELECT c.id, c.equipo, c.imagen, c.precio, c.temporada, c.liga, c.nombreDorsal, c.numeroDorsal, c.parche, s.stockS, s.stockM, s.stockL, s.stockXL"
				+ "FROM Camisetas c" + "INNER JOIN StockPorTalla s ON c.id = s.camiseta_Id"
				+ "WHERE c.id = :idCamisetaSeleccionada";
		List<Camisetas> misCamisetas = jdbcTemplate.query(sql, new CamisetasMapper());
		
		return misCamisetas;
	}

}
