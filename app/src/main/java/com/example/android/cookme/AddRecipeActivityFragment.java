package com.example.android.cookme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.example.android.cookme.R;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddRecipeActivityFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    private Uri mURI;
    private static final int PICK_IMAGE = 0;
    private static final int PICK_IMAGE_FROM_GALLERY = 1;


    public AddRecipeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        mImageView = (ImageView) rootView.findViewById(R.id.add_picture_thumbnail);

        Button takePictureButton = (Button) rootView.findViewById(R.id.add_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1888);
            }


        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("LOG", "" + requestCode);
        if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
