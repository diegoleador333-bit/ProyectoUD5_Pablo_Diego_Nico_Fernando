package com.example.demo.CarritoContenido;

public class CarritoContenido {
	private Integer id;
	private Integer carrito;
	private Integer camiseta;
	private Integer cantidad;
	private String tallaSeleccionada;
	private String nombrePersonalizado;
	private Integer numeroPersonalizado;
	private boolean llevaParche;

	public CarritoContenido(Integer id, Integer carrito, Integer camiseta, Integer cantidad, String tallaSeleccionada,
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

	public Integer getCarrito() {
		return carrito;
	}

	public void setCarrito(Integer carrito) {
		this.carrito = carrito;
	}

	public Integer getCamiseta() { return camiseta; }
    public void setCamiseta(Integer camiseta) { this.camiseta = camiseta; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

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