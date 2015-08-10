package com.example.android.cookme.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ingredient implements Parcelable, Serializable{

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(name);
		parcel.writeDouble(quantity);
		parcel.writeString(unit);
	}

	public Ingredient (Parcel in){
		name = in.readString();
		quantity = in.readDouble();
		unit = in.readString();
	}

	public static final Parcelable.Creator<Ingredient> CREATOR
			= new Parcelable.Creator<Ingredient>() {
		public Ingredient createFromParcel(Parcel in) {
			return new Ingredient(in);
		}

		public Ingredient[] newArray(int size) {
			return new Ingredient[size];
		}
	};

}