package com.example.android.cookme.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.android.cookme.Utility;

/**
 * Created by eduardovaca on 20/07/15.
 */
public class RecipeProvider extends ContentProvider {

    private static final String LOG_TAG = RecipeProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDbHelper mDbHelper;

    private static final SQLiteQueryBuilder sRecipeIngredientQueryBuilder;

    static final int RECIPE = 100;
    static final int RECIPE_ID_INGREDIENTS = 101;
    static final int INGREDIENT_ID_RECIPES = 102;
    static final int INGREDIENT_ID_RECIPE_ID = 103;
    static final int INGREDIENT = 104;
    static final int RELATIONSHIP = 105;

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

    private static final String sIngredientSelection =
            RecipeContract.IngredientEntry.TABLE_NAME + "." +
                    RecipeContract.IngredientEntry.COL_NAME + " = ? ";

    private Cursor getRecipesByIngredient(Uri uri, String[] projection, String sortOrder){
        String ingredientName = RecipeContract.IngredientEntry.getIngredientFromUri(uri);

        String [] selectionArgs = new String[]{ingredientName};
        String selection = sIngredientSelection;

        return sRecipeIngredientQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor getAllRelationships(Uri uri, String[] projection, String sortOrder){

        return  sRecipeIngredientQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder);
    }



    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, RecipeContract.PATH_RECIPE, RECIPE);
        matcher.addURI(authority, RecipeContract.PATH_RECIPE + "/*/" + RecipeContract.PATH_INGREDIENT,
                RECIPE_ID_INGREDIENTS);
        matcher.addURI(authority, RecipeContract.PATH_INGREDIENT + "/*/" + RecipeContract.PATH_RECIPE,
                INGREDIENT_ID_RECIPES);
        matcher.addURI(authority, RecipeContract.PATH_RELATIONSHIP + "/*/*", INGREDIENT_ID_RECIPE_ID);
        matcher.addURI(authority, RecipeContract.PATH_INGREDIENT, INGREDIENT);
        matcher.addURI(authority, RecipeContract.PATH_RELATIONSHIP, RELATIONSHIP);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor returnCursor;

        switch (sUriMatcher.match(uri)){

            case INGREDIENT_ID_RECIPES:{
                returnCursor = getRecipesByIngredient(uri, projection, sortOrder);
                break;
            }
            case INGREDIENT:{
                returnCursor = mDbHelper.getReadableDatabase().query(
                        RecipeContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RECIPE:{
                returnCursor = mDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RELATIONSHIP:{
                returnCursor = getAllRelationships(uri, projection, sortOrder);
                /*returnCursor = mDbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeIngredientRelationship.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );*/
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match){
            case INGREDIENT_ID_RECIPE_ID:
                return RecipeContract.RecipeIngredientRelationship.CONTENT_ITEM_TYPE;
            case INGREDIENT_ID_RECIPES:
                return RecipeContract.IngredientEntry.CONTENT_TYPE;
            case RECIPE_ID_INGREDIENTS:
                return RecipeContract.RecipeEntry.CONTENT_TYPE;
            case RECIPE:
                return RecipeContract.RecipeEntry.CONTENT_TYPE;
            case INGREDIENT:
                return RecipeContract.IngredientEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case RECIPE:{
                long _id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, contentValues);
                if(_id > 0)
                    returnUri = RecipeContract.RecipeEntry.buildRecipeUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case INGREDIENT:{
                long _id = db.insert(RecipeContract.IngredientEntry.TABLE_NAME, null, contentValues);
                if(_id > 0)
                    returnUri = RecipeContract.IngredientEntry.buildIngredientUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RELATIONSHIP:{
                long _id = db.insert(RecipeContract.RecipeIngredientRelationship.TABLE_NAME, null, contentValues);
                if(_id > 0)
                    returnUri = RecipeContract.RecipeIngredientRelationship.buildRelationshipUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match){
            case RECIPE:{
                rowsDeleted = db.delete(RecipeContract.RecipeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case INGREDIENT:{
                rowsDeleted = db.delete(RecipeContract.IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case RELATIONSHIP:{
                rowsDeleted = db.delete(RecipeContract.RecipeIngredientRelationship.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null deletes all rows
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    public void insertWholeRecipe(ContentValues recipeValues,
                                  ContentValues ingredientValues,
                                  String units, int quantity){

        //Insert in tables Recipe and Ingredient
        Uri recipeInserted = insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);
        Uri ingredientInserted = insert(RecipeContract.IngredientEntry.CONTENT_URI, ingredientValues);

        //Get the id of the rows inserted
        long recipe_id = ContentUris.parseId(recipeInserted);
        long ingredient_id = ContentUris.parseId(ingredientInserted);

        //Insert in the table of relationship
        ContentValues relationValues = Utility.createRelationshipValues(recipe_id, ingredient_id, units, quantity);
        insert(RecipeContract.RecipeIngredientRelationship.CONTENT_URI, relationValues);

        Log.v(LOG_TAG, "Insertion of entire recipe completed!");

    }


    //TODO: add a bulkInsert method if we use the API of recipes
}
