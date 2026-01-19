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
		String sql = "SELECT id, imagen, equipo, precio, temporada, liga, parche, nombreDorsal, numeroDorsal FROM camisetas";
		return jdbcTemplate.query(sql, new CamisetasMapper());
	}

	@GetMapping("/{unId}")
	public Camisetas mostrarCamisetasById(@PathVariable int unId) {
	    String sql = "SELECT id, imagen, equipo, precio, temporada, liga, parche, nombreDorsal, numeroDorsal FROM camisetas WHERE id = ?";
	    return jdbcTemplate.queryForObject(sql, new CamisetasMapper(), unId);
	}


}
