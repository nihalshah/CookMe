package com.example.android.cookme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.android.cookme.data.RecipeContract;

/**
 * Created by eduardovaca on 23/07/15.
 */
public class Utility {

    public static ContentValues createRelationshipValues(long recipe_id, long ingredient_id,
                                                     String units, int quantity){
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
