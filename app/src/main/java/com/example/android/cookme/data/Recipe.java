package com.example.android.cookme.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Recipe implements Serializable {

	private String name;
	private LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
	private String instructions;
	private String image;

	public Recipe(String name, LinkedList<Ingredient> ingredients, String instructions, String image){
		this.name = name;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.image = image;
	}

	//Constructor in case it recieves an array instead of list of ingredients
	public Recipe(String name, Ingredient[] ing, String instructions){
		this.name = name;
		this.instructions = instructions;

		ingredients = new LinkedList<Ingredient>();

		for(int i = 0; i < ing.length; i++){
			ingredients.add(ing[i]);
		}
	}

	public String getName(){
		return name;
	}

	public String getImage(){ return  image; }

	public LinkedList<Ingredient> getIngredients(){
		return ingredients;
	}

	public String getInstructions(){
		return instructions;
	}

	public boolean hasIngredient(String name_ingredient){
		for(Ingredient ing : ingredients){
			if(ing.getName().toLowerCase().equals(name_ingredient.toLowerCase()))
				return true;
		}
		return false;
	}

	public ArrayList<String> getStringsOfIngredients(){
		ArrayList<String> ingredientsStrings = new ArrayList<>();
		for(Ingredient ing : ingredients)
			ingredientsStrings.add(ing.getName() + " " + ing.getQuantity() + " " + ing.getUnits());
		return ingredientsStrings;
	}


	/*
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(name);
		dest.writeString(instructions);
		dest.writeString(image);
		dest.writeTypedList(ingredients);

	}


    public Recipe(Parcel source){

        this.name = source.readString();
        Log.i("Name is :", name);
        this.instructions = source.readString();
        this.image = source.readString();
       // this.ingredients = null;
        source.readTypedList(this.ingredients, Ingredient.CREATOR);

    }



    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

	*/


}