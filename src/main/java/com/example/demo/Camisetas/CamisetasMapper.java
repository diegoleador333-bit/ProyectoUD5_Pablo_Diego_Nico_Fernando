package com.example.demo.Camisetas;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CamisetasMapper implements RowMapper<Camisetas> {
	public Camisetas mapRow(ResultSet rs, int rowNum) throws SQLException {
		Camisetas camiseta = new Camisetas(rs.getInt("id"), rs.getString("equipo"), rs.getString("imagen"),
				rs.getDouble("precio"), rs.getString("temporada"), rs.getString("liga"), rs.getBoolean("parche"),
				rs.getString("nombreDorsal"), rs.getInt("numeroDorsal"));
		return camiseta;
	}
}
