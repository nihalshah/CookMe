package com.example.android.cookme.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Nihal on 7/11/15.
 */
public class RecipeProvider {

    private ArrayList<Recipe> collection_of_recipes;
    private static final String LOG_TAG = RecipeProvider.class.getSimpleName();
/*

    Constructor :
        Uses both readJsonFromAssets as well as getIngredients function
        Creates an array of Recipes.

 */
    public RecipeProvider(Context context){

        collection_of_recipes = new ArrayList<>();

        try {
            JSONObject jsonRecipe = new JSONObject(readJsonFromAssets(context));
            JSONArray RecipeArray = jsonRecipe.getJSONArray("recipe");

            for( int i = 0; i < RecipeArray.length(); i++){
                JSONObject dish = RecipeArray.getJSONObject(i);

                String name = dish.getString("name");
                LinkedList<Ingredient>allIngredients = getIngredients(dish);
                String instructions = dish.getString("instructions");
                Log.v(LOG_TAG, "Adding Recipe to collection...");
                collection_of_recipes.add(new Recipe(name, allIngredients ,instructions));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /* Return an ArrayList of Strings with only the name of each Recipe to populate the ListView */
    public ArrayList<String> getRecipeNames(){

        ArrayList<String> recipe_names = new ArrayList<>();
        for(Recipe recipe : collection_of_recipes){
            recipe_names.add(recipe.getName());
        }
        return recipe_names;
    }
/*
    Returns a list of ingredients in the appropriate format
 */
    private LinkedList<Ingredient> getIngredients(JSONObject dish) {

        JSONObject ingredientInfo = null;
        LinkedList<Ingredient> allIngredients = new LinkedList<Ingredient>();
        try {
            ingredientInfo = dish.getJSONObject("ingredients");

            double quantity = Double.parseDouble(ingredientInfo.getString("ingredient quantity"));
            String ingredientName = ingredientInfo.getString("ingredient name");
            String ingredientUnit = ingredientInfo.getString("ingredient units");
            Ingredient ingredients = new Ingredient(ingredientName, quantity, ingredientUnit);


            allIngredients.add(ingredients);

         }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return allIngredients;
    }


/*
    Reads the testdata JSON file in the assets folder
    Return a String
 */
    public String readJsonFromAssets(Context context){
        String json = null;
        AssetManager assetManager = context.getResources().getAssets();
        try {
            InputStream jsonReader = assetManager.open("testdata.json");
            int size = jsonReader.available();
            byte[] buffer = new byte[size];
            jsonReader.read(buffer);
            jsonReader.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;

    }
}
