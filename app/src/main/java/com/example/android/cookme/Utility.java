package com.example.android.cookme;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.cookme.data.Ingredient;
import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeContract;

import java.util.ArrayList;

/**
 * Created by eduardovaca on 23/07/15.
 */
public class Utility {

    public static ContentValues createRelationshipValues(long recipe_id, long ingredient_id,
                                                     String units, double quantity){
        ContentValues relationValues = new ContentValues();
        relationValues.put(RecipeContract.RecipeIngredientRelationship.COL_RECIPE_KEY, recipe_id);
        relationValues.put(RecipeContract.RecipeIngredientRelationship.COL_INGREDIENT_KEY, ingredient_id);
        relationValues.put(RecipeContract.RecipeIngredientRelationship.COL_UNITS, units);
        relationValues.put(RecipeContract.RecipeIngredientRelationship.COL_QUANTITY, quantity);
        return relationValues;
    }

    public static ContentValues createRecipeValues(String name, String instructions){
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeContract.RecipeEntry.COL_NAME, name);
        recipeValues.put(RecipeContract.RecipeEntry.COL_INSTRUCTIONS, instructions);
        return recipeValues;
    }

    public static ContentValues createIngredientValues(String name){
        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put(RecipeContract.IngredientEntry.COL_NAME, name);
        return ingredientValues;
    }

    public static boolean dataBaseIsEmpty(Context context){
        Cursor cursor = context.getContentResolver().query(
                RecipeContract.RecipeIngredientRelationship.CONTENT_URI,
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst())
            return false;
        return true;
    }

    /*Method that checks if ingredient is already in DB, it return the id*/
    public static long getIngredientId(Context context, String ingredientName){

        String selection = RecipeContract.IngredientEntry.COL_NAME + " = ? ";
        String selectionArgs [] = new String[]{ingredientName};

        Cursor cursor = context.getContentResolver().query(
                RecipeContract.IngredientEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);

        if(cursor.moveToFirst())
            return cursor.getLong(cursor.getColumnIndex("_id"));

        return -1L;
    }

    /*Method that insert the whole recipe to the DB*/
    //TODO: this methos will change when we add more than one ingredient by recipe
    public static void insertWholeRecipeInDb(Context context, String recipeName, String instructions,
                                             String ingredientName, String units, double quantity){

        ContentValues recipeValues = createRecipeValues(recipeName, instructions);
        Uri recipeInserted = context.getContentResolver().insert(
                RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);
        long recipe_id = ContentUris.parseId(recipeInserted);

        //check if ingredient exists
        long ingredient_id = getIngredientId(context, ingredientName);
        if(ingredient_id == -1L){
            //Ingredient doesn't exists, so we add it
            ContentValues ingredientValues = createIngredientValues(ingredientName);
            Uri ingredient_inserted = context.getContentResolver().insert(
                    RecipeContract.IngredientEntry.CONTENT_URI,
                    ingredientValues);
            ingredient_id = ContentUris.parseId(ingredient_inserted);
        }else{
            Log.v(null, "USING EXISTING ID!!! " + ingredient_id);
        }

        ContentValues relationValues = createRelationshipValues(recipe_id, ingredient_id, units, quantity);

        context.getContentResolver().insert(RecipeContract.RecipeIngredientRelationship.CONTENT_URI,
                                            relationValues);

        Log.v(null, "RECIPE INSERTED!!!!");

    }

    /*Method that insert a whole arrayList of Recipes to the Db
    I would do this on Async Task but is just one time, and its for testing*/
    public static void insertJSONRecipesToDb(Context context, ArrayList<Recipe> list_recipes){

        for(Recipe recipe : list_recipes){
            //Insert the recipe in table Recipe
            ContentValues recipeValues = createRecipeValues(recipe.getName(), recipe.getInstructions());
            Uri recipe_inserted = context.getContentResolver().insert(
                    RecipeContract.RecipeEntry.CONTENT_URI,
                    recipeValues);
            long recipe_id = ContentUris.parseId(recipe_inserted);
            for(Ingredient ingredient : recipe.getIngredients()){
                //Insert the ingredients of recipe
                long ingredient_id = getIngredientId(context, ingredient.getName());
                if(ingredient_id == -1){
                    //Ingredient doesn't exists, so we add it
                    ContentValues ingredientValues = createIngredientValues(ingredient.getName());
                    Uri ingredient_inserted = context.getContentResolver().insert(
                            RecipeContract.IngredientEntry.CONTENT_URI,
                            ingredientValues);
                    ingredient_id = ContentUris.parseId(ingredient_inserted);
                }
                //Now we add the relation
                ContentValues relationValues = createRelationshipValues(recipe_id,
                                                                        ingredient_id,
                                                                        ingredient.getUnits(),
                                                                        ingredient.getQuantity());
                context.getContentResolver().insert(RecipeContract.RecipeIngredientRelationship.CONTENT_URI,
                                                    relationValues);
            }
        }
        Log.v(null, "JSON Already in DB!!!!!");
    }



    /* Careful with this method, it deletes all records in local DB */
    public static void deleteAllRecordsFromDb(Context context){
            context.getContentResolver().delete(
                    RecipeContract.RecipeEntry.CONTENT_URI,
                    null,
                    null
            );
            context.getContentResolver().delete(
                    RecipeContract.IngredientEntry.CONTENT_URI,
                    null,
                    null
            );
            context.getContentResolver().delete(
                    RecipeContract.RecipeIngredientRelationship.CONTENT_URI,
                    null,
                    null
            );
    }


}
