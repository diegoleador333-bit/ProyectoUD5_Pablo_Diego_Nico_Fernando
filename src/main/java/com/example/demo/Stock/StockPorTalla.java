package com.example.demo.Stock;

import com.example.demo.Camisetas.Camisetas;

public class StockPorTalla {
	private int id;
	private Camisetas camiseta;
	private int stockS;
	private int stockM;
	private int stockL;
	private int stockXL;

	public StockPorTalla(int id, Camisetas camiseta, int stockS, int stockM, int stockL, int stockXL) {
		this.id = id;
		this.camiseta = camiseta;
		this.stockS = stockS;
		this.stockM = stockM;
		this.stockL = stockL;
		this.stockXL = stockXL;
	}
	
	public StockPorTalla() {
		
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Camisetas getCamiseta() {
		return camiseta;
	}

	public void setCamiseta(Camisetas camiseta) {
		this.camiseta = camiseta;
	}

	public int getStockS() {
		return stockS;
	}

	public void setStockS(int stockS) {
		this.stockS = stockS;
	}

	public int getStockM() {
		return stockM;
	}

	public void setStockM(int stockM) {
		this.stockM = stockM;
	}

	public int getStockL() {
		return stockL;
	}

	public void setStockL(int stockL) {
		this.stockL = stockL;
	}

	public int getStockXL() {
		return stockXL;
	}

	public void setStockXL(int stockXL) {
		this.stockXL = stockXL;
	}

}
