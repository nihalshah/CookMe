package com.example.android.cookme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.cookme.data.RecipeProvider;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeFragment extends Fragment {

    private RecipeProvider mListRecipes;
    private ArrayAdapter<String> mRecipeAdapter;

    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//
//        //mListRecipes = new RecipeProvider(getActivity());
//
//        //Fake Data to try the population of the ListView with an ArrayList of Strings
//        String [] fakeData = {"Pizza with Brocoli", "Tune Sandwich", "Paella",
//                                "Banana MilkShake", "Black Coffee", "Scramble Eggs",
//                                "French Bread", "Mexican Tacos"};
//
//        ArrayList<String> fakeListRecipes = new ArrayList<String>(Arrays.asList(fakeData));
//
//        mRecipeAdapter = new ArrayAdapter<>(
//                                    getActivity(),
//                                    R.layout.list_item_recipes,
//                                    R.id.list_item_recipes_textview,
//                                    fakeListRecipes);
//
//        ListView listRecipes = (ListView) rootView.findViewById(R.id.recipes_list);
//        listRecipes.setAdapter(mRecipeAdapter);

        RecipeProvider obj = new RecipeProvider(getActivity());
        ArrayList<String> allRecipes = obj.getRecipeNames();
        Log.v("First Recipe :", allRecipes.get(0));

        return rootView;
    }
}
