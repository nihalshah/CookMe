package com.example.android.cookme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeProviderByJSON;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeFragment extends Fragment {

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

                EditText ingredientInput = (EditText) rootView.findViewById(R.id.ingredient_input);
                String ingredientQuery = ingredientInput.getText().toString();
                filterRecipesByIngredient(ingredientQuery);

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
}
