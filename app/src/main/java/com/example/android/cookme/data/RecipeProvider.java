package com.example.android.cookme.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by eduardovaca on 20/07/15.
 */
public class RecipeProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final SQLiteQueryBuilder sRecipeIngredientQueryBuilder;

    static final int RECIPE = 100;
    static final int RECIPE_WITH_INGREDIENT = 101;
    static final int INGREDIENT = 102;

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


    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, RecipeContract.PATH_RECIPE, RECIPE);
        matcher.addURI(authority, RecipeContract.PATH_RELATIONSHIP + "/*/*", RECIPE_WITH_INGREDIENT);
        matcher.addURI(authority, RecipeContract.PATH_INGREDIENT, INGREDIENT);

        return matcher;
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
