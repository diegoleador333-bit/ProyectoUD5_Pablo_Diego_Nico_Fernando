package com.example.demo.Carrito;

import java.sql.Date;

import com.example.demo.Usuarios.Usuarios;

public class Carrito {
	private int id;
	private int usuario;
	private double precioTotal;
	private Date fechaCreacion;

	public Carrito(int id, int usuario, double precioTotal, Date fechaCreacion) {
		this.id = id;
		this.usuario = usuario;
		this.precioTotal = precioTotal;
		this.fechaCreacion = fechaCreacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public double getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

}
