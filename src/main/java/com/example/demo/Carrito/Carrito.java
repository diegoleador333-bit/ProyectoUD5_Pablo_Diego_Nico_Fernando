package com.example.demo.Carrito;

import java.sql.Date;

import com.example.demo.Usuarios.Usuarios;

public class Carrito {
	private long id;
	private Usuarios usuario;
	private double precioTotal;
	private Date fechaCreacion;

	public Carrito(long id, Usuarios usuario, double precioTotal, Date fechaCreacion) {
		this.id = id;
		this.usuario = usuario;
		this.precioTotal = precioTotal;
		this.fechaCreacion = fechaCreacion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
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
