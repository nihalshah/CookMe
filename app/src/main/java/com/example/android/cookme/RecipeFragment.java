package com.example.android.cookme;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.cookme.data.RecipeProvider;


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

        mListRecipes = new RecipeProvider(getActivity());
        

        return rootView;
    }
}
