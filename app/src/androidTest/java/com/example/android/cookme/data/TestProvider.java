package com.example.android.cookme.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.cookme.data.RecipeContract.RecipeEntry;
import com.example.android.cookme.data.RecipeContract.IngredientEntry;
import com.example.android.cookme.data.RecipeContract.RecipeIngredientRelationship;

/**
 * Created by eduardovaca on 21/07/15.
 */
public class TestProvider extends AndroidTestCase {



    public void deleteAllRecordsFromDB() {
        RecipeDbHelper dbHelper = new RecipeDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(RecipeEntry.TABLE_NAME, null, null);
        db.delete(IngredientEntry.TABLE_NAME, null, null);
        db.delete(RecipeIngredientRelationship.TABLE_NAME, null, null);
        db.close();
    }

    /*
        Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                RecipeProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: RecipeProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + RecipeContract.CONTENT_AUTHORITY,
                    providerInfo.authority, RecipeContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: RecipeProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        // content://com.example.android.cookme/recipe/
        String type = mContext.getContentResolver().getType(RecipeEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the RecipeEntry CONTENT_URI should return RecipeEntry.CONTENT_TYPE",
               RecipeEntry.CONTENT_TYPE, type);

        Long testRecipeId = 10L;
        // content://com.example.android.sunshine.app/weather/94074
        type = mContext.getContentResolver().getType(
                RecipeEntry.buildIngredientsDirUri(Long.toString(testRecipeId)));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the WeatherEntry CONTENT_URI with location should return WeatherEntry.CONTENT_TYPE",
                RecipeEntry.CONTENT_TYPE, type);

        Long testIngredientId = 11L;
        // content://com.example.android.sunshine.app/weather/94074
        type = mContext.getContentResolver().getType(
                IngredientEntry.buildRecipesDirUri(Long.toString(testIngredientId)));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the IngredientEntry CONTENT_URI with location should return IngredientEntry.CONTENT_TYPE",
                IngredientEntry.CONTENT_TYPE, type);


        type = mContext.getContentResolver().getType(
                RecipeIngredientRelationship.buildRecipeItemUri(Long.toString(testIngredientId), Long.toString(testRecipeId)));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals("Error: the RelationEntry ITEM should return RELATION.CONTENT_ITEM_TYPE",
                RecipeIngredientRelationship.CONTENT_ITEM_TYPE, type);

    }

    public void testBasicIngredientQueries() {
        // insert our test records into the database
        RecipeDbHelper dbHelper = new RecipeDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createIngredientTomatoValues();
        long ingredientRowId = db.insert(IngredientEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert IngredientEntry into the Database", ingredientRowId != -1);

        // Test the basic content provider query
        Cursor ingredientCursor = mContext.getContentResolver().query(
                IngredientEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicIngredientQueries, ingredient query", ingredientCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: Ingredient Query did not properly set NotificationUri",
                    ingredientCursor.getNotificationUri(), IngredientEntry.CONTENT_URI);
        }
    }


    public void testGetRecipesByIngredient() {
        // insert our test records into the database
        RecipeDbHelper dbHelper = new RecipeDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues recipeValues = TestUtilities.createRecipeGuacamoleValues();
        long recipeRowId = db.insert(RecipeEntry.TABLE_NAME, null, recipeValues);
        assertTrue("Unable to Insert RecipeEntry into the Database", recipeRowId != -1);

        // Fantastic.  Now that we have a recipe, add ingredient!
        ContentValues ingredientValues = TestUtilities.createIngredientTomatoValues();

        long ingredientRowId = db.insert(IngredientEntry.TABLE_NAME, null, ingredientValues);
        assertTrue("Unable to Insert IngredientEntry into the Database", ingredientRowId != -1);

        //Now insert in relation
        ContentValues relationValues = TestUtilities.createRelationshipValues(recipeRowId, ingredientRowId);
        long relationId = db.insert(RecipeIngredientRelationship.TABLE_NAME, null, relationValues);
        assertTrue("Unable to Insert RelationEntry into the Database", relationId != -1);

        db.close();
//
        // Test the content provider query
        Cursor listRecipesCursor = mContext.getContentResolver().query(
                IngredientEntry.buildRecipesDirUri("Tomato"),
                null,
                null,
                null,
                null
        );

        assertTrue("Error, CUrsos is null", listRecipesCursor.moveToFirst());
        String [] names = listRecipesCursor.getColumnNames();
        StringBuilder builder = new StringBuilder();
        for(String s : names) {
            builder.append(s);
        }
        // recipe_idingredient_idunitsquantity_idnameinstructions_idname
        String row = "";
        row += "Rec_id: " + Integer.toString(listRecipesCursor.getInt(0));
        row += " Ing_id: " + Integer.toString(listRecipesCursor.getInt(1));
        row += " Rec: " + listRecipesCursor.getString(5);
        row += " Ing: " + listRecipesCursor.getString(8);
       // assertNull(row, listRecipesCursor); //I USE THIS TO PRINT IN CONSOLE
//        // Make sure we get the correct cursor out of the database
//        TestUtilities.validateCursor("testBasicWeatherQuery", weatherCursor, weatherValues);
    }

    public void testInsertReadProvider() {
        ContentValues recipeValues = TestUtilities.createRecipeGuacamoleValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(RecipeEntry.CONTENT_URI, true, tco);
        Uri recipeUri = mContext.getContentResolver().insert(RecipeEntry.CONTENT_URI, recipeValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long recipeRowId = ContentUris.parseId(recipeUri);

        // Verify we got a row back.
        assertTrue(recipeRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                RecipeEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating RecipeEntry.",
                cursor, recipeValues);

        // Fantastic.  Now that we have a recipe, add ingredient!
        ContentValues ingredientValues = TestUtilities.createIngredientTomatoValues();
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(IngredientEntry.CONTENT_URI, true, tco);

        Uri ingredientInsertUri = mContext.getContentResolver()
                .insert(IngredientEntry.CONTENT_URI, ingredientValues);
        assertTrue(ingredientInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long ingredientRowId = ContentUris.parseId(ingredientInsertUri);

        // A cursor is your primary interface to the query results.
        Cursor ingredientCursor = mContext.getContentResolver().query(
                IngredientEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating IngredientEntry insert.",
                ingredientCursor, ingredientValues);

        //NOW ADD THE RELATION
        ContentValues relationValues = TestUtilities.createRelationshipValues(recipeRowId, ingredientRowId);

        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(RecipeIngredientRelationship.CONTENT_URI, true, tco);

        Uri relationInsertUri = mContext.getContentResolver().
                insert(RecipeIngredientRelationship.CONTENT_URI, relationValues);

        assertTrue(relationInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);



        // make sure that the join worked and we actually get all the values back
        relationValues.putAll(recipeValues);
        relationValues.putAll(ingredientValues);


        // Get the joined Recipe and Ingrdient data
        Cursor relationCursor = mContext.getContentResolver().query(
                IngredientEntry.buildRecipesDirUri("Tomato"),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );


        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Recipe and Ingredient Data.",
               relationCursor, relationValues);

    }
}
