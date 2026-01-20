package com.example.demo.Carrito;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.Usuarios.Usuarios;


public class CarritoMapper implements RowMapper<Carrito> {
	  @Override
	  public Carrito mapRow(ResultSet rs, int rowNum) throws SQLException {
	    
	    return new Carrito(
	      rs.getInt("id"),
	      rs.getInt("usuario_Id"),
	      rs.getDouble("precioTotal"),
	      rs.getDate("fechaCreacion")
	    );
	  }
	}

