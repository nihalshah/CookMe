package com.example.android.cookme.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by eduardovaca on 20/07/15.
 */
public class TestUtilities extends AndroidTestCase{

    static ContentValues createRecipeGuacamoleValues(){
        ContentValues testValues = new ContentValues();
        String inst = "1.- Peel and mash avocados in a medium serving bowl. Stir in onion, garlic, tomato, lime juice, salt and pepper. Season with remaining lime juice and salt and pepper to taste. Chill for half an hour to blend flavors.";
        testValues.put(RecipeContract.RecipeEntry.COL_NAME, "Guacamole");
        testValues.put(RecipeContract.RecipeEntry.COL_INSTRUCTIONS, inst);
        return testValues;
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
}
