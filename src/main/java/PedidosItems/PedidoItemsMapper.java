package PedidosItems;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.demo.Camisetas.Camisetas;

import Pedidos.Pedidos;

public class PedidoItemsMapper {
	public DetallePedido mapRow(ResultSet rs, int rowNum) throws SQLException {
		DetallePedido contenido = new DetallePedido(rs.getInt("id"), (Pedidos) rs.getObject("pedido_Id"),
				(Camisetas) rs.getObject("camiseta_Id"), rs.getInt("cantidad"), rs.getString("tallaSeleccionada"),
				rs.getString("nombrePersonalizado"), rs.getInt("numeroPersonalizado"), rs.getBoolean("llevaParche"));
		return contenido;
	}
}
