package com.example.android.cookme.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eduardovaca on 16/07/15.
 */
public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.cookme";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_INGREDIENT = "ingredient";
    public static final String PATH_RELATIONSHIP = "recipe_ingredient_relationship";

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

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPE;

        public Uri buildRecipeUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class IngredientEntry implements BaseColumns{

        //Name of the table
        public static final String TABLE_NAME = "ingredient";

        //Name of the ingredient
        public static final String COL_NAME = "name";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENT;

        public Uri buildIngredientUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
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

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RELATIONSHIP).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONSHIP;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RELATIONSHIP;

        public Uri buildRelationshipUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
