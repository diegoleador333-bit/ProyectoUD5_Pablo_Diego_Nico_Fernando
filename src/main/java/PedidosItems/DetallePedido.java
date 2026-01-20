package PedidosItems;

import com.example.demo.Camisetas.Camisetas;

import Pedidos.Pedidos;

public class DetallePedido {

	private int id;
	private int pedidoId;
	private int camisetaId;
	private int cantidad;
	private String talla;
	private String nombrePersonalizado;
	private Integer numeroPersonalizado; // Posible Nulo
	private boolean llevaParche;

	public DetallePedido() {
	}

	public DetallePedido(int id, int pedidoId, int camisetaId, int cantidad, String talla,
			String nombrePersonalizado, Integer numeroPersonalizado, boolean llevaParche) {
		this.id = id;
		this.pedidoId = pedidoId;
		this.camisetaId = camisetaId;
		this.cantidad = cantidad;
		this.talla = talla;
		this.nombrePersonalizado = nombrePersonalizado;
		this.numeroPersonalizado = numeroPersonalizado;
		this.llevaParche = llevaParche;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(int pedidoId) {
		this.pedidoId = pedidoId;
	}

	public int getCamisetaId() {
		return camisetaId;
	}

	public void setCamisetaId(int camisetaId) {
		this.camisetaId = camisetaId;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getTalla() {
		return talla;
	}

	public void setTalla(String talla) {
		this.talla = talla;
	}

	public String getNombrePersonalizado() {
		return nombrePersonalizado;
	}

	public void setNombrePersonalizado(String nombrePersonalizado) {
		this.nombrePersonalizado = nombrePersonalizado;
	}

	public Integer getNumeroPersonalizado() {
		return numeroPersonalizado;
	}

	public void setNumeroPersonalizado(Integer numeroPersonalizado) {
		this.numeroPersonalizado = numeroPersonalizado;
	}

	public boolean isLlevaParche() {
		return llevaParche;
	}

	public void setLlevaParche(boolean llevaParche) {
		this.llevaParche = llevaParche;
	}


}