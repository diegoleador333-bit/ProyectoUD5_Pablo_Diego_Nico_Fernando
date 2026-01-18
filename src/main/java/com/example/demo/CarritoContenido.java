	package com.example.demo;
	
	public class CarritoContenido {
		private int id;
		private Carrito carrito;
		private Camisetas camiseta;
		private int cantidad;
	
		public CarritoContenido(int id, Carrito carrito, Camisetas camiseta, int cantidad) {
			this.id = id;
			this.carrito = carrito;
			this.camiseta = camiseta;
			this.cantidad = cantidad;
		}
	
		public int getId() {
			return id;
		}
	
		public void setId(int id) {
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
	
		public int getCantidad() {
			return cantidad;
		}
	
		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
	
		
	}
