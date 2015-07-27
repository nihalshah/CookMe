package com.example.android.cookme;

import android.content.Intent;
import android.database.Cursor;
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

import com.example.android.cookme.data.RecipeContract;
import com.example.android.cookme.data.RecipeProviderByJSON;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeFragment extends Fragment {

    private static final String LOG_TAG = RecipeFragment.class.getSimpleName();

    private RecipeProviderByJSON mListRecipes;
    private RecipeAdapter mRecipeAdapter;

    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /*Just insert JSON in DB if DB is empty*/
        if(Utility.dataBaseIsEmpty(getActivity())){
            mListRecipes = new RecipeProviderByJSON(getActivity());
            Utility.insertJSONRecipesToDb(getActivity(), mListRecipes.getCollection_of_recipes());
        }

        String sortOrder = RecipeContract.RecipeEntry.COL_NAME + " ASC";

        Cursor cursor = getActivity().getContentResolver().query(
                RecipeContract.RecipeEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);

        mRecipeAdapter = new RecipeAdapter(getActivity(), cursor, 0);


        final ListView listRecipes = (ListView) rootView.findViewById(R.id.recipes_list);
        listRecipes.setAdapter(mRecipeAdapter);


        //Button Pressed event
        Button searchBtn = (Button) rootView.findViewById(R.id.search_ingredient_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText ingredientInput = (EditText) rootView.findViewById(R.id.ingredient_input);
                String ingredient = ingredientInput.getText().toString();
                mRecipeAdapter.swapCursor(queryByFilter(ingredient));
            }
        });

        return rootView;
    }


    public Cursor queryByFilter(String ingredientTyped){
        Cursor cursor;

        if(ingredientTyped.length() == 0){
            return cursor = queryWithNoParameters();
        }else{
            return cursor = queryWithParameters(ingredientTyped);
        }
    }

    public Cursor queryWithNoParameters(){

        String sortOrder = RecipeContract.RecipeEntry.COL_NAME + " ASC";

        Cursor cursor = getActivity().getContentResolver().query(
                RecipeContract.RecipeEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);

        return cursor;
    }

    public Cursor queryWithParameters(String ingredientName){

        String sortOrder = RecipeContract.RecipeEntry.COL_NAME + " ASC";

        Cursor cursor = getActivity().getContentResolver().query(
                RecipeContract.IngredientEntry.buildRecipesDirUri(ingredientName),
                null,
                null,
                null,
                sortOrder);

        return cursor;
    }


}
