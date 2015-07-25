package com.example.android.cookme;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.cookme.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddRecipeActivityFragment extends Fragment {

    public AddRecipeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);


        /*Button event*/
        Button addButton = (Button) rootView.findViewById(R.id.add_new_recipe_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText recipeInput = (EditText) rootView.findViewById(R.id.add_recipe_name_input);
                String recipe_name = recipeInput.getText().toString();

                EditText ingredientInput = (EditText) rootView.findViewById(R.id.add_ingredient_name_input);
                String ingredient_name = ingredientInput.getText().toString();

                EditText unitsInput = (EditText) rootView.findViewById(R.id.add_units_input);
                String units = unitsInput.getText().toString();

                EditText quantityInput = (EditText) rootView.findViewById(R.id.add_quantity_input);
                double quantity = Double.parseDouble(quantityInput.getText().toString());

                EditText instructionsInput = (EditText) rootView.findViewById(R.id.add_instruction_input);
                String instructions = instructionsInput.getText().toString();

                Utility.insertWholeRecipeInDb(getActivity(), recipe_name, instructions,
                                                ingredient_name, units, quantity);

                //Message that shows the user that his recipe was added
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity()).
                        setMessage(recipe_name + " recipe added!");

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
        return rootView;
    }
}
