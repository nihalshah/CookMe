package com.example.android.cookme.data;

import android.content.ContentValues;

/**
 * Created by eduardovaca on 20/07/15.
 */
public class TestUtilities {

    static ContentValues createRecipeGuacamoleValues(){
        ContentValues testValues = new ContentValues();
        String inst = "1.- Peel and mash avocados in a medium serving bowl. Stir in onion, garlic, tomato, lime juice, salt and pepper. Season with remaining lime juice and salt and pepper to taste. Chill for half an hour to blend flavors.";
        testValues.put(RecipeContract.RecipeEntry.COL_NAME, "Guacamole");
        testValues.put(RecipeContract.RecipeEntry.COL_INSTRUCTIONS, inst);
        return testValues;
    }
}
