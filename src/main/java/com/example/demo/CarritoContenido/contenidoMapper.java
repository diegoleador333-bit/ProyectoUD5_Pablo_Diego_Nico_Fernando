package com.example.demo.CarritoContenido;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Carrito.Carrito;

public class contenidoMapper implements RowMapper<CarritoContenido> {
	@Override
	public CarritoContenido mapRow(ResultSet rs, int rowNum) throws SQLException {

		Carrito c = new Carrito(rs.getLong("carrito_Id"), null, 
				0, null);

		Camisetas cam = new Camisetas(rs.getInt("camiseta_Id"), null, null, 0.0, null, null);

		Integer dorsal = (Integer) rs.getObject("numeroPersonalizado"); 

		return new CarritoContenido(rs.getInt("id"), c, cam, rs.getInt("cantidad"), rs.getString("tallaSeleccionada"),
				rs.getString("nombrePersonalizado"), dorsal, rs.getBoolean("llevaParche"));
	}
}
