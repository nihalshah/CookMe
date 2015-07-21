package com.example.android.cookme.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by eduardovaca on 21/07/15.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final String INGREDIENT = "Tomato";
    private static final long RECIPE_ID = 10L;
    private static long INGREDIENT_ID = 20L;
    private static final String RECIPE = "Guacamole";

    private static final Uri TEST_RECIPE_DIR = RecipeContract.RecipeEntry.CONTENT_URI;
    private static final Uri TEST_RECIPE_ID_INGREDIENT_DIR = RecipeContract.RecipeEntry.
            buildIngredientsDirUri(Long.toString(RECIPE_ID));
    private static final Uri TEST_INGREDIENT_ID_RECIPES_DIR = RecipeContract.IngredientEntry.
            buildRecipesDirUri(Long.toString(INGREDIENT_ID));
    private static final Uri TEST_ING_ID_RECIPE_ID = RecipeContract.RecipeIngredientRelationship.
            buildRecipeItemUri(Long.toString(INGREDIENT_ID), Long.toString(RECIPE_ID));

    private static final Uri TEST_INGREDIENT_DIR = RecipeContract.IngredientEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = RecipeProvider.buildUriMatcher();

        assertEquals("Error: The RECIPE URI was matched incorrectly.",
                testMatcher.match(TEST_RECIPE_DIR), RecipeProvider.RECIPE);
        assertEquals("Error: The RECIPE ID INGREDIENTS URI was matched incorrectly.",
                testMatcher.match(TEST_RECIPE_ID_INGREDIENT_DIR), RecipeProvider.RECIPE_ID_INGREDIENTS);
        assertEquals("Error: The INGREDIENT ID RECIPES URI was matched incorrectly.",
                testMatcher.match(TEST_INGREDIENT_ID_RECIPES_DIR), RecipeProvider.INGREDIENT_ID_RECIPES);
        assertEquals("Error: The INGREDIENT ID RECIPE ID URI was matched incorrectly.",
                testMatcher.match(TEST_ING_ID_RECIPE_ID), RecipeProvider.INGREDIENT_ID_RECIPE_ID);
        assertEquals("Error: The INGREDIENT URI was matched incorrectly.",
                testMatcher.match(TEST_INGREDIENT_DIR), RecipeProvider.INGREDIENT);

    }
}
