package Pedidos;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.Usuarios.Usuarios;

public class PedidosMapper implements RowMapper<Pedidos> {

	public Pedidos mapRow(ResultSet rs, int rowNum) throws SQLException {
		Pedidos pedidos = new Pedidos(rs.getInt("id"), rs.getInt("usuario_Id"), rs.getDate("fechaPedido"),
				rs.getDouble("precioTotal"));
		return pedidos;
	}
}