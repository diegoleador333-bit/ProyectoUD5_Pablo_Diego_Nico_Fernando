package com.example.demo.CarritoContenido;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Carrito.Carrito;

public class CarritoContenido {
	private Integer id;
	private Carrito carrito;
	private Camisetas camiseta;
	private Integer cantidad;
	private String tallaSeleccionada;
	private String nombrePersonalizado;
	private Integer numeroPersonalizado;
	private boolean llevaParche;

	public CarritoContenido(Integer id, Carrito carrito, Camisetas camiseta, Integer cantidad, String tallaSeleccionada,
			String nombrePersonalizado, Integer numeroPersonalizado, boolean llevaParche) {
		super();
		this.id = id;
		this.carrito = carrito;
		this.camiseta = camiseta;
		this.cantidad = cantidad;
		this.tallaSeleccionada = tallaSeleccionada;
		this.nombrePersonalizado = nombrePersonalizado;
		this.numeroPersonalizado = numeroPersonalizado;
		this.llevaParche = llevaParche;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Carrito getCarrito() {
		return carrito;
	}

	public void setCarrito(Carrito carrito) {
		this.carrito = carrito;
	}

	public Camisetas getCamiseta() {
		return camiseta;
	}

	public void setCamiseta(Camisetas camiseta) {
		this.camiseta = camiseta;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getTallaSeleccionada() {
		return tallaSeleccionada;
	}

	public void setTallaSeleccionada(String tallaSeleccionada) {
		this.tallaSeleccionada = tallaSeleccionada;
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