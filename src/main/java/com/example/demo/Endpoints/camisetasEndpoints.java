package com.example.demo.Endpoints;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Camisetas.CamisetasMapper;
import com.example.demo.CarritoContenido.CarritoContenido;
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
		String sql = "SELECT * FROM camisetas";
		return jdbcTemplate.query(sql, new CamisetasMapper());
	}

//	@GetMapping("camisetas/{unId}")
//	public String mostrarCamisetasById(@PathVariable int unId, @RequestBody CarritoContenido miCarritoItems,
//			HttpSession session) {
//
//		try {
//			Usuarios usuario = (Usuarios) session.getAttribute("usuario");
//			if (usuario == null) {
//				return "No has iniciado sesion";
//			}
//			StockPorTalla miStock = new StockPorTalla();
//			if (miCarritoItems.getTallaSeleccionada().equals("S")) {
//				if (miStock.getStockS() == 0) {
//					return "No quedan camisetas S";
//				}
//			} else if (miCarritoItems.getTallaSeleccionada().equals("M")) {
//				if (miStock.getStockM() == 0) {
//					return "No quedan camisetas M";
//				}
//			} else if (miCarritoItems.getTallaSeleccionada().equals("L")) {
//				if (miStock.getStockL() == 0) {
//					return "No quedan camisetas L";
//				}
//			} else if (miCarritoItems.getTallaSeleccionada().equals("XL")) {
//				if (miStock.getStockXL() == 0) {
//					return "No quedan camisetas XL";
//				}
//			}
//			String sql = "INSERT INTO CarritoContenido (carrito_Id, cantidad, tallaSeleccionada,nombrePersonalizado, numeroPersonalizado, llevaParche)values (?,?,?,?,?,?) where camiseta_Id = ?";
//			jdbcTemplate.update(sql, miCarritoItems.getCarrito().getId(), miCarritoItems.getCantidad(),
//					miCarritoItems.getTallaSeleccionada(), miCarritoItems.getNombrePersonalizado(),
//					miCarritoItems.getNumeroPersonalizado(), miCarritoItems.isLlevaParche(), unId);
//
//			return "Camiseta Insertada con exito";
//		} catch (Exception e) {
//			return "Camiseta Insertada SIN éxito";
//		}
//	}

}
