package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuariosMapper {
	public Usuarios mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Usuarios(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("apelldio"),
            rs.getString("dni"),
            rs.getString("correo"),
            rs.getString("password")
        );
    }
}
