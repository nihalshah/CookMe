package com.example.android.cookme.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by eduardovaca on 17/07/15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(RecipeDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(RecipeContract.RecipeEntry.TABLE_NAME);
        tableNameHashSet.add(RecipeContract.IngredientEntry.TABLE_NAME);
        tableNameHashSet.add(RecipeContract.RecipeIngredientRelationship.TABLE_NAME);

        mContext.deleteDatabase(RecipeDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new RecipeDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without three entry tables",
                tableNameHashSet.isEmpty());

        //******************** TRYING RECIPE TABLE *******************************

        // now, does the Recipe table contains the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + RecipeContract.RecipeEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> recipeColumnHashSet = new HashSet<String>();
        recipeColumnHashSet.add(RecipeContract.RecipeEntry._ID);
        recipeColumnHashSet.add(RecipeContract.RecipeEntry.COL_NAME);
        recipeColumnHashSet.add(RecipeContract.RecipeEntry.COL_INSTRUCTIONS);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            recipeColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required Recipe entry columns",
                recipeColumnHashSet.isEmpty());

        //*********************** TRYING INGREDIENT TABLE ******************************

        c = db.rawQuery("PRAGMA table_info(" + RecipeContract.IngredientEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> ingredientColumnHashSet = new HashSet<String>();
        ingredientColumnHashSet.add(RecipeContract.IngredientEntry._ID);
        ingredientColumnHashSet.add(RecipeContract.IngredientEntry.COL_NAME);


        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            ingredientColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required Ingredient entry columns",
                ingredientColumnHashSet.isEmpty());

        //*********************** TRYING RELATIONSHIP TABLE ******************************

        c = db.rawQuery("PRAGMA table_info(" + RecipeContract.RecipeIngredientRelationship.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> relationColumnHashSet = new HashSet<String>();
        relationColumnHashSet.add(RecipeContract.RecipeIngredientRelationship.COL_RECIPE_KEY);
        relationColumnHashSet.add(RecipeContract.RecipeIngredientRelationship.COL_INGREDIENT_KEY);
        relationColumnHashSet.add(RecipeContract.RecipeIngredientRelationship.COL_UNITS);
        relationColumnHashSet.add(RecipeContract.RecipeIngredientRelationship.COL_QUANTITY);


        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            relationColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required Relationship entry columns",
                relationColumnHashSet.isEmpty());

        db.close();
    }
/*
    public void testRecipeTable(){
        insertRecipe();
    }

    public void testIngredientTable(){
        insertIngredient();
    }

    public void testRelationshipTable(){

        long recipeRowId = insertRecipe();
        long ingredientRowId = insertIngredient();

        //Make sure both were inserted
        assertFalse("Error: Recipe Not Inserted Correctly", recipeRowId == -1L);
        assertFalse("Error: Ingredient Not Inserted Correctly", ingredientRowId == -1L);

        RecipeDbHelper dbHelper = new RecipeDbHelper(mContext);
        SQLiteDatabase dB = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createRelationshipValues(recipeRowId, ingredientRowId);

        long relationRowId = dB.insert(RecipeContract.RecipeIngredientRelationship.TABLE_NAME, null, testValues);
        assertTrue(relationRowId != -1);

        Cursor cursor = dB.query(RecipeContract.RecipeIngredientRelationship.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue( "Error: No Records returned from relationship query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error:Relationship Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from relationship query",
                cursor.moveToNext() );

        cursor.close();
        dB.close();
    }


    public long insertRecipe(){

        RecipeDbHelper dbHelper = new RecipeDbHelper(mContext);
        SQLiteDatabase dB = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createRecipeGuacamoleValues();

        long recipeRowId;
        recipeRowId = dB.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, testValues);

        assertTrue(recipeRowId != -1);

        Cursor cursor = dB.query(RecipeContract.RecipeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue( "Error: No Records returned from recipe query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Recipe Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from recipe query",
                cursor.moveToNext() );

        cursor.close();
        dB.close();
        return recipeRowId;
    }

    public long insertIngredient(){
        RecipeDbHelper dbHelper = new RecipeDbHelper(mContext);
        SQLiteDatabase dB = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createIngredientTomatoValues();

        long ingredientRowId;
        ingredientRowId = dB.insert(RecipeContract.IngredientEntry.TABLE_NAME, null, testValues);

        assertTrue(ingredientRowId != -1);

        Cursor cursor = dB.query(RecipeContract.IngredientEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from ingredient query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error:Ingredient Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from ingredient query",
                cursor.moveToNext());

        cursor.close();
        dB.close();
        return ingredientRowId;
    }*/
}