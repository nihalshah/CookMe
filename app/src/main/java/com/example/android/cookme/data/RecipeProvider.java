package com.example.android.cookme.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by eduardovaca on 20/07/15.
 */
public class RecipeProvider extends ContentProvider {


    private static final SQLiteQueryBuilder sRecipeIngredientQueryBuilder;

    static{
        sRecipeIngredientQueryBuilder = new SQLiteQueryBuilder();

        //Inner join of all tables
        sRecipeIngredientQueryBuilder.setTables(
                RecipeContract.RecipeIngredientRelationship.TABLE_NAME + " INNER JOIN " +
                        RecipeContract.RecipeEntry.TABLE_NAME + " ON " +
                        RecipeContract.RecipeIngredientRelationship.TABLE_NAME + "." +
                        RecipeContract.RecipeIngredientRelationship.COL_RECIPE_KEY + " = " +
                        RecipeContract.RecipeEntry.TABLE_NAME + "." +
                        RecipeContract.RecipeEntry._ID +
                        " INNER JOIN " + RecipeContract.IngredientEntry.TABLE_NAME + " ON " +
                        RecipeContract.RecipeIngredientRelationship.TABLE_NAME + "." +
                        RecipeContract.RecipeIngredientRelationship.COL_INGREDIENT_KEY + " = " +
                        RecipeContract.IngredientEntry.TABLE_NAME + "." +
                        RecipeContract.IngredientEntry._ID);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
