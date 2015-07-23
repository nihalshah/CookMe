package com.example.android.cookme;

import android.content.ContentValues;

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
}
