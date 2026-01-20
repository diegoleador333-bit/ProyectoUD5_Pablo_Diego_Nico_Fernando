package com.example.demo.Stock;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.demo.Camisetas.Camisetas;

public class StockMapper {
	public StockPorTalla mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new StockPorTalla(rs.getInt("id"), rs.getInt("camiseta_Id"), rs.getInt("stockS"), rs.getInt("stockM"),
				rs.getInt("stockL"), rs.getInt("stockXL"));
	}
}
