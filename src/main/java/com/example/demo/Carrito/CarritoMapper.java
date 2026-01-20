package com.example.demo.Carrito;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.Usuarios.Usuarios;

public class CarritoMapper implements RowMapper<Carrito> { // también podemos usar un ResultSetExtractor
	public Carrito mapRow(ResultSet rs, int rowNum) throws SQLException {
		Carrito carrito = new Carrito(rs.getInt("id"), (Usuarios) rs.getObject("usuario_Id"),
				rs.getDouble("precioTotal"), rs.getDate("fechaCreacion"));
		return carrito;
	} 
}