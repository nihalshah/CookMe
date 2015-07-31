package com.example.android.cookme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cookme.R;
import com.example.android.cookme.data.Ingredient;

import java.io.File;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddRecipeActivityFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    private Uri mURI;
    private static final int PICK_IMAGE = 0;
    private static final int PICK_IMAGE_FROM_GALLERY = 1;
    private String mIngredientAdded;
    private ArrayList<Ingredient> mIngredientsList;

    private EditText mRecipeInput;
    private EditText mIngredientInput;
    private EditText mUnitInput;
    private EditText mQuantityInput;
    private EditText mInstructionsInput;
    private TextView mIngredientsAdded_tv;


    public AddRecipeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        mRecipeInput = (EditText)rootView.findViewById(R.id.add_recipe_name_input);
        mIngredientInput = (EditText) rootView.findViewById(R.id.add_ingredient_name_input);
        mUnitInput = (EditText) rootView.findViewById(R.id.add_units_input);
        mQuantityInput = (EditText) rootView.findViewById(R.id.add_quantity_input);
        mInstructionsInput = (EditText) rootView.findViewById(R.id.add_instruction_input);
        mImageView = (ImageView) rootView.findViewById(R.id.add_picture_imageview);

        mIngredientAdded = "INGREDIENTS ADDED : ";
        mIngredientsList = new ArrayList<>();

        mIngredientsAdded_tv = (TextView)rootView.findViewById(R.id.list_of_ingredients_added);
        mIngredientsAdded_tv.setText(mIngredientAdded);

        Button takePictureButton = (Button) rootView.findViewById(R.id.add_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1888);
            }


        });


        /*Add ingredient button event*/

        Button addIngredientButton = (Button) rootView.findViewById(R.id.add_ingredient_button);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ingredient_name = mIngredientInput.getText().toString();

                String units = mUnitInput.getText().toString();

                String quantityString = mQuantityInput.getText().toString();

                if(validAddingIngredient(ingredient_name, units, quantityString)){
                    double quantity = Double.parseDouble(quantityString);

                    mIngredientsList.add(new Ingredient(ingredient_name, quantity, units));

                    mIngredientAdded += ingredient_name + " " + quantity + " " + units + ", ";
                    mIngredientsAdded_tv.setText(mIngredientAdded);

                    mIngredientInput.setText("");
                    mUnitInput.setText("");
                    mQuantityInput.setText("");
                }
            }
        });

        /*Add recipe Button event*/
        //TODO: ADD VALIDATIONS!
        Button addButton = (Button) rootView.findViewById(R.id.add_new_recipe_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recipe_name = mRecipeInput.getText().toString();

                String instructions = mInstructionsInput.getText().toString();

                BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
                Bitmap picture = drawable.getBitmap();

                byte picture_in_bytes[] = Utility.getBytes(picture);

                if(validAddingRecipe(recipe_name, instructions, mIngredientsList)){
                    Utility.insertWholeRecipeInDb(getActivity(), recipe_name, instructions,
                            picture_in_bytes, mIngredientsList);

                    Context context = getActivity();
                    CharSequence text = "recipe Added!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    getActivity().finish();

                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("LOG", "" + requestCode);
        if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validAddingIngredient(String ing, String untis, String quantity){

        if(ing.length() > 0 && untis.length() > 0 && quantity.length() > 0)
            return true;
        else{

            String userHelp = "";

            if(ing.length() == 0)
                userHelp += "\n- Ingredient name";
            if(untis.length() == 0)
                userHelp += "\n- Ingredient units";
            if(quantity.length() == 0)
                userHelp += "\n- Ingredient quantity";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Missing information:\n " + userHelp)
                    .setCancelable(false)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    }

    private boolean validAddingRecipe(String name, String instructions, ArrayList<Ingredient> ingredients){

        if(name.length() > 0 && instructions.length() > 0 && ingredients.isEmpty() == false)
            return true;
        else{

            String userHelp = "";

            if(name.length() == 0)
                userHelp += "\n- Recipe name";
            if(instructions.length() == 0)
                userHelp += "\n- Instructions";
            if(ingredients.isEmpty())
                userHelp += "\n- Ingredients";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Missing information:\n " + userHelp)
                    .setCancelable(false)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
    }


}
