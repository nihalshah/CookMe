package com.example.android.cookme;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeProvider;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private RecipeProvider mRecipeProvider;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent receivedIntent = getActivity().getIntent();
        if(receivedIntent != null && receivedIntent.hasExtra("Recipe")){
            //Get the recipe from the intent and fill all the elements of the layout with it
            Recipe recipe = (Recipe) receivedIntent.getExtras().getSerializable("Recipe");

            TextView recipeName = (TextView) rootView.findViewById(R.id.recipeName_textview);
            recipeName.setText(recipe.getName());

            ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(
                                                        getActivity(),
                                                        R.layout.list_item_ingredients,
                                                        R.id.list_item_ingredients_textview,
                                                        recipe.getStringsOfIngredients());
            ListView ingredientsList = (ListView)rootView.findViewById(R.id.ingredients_list);
            ingredientsList.setAdapter(ingredientsAdapter);

        }

        return rootView;
    }
}
