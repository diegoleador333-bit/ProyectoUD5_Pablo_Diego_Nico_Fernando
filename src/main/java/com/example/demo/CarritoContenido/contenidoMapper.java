package com.example.demo.CarritoContenido;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Carrito.Carrito;

public class contenidoMapper implements RowMapper<CarritoContenido> {
	@Override
	public CarritoContenido mapRow(ResultSet rs, int rowNum) throws SQLException {
		Integer dorsal = (Integer) rs.getObject("numeroPersonalizado");

		return new CarritoContenido(rs.getInt("id"), rs.getInt("carrito_Id"), rs.getInt("camiseta_Id"),
				rs.getInt("cantidad"), rs.getString("tallaSeleccionada"), rs.getString("nombrePersonalizado"), dorsal,
				rs.getBoolean("llevaParche"));
	}
}
