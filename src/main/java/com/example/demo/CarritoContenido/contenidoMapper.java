package com.example.demo.CarritoContenido;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Carrito.Carrito;

public class contenidoMapper implements RowMapper<CarritoContenido> { // también podemos usar un ResultSetExtractor
	public CarritoContenido mapRow(ResultSet rs, int rowNum) throws SQLException {
		CarritoContenido contenido = new CarritoContenido(rs.getInt("id"), (Carrito) rs.getObject("carrito_Id"),
				(Camisetas) rs.getObject("camiseta_Id"), rs.getInt("cantidad"), rs.getString("tallaSeleccionada"),
				rs.getString("nombrePersonalizado"), rs.getInt("numeroPersonalizado"), rs.getBoolean("llevaParche"));
		return contenido;
	} // este método recibirá cada fila devuelta por la query
}