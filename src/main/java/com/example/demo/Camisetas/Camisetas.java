package com.example.demo.Camisetas;

public class Camisetas {
	private int id;
	private String equipo;
	private String imagen;
	private double precio;
	private String temporada;
	private String liga;

	public Camisetas(int id, String equipo, String imagen, double precio, String temporada, String liga, boolean parche,
			String nombreDorsal, int numeroDorsal) {
		this.id = id;
		this.equipo = equipo;
		this.imagen = imagen;
		this.precio = precio;
		this.temporada = temporada;
		this.liga = liga;
	}

	public Camisetas(int id, String equipo, String imagen, double precio, String temporada, String liga) {
		this.id = id;
		this.equipo = equipo;
		this.imagen = imagen;
		this.precio = precio;
		this.temporada = temporada;
		this.liga = liga;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getTemporada() {
		return temporada;
	}

	public void setTemporada(String temporada) {
		this.temporada = temporada;
	}

	public String getLiga() {
		return liga;
	}

	public void setLiga(String liga) {
		this.liga = liga;
	}




}
