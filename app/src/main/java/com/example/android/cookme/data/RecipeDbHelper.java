package com.example.android.cookme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.cookme.data.RecipeContract.RecipeEntry;
import com.example.android.cookme.data.RecipeContract.IngredientEntry;
import com.example.android.cookme.data.RecipeContract.RecipeIngredientRelationship;

/**
 * Created by eduardovaca on 17/07/15.
 */
public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "recipe.db";

    public RecipeDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //String for creating the Recipe Table
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COL_NAME + " TEXT NOT NULL, " +
                RecipeEntry.COL_INSTRUCTIONS + " TEXT NOT NULL " + //When we put the Photo we must add a coma
               // RecipeEntry.COL_PHOTO + " BLOB " +
                ");";

        //String for creating the Ingredient Table
        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COL_NAME + " TEXT NOT NULL " +
                ");";

        


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
