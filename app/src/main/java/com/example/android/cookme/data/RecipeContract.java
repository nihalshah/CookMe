package com.example.android.cookme.data;

import android.provider.BaseColumns;

/**
 * Created by eduardovaca on 16/07/15.
 */
public class RecipeContract {

    /*Inner class that defines the table contents for the Recipe table */
    public static final class RecipeEntry implements BaseColumns{

        //Name of the table
        public static final String TABLE_NAME = "recipe";

        //Name of the recipe
        public static final String COL_NAME = "name";

        //Instructions of the recipe
        public static final String COL_INSTRUCTIONS = "instructions";

        //Photo of the recipe
        public static final String COL_PHOTO = "photo";
    }

    public static final class IngredientEntry implements BaseColumns{

        //Name of the table
        public static final String TABLE_NAME = "ingredient";

        //Name of the ingredient
        public static final String COL_NAME = "name";
    }

    //This class doesn't implements BaseColumns cause it doesn't need an autoincrement key
    public static final class RecipeIngredientRelationship{

        //Name of table
        public static final String TABLE_NAME = "recipe_ingredient_relationship";

        //Foreign key from RecipeTable
        public static final String COL_RECIPE_KEY = "recipe_id";

        //Foreign key from IngredientTable
        public static final String COL_INGREDIENT_KEY = "ingredient_id";

        //Units of the ingredient in an specific recipe
        public static final String COL_UNITS = "units";

        //Quantity of each ingredient in an specific recipe
        public static final String COL_QUANTITY = "quantity";
    }
}
