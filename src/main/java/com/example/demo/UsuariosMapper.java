package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UsuariosMapper implements RowMapper<Usuarios> {
	@Override
	public Usuarios mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Usuarios(rs.getLong("id"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("dni"),
				rs.getString("correo"), rs.getString("password"));
	}
}
