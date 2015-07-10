package com.example.android.cookme;

public class Ingredient{

	public enum Units {ML, CUPS, SPOONS, QUARTS, GRAMS};

	private String name;
	private double quantity;
	private Units unit;
	

	public Ingredient(String name, double quantity, Units unit){
		this.name = name;
		this.quantity = quantity;
		this.unit = unit;
	}

	public String getName(){
		return name;
	}

	public double getQuantity(){
		return quantity;
	}

	public Units getUnits(){
		return unit;
	}

}