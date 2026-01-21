package PedidosItems;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PedidoItemsMapper implements RowMapper<DetallePedido> {
	public DetallePedido mapRow(ResultSet rs, int rowNum) throws SQLException {
		DetallePedido contenido = new DetallePedido(rs.getInt("id"), rs.getInt("pedido_Id"), rs.getInt("camiseta_Id"),
				rs.getInt("cantidad"), rs.getString("talla"), rs.getString("nombrePersonalizado"),
				rs.getInt("numeroPersonalizado"), rs.getBoolean("llevaParche"));
		return contenido;
	}
}
