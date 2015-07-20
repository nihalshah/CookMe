package com.example.android.cookme.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by eduardovaca on 20/07/15.
 */
public class TestRecipeContract extends AndroidTestCase {

    public static final String RECIPE_NAME = "/Guacamole";
    public static final String INGREDIENT_NAME = "Tomato";


    public void testBuilRecipeIngredientByRecipeName(){
        Uri recIngUri = RecipeContract.RecipeIngredientRelationship.builRecipeIngredientbyRecipe(RECIPE_NAME);
        assertNotNull("ERROR: Null uri returned");
        assertEquals("Error: Recipe Name not properly appended to the ern of Uri",
                RECIPE_NAME, recIngUri.getLastPathSegment());
        assertEquals("ERROR: Uri doesnt match our expected result", recIngUri.toString(),
                "content://com.example.android.cookme/recipe_ingredient_relationship/%2FGuacamole");
    }
}
