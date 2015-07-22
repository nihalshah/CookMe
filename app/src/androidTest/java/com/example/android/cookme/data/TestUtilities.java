package com.example.android.cookme.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.android.cookme.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by eduardovaca on 20/07/15.
 */
public class TestUtilities extends AndroidTestCase{

    static ContentValues createRecipeGuacamoleValues(){
        ContentValues testValues = new ContentValues();
        String inst = "1.- Peel and mash avocados.";
        testValues.put(RecipeContract.RecipeEntry.COL_NAME, "Guacamole");
        testValues.put(RecipeContract.RecipeEntry.COL_INSTRUCTIONS, inst);
        return testValues;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createIngredientTomatoValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(RecipeContract.IngredientEntry.COL_NAME, "Tomato");
        return testValues;
    }

    static ContentValues createRelationshipValues(long recipeId, long ingredientId){
        ContentValues testValues = new ContentValues();
        testValues.put(RecipeContract.RecipeIngredientRelationship.COL_RECIPE_KEY, recipeId);
        testValues.put(RecipeContract.RecipeIngredientRelationship.COL_INGREDIENT_KEY, ingredientId);
        testValues.put(RecipeContract.RecipeIngredientRelationship.COL_UNITS, "CUP");
        testValues.put(RecipeContract.RecipeIngredientRelationship.COL_QUANTITY, 1);
        return testValues;
    }


    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
