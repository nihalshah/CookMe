package com.example.android.cookme;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeContract;
import com.example.android.cookme.data.RecipeProviderByJSON;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeFragment extends Fragment {

    private static final String LOG_TAG = RecipeFragment.class.getSimpleName();

    private RecipeProviderByJSON mListRecipes;
    private ArrayAdapter<String> mRecipeAdapter;
    private ArrayList<String> mRecipeNames;
    //List of Recipes which with the filter had been removed
    // (This will be improved with DB)
    private ArrayList<String> mDeletedRecipesNames;

    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        testInsertionOfRecipe();


        mListRecipes = new RecipeProviderByJSON(getActivity());

        mDeletedRecipesNames = new ArrayList<>();

        mRecipeNames = mListRecipes.getRecipeNames();

        mRecipeAdapter = new ArrayAdapter<>(
                                    getActivity(),
                                    R.layout.list_item_recipes,
                                    R.id.list_item_recipes_textview,
                                    mRecipeNames);

        ListView listRecipes = (ListView) rootView.findViewById(R.id.recipes_list);
        listRecipes.setAdapter(mRecipeAdapter);

        //Item clicked event
        listRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String recipeName = mRecipeAdapter.getItem(i);
                Recipe actualRecipe = mListRecipes.getRecipeByName(recipeName);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra("Recipe", actualRecipe);
                startActivity(detailIntent);
            }
        });

        //Button Pressed event
        Button searchBtn = (Button) rootView.findViewById(R.id.search_ingredient_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*EditText ingredientInput = (EditText) rootView.findViewById(R.id.ingredient_input);
                String ingredientQuery = ingredientInput.getText().toString();
                filterRecipesByIngredient(ingredientQuery);*/

                testReadWholeRecipes();

            }
        });

        return rootView;
    }

    public void filterRecipesByIngredient(String ingredientTyped){

        mRecipeNames.addAll(mDeletedRecipesNames);
        mDeletedRecipesNames.clear();
        if(ingredientTyped.length() != 0){
            Iterator it = mRecipeNames.iterator();
            while(it.hasNext()){
                String actualRep = (String) it.next();

                if(!mListRecipes.getRecipeByName(actualRep).hasIngredient(ingredientTyped)){
                    mDeletedRecipesNames.add(actualRep);
                    it.remove();
                }
            }
        }
        mRecipeAdapter.notifyDataSetChanged();
    }

    public void testInsertionOfRecipe(){

        ContentValues recipeValues = Utility.createRecipeValues("Sandwich", "Go to Subway");
        ContentValues ingredientValues = Utility.createIngredientValues("Bread");


        //Insert in tables Recipe and Ingredient
        Uri recipeInserted = getActivity().getContentResolver().
                insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);
        Uri ingredientInserted = getActivity().getContentResolver().
                insert(RecipeContract.IngredientEntry.CONTENT_URI, ingredientValues);

        //Get the id of the rows inserted
        long recipe_id = ContentUris.parseId(recipeInserted);
        long ingredient_id = ContentUris.parseId(ingredientInserted);

        //Insert in the table of relationship
        ContentValues relationValues = Utility.createRelationshipValues(recipe_id, ingredient_id, "L", 1);
        getActivity().getContentResolver().
                insert(RecipeContract.RecipeIngredientRelationship.CONTENT_URI, relationValues);

        Log.v(LOG_TAG, "Insertion of entire recipe completed!");
    }

    public void testReadWholeRecipes(){

        Cursor cursor = getActivity().getContentResolver().query(
                RecipeContract.IngredientEntry.buildRecipesDirUri("Bread"),
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            String rec = "";
            int index = cursor.getColumnIndex(RecipeContract.RecipeEntry.COL_NAME);
            rec += cursor.getString(index) + ", ";
            index = cursor.getColumnIndex(RecipeContract.RecipeEntry.COL_INSTRUCTIONS);
            rec += cursor.getString(index) + ", ";
            index = cursor.getColumnIndex(RecipeContract.IngredientEntry.COL_NAME);
            rec += cursor.getString(index) + ", ";
            index = cursor.getColumnIndex(RecipeContract.RecipeIngredientRelationship.COL_QUANTITY);
            rec += cursor.getInt(index) + ", ";
            index = cursor.getColumnIndex(RecipeContract.RecipeIngredientRelationship.COL_UNITS);
            rec += cursor.getString(index) + ", ";

            Log.v(LOG_TAG, "MY RECIPE: " + rec);


        }


        cursor.close();
    }
}
