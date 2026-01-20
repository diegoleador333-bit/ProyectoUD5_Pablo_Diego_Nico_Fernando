package com.example.demo.CarritoContenido;

import com.example.demo.Camisetas.Camisetas;
import com.example.demo.Carrito.Carrito;

public class CarritoContenido {
	private int id;
	private int carrito;
	private int camiseta;
	private int cantidad;
	private String tallaSeleccionada;
	private String nombrePersonalizado;
	private Integer numeroPersonalizado;
	private boolean llevaParche;

	public CarritoContenido(int id, int carrito, int camiseta, int cantidad, String tallaSeleccionada,
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCarrito() {
		return carrito;
	}

	public void setCarrito(int carrito) {
		this.carrito = carrito;
	}

	public int getCamiseta() {
		return camiseta;
	}

	public void setCamiseta(int camiseta) {
		this.camiseta = camiseta;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
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