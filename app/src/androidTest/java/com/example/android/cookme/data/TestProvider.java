package com.example.android.cookme.data;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.test.AndroidTestCase;
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
}
