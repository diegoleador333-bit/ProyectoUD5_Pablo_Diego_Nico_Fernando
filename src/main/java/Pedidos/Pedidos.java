package Pedidos;

import java.sql.Date;

import com.example.demo.Usuarios.Usuarios;

public class Pedidos {

	// Atributos
	private int id;
	private Usuarios usuarioId; // Mapeo de usuario_Id
	private Date fechaPedido;
	private double precioTotal;

	public Pedidos() {
	}

	public Pedidos(int id, Usuarios usuarioId, Date fechaPedido, double precioTotal) {
		this.id = id;
		this.usuarioId = usuarioId;
		this.fechaPedido = fechaPedido;
		this.precioTotal = precioTotal;
	}

	// Getters y Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuarios getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Usuarios usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public double getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}

}
