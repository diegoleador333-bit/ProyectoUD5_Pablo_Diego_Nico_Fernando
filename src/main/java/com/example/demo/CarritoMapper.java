package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CarritoMapper implements RowMapper<Carrito> { // también podemos usar un ResultSetExtractor
	public Carrito mapRow(ResultSet rs, int rowNum) throws SQLException {
		Carrito carrito = new Carrito(rs.getInt("id"), (Usuarios) rs.getObject("usuario_Id"),
				rs.getDouble("precioTotal"), rs.getDate("fechaCreacion"));
		return carrito;
	} // este método recibirá cada fila devuelta por la query
}