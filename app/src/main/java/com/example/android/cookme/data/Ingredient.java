package com.example.android.cookme.data;

import java.io.Serializable;

public class Ingredient implements Serializable{

	//enum for having the Units of measure as constants
//	public enum Units {ML, CUPS, SPOONS, QUARTS, GRAMS};

	private String name;
	private double quantity;
//	private Units unit;
	private String unit;
	

	public Ingredient(String name, double quantity, String unit){
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

	public String getUnits(){
		return unit;
	}

}