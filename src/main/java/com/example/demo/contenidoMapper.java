package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class contenidoMapper implements RowMapper<CarritoContenido> { // también podemos usar un ResultSetExtractor
	public CarritoContenido mapRow(ResultSet rs, int rowNum) throws SQLException {
		CarritoContenido contenido = new CarritoContenido(rs.getInt("id"), (Carrito) rs.getObject("carrito_Id"),
				(Camisetas) rs.getObject("camiseta_Id"), rs.getInt("cantidad"));
		return contenido;
	} // este método recibirá cada fila devuelta por la query
}