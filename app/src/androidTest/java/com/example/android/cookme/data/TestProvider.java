package com.example.android.cookme.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.android.cookme.data.RecipeContract.RecipeEntry;
import com.example.android.cookme.data.RecipeContract.IngredientEntry;
import com.example.android.cookme.data.RecipeContract.RecipeIngredientRelationship;

/**
 * Created by eduardovaca on 21/07/15.
 */
public class TestProvider extends AndroidTestCase {

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



        /*long testDate = 1419120000L; // December 21st, 2014
        // content://com.example.android.sunshine.app/weather/94074/20140612
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocationWithDate(testLocation, testDate));
        // vnd.android.cursor.item/com.example.android.sunshine.app/weather/1419120000
        assertEquals("Error: the WeatherEntry CONTENT_URI with location and date should return WeatherEntry.CONTENT_ITEM_TYPE",
                WeatherEntry.CONTENT_ITEM_TYPE, type);

        // content://com.example.android.sunshine.app/location/
        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/location
        assertEquals("Error: the LocationEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE",
                LocationEntry.CONTENT_TYPE, type);*/
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
        assertNull(row, listRecipesCursor); //I USE THIS TO PRINT IN CONSOLE
//        // Make sure we get the correct cursor out of the database
//        TestUtilities.validateCursor("testBasicWeatherQuery", weatherCursor, weatherValues);
    }
}
