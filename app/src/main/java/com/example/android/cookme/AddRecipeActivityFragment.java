package com.example.android.cookme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cookme.data.Ingredient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddRecipeActivityFragment extends Fragment {

    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView mImageView;
    private Uri mURI;
    private String mIngredientAdded;
    private String mInstructionsAdded;
    private ArrayList<Ingredient> mIngredientsList;

    private EditText mRecipeInput;
    private EditText mIngredientInput;
    private EditText mUnitInput;
    private EditText mQuantityInput;
    private EditText mInstructionsInput;
    private TextView mIngredientsAdded_tv;
    private TextView mInstructionsAdded_tv;
    private int instruction_count = 1;
    String mCurrentPhotoPath;

    //Variables for maintaining state of fragment
    private static final String STATE_INGREDIENTS_LIST = "ingredientList";
    private static final String STATE_INGREDIENTS_TEXTVIEW = "ingredientsTextView";
    private static final String STATE_INSTRUCTIONS_LIST = "instructionsList";
    private static final String STATE_PHOTO = "photoState";
    private static final String STATE_PHOTO_PATH = "photoPathState";


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

        mIngredientAdded = "Ingredients Added : ";
        mIngredientsList = new ArrayList<>();
        mInstructionsAdded = "Instructions Added : ";

        mIngredientsAdded_tv = (TextView)rootView.findViewById(R.id.list_of_ingredients_added);
        mIngredientsAdded_tv.setText(mIngredientAdded);

        mInstructionsAdded_tv = (TextView)rootView.findViewById(R.id.list_of_instructions_added);
        mInstructionsAdded_tv.setText(mInstructionsAdded);


        Button takePictureButton = (Button) rootView.findViewById(R.id.add_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
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

        /*Add instructions button */
        Button addInstructionButton = (Button) rootView.findViewById(R.id.add_instruction_button);
        addInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String instruction = mInstructionsInput.getText().toString();

                if(validAddingInstruction(instruction)){

                    mInstructionsAdded += "\n" + (instruction_count++) + ". " + instruction + ".";
                    mInstructionsAdded_tv.setText(mInstructionsAdded);

                    mInstructionsInput.setText("");
                }
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + mCurrentPhotoPath), "image/*");
                startActivity(intent);


            }
        });

        /*Add recipe Button event*/
        Button addButton = (Button) rootView.findViewById(R.id.add_new_recipe_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recipe_name = mRecipeInput.getText().toString();

                String instructions = mInstructionsInput.getText().toString();

                BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
                Bitmap picture = drawable.getBitmap();

                byte picture_in_bytes[] = Utility.getBytes(picture);


                if(validAddingRecipe(recipe_name, mInstructionsAdded, mIngredientsList)){
                    Utility.insertWholeRecipeInDb(getActivity(), recipe_name, mInstructionsAdded,
                            mCurrentPhotoPath, picture_in_bytes, mIngredientsList);
                     Utility.insertRecipeIntoRemoteServer(recipe_name, mInstructionsAdded, picture_in_bytes, mIngredientsList);
                 }

                    Context context = getActivity();
                    CharSequence text = recipe_name + " recipe Added!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    getActivity().finish();

                }
            });

        if(savedInstanceState != null){
            //if(savedInstanceState.containsKey(STATE_INGREDIENTS_LIST))
              //  mIngredientsList = savedInstanceState.getParcelableArrayList(STATE_INGREDIENTS_LIST);
            if(savedInstanceState.containsKey(STATE_INGREDIENTS_TEXTVIEW)){
                mIngredientAdded = savedInstanceState.getString(STATE_INGREDIENTS_TEXTVIEW);
                mIngredientsAdded_tv.setText(mIngredientAdded);
            }
            if(savedInstanceState.containsKey(STATE_INSTRUCTIONS_LIST)){
                mInstructionsAdded = savedInstanceState.getString(STATE_INSTRUCTIONS_LIST);
                mInstructionsAdded_tv.setText(mInstructionsAdded);
            }
            if(savedInstanceState.containsKey(STATE_PHOTO)){
                mImageView.setImageBitmap(savedInstanceState.<Bitmap>getParcelable(STATE_PHOTO));
            }
            if(savedInstanceState.containsKey(STATE_PHOTO_PATH))
                mCurrentPhotoPath = savedInstanceState.getString(STATE_PHOTO_PATH);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

       // outState.putParcelableArrayList(STATE_INGREDIENTS_LIST, mIngredientsList);
        outState.putString(STATE_INGREDIENTS_TEXTVIEW, mIngredientAdded);
        outState.putString(STATE_INSTRUCTIONS_LIST, mInstructionsAdded);
        if(mCurrentPhotoPath != null){
            BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
            Bitmap picture = drawable.getBitmap();
            outState.putParcelable(STATE_PHOTO, picture);
            outState.putString(STATE_PHOTO_PATH, mCurrentPhotoPath);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("LOG", "" + requestCode);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            setPic();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {


            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setPic() {

        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private boolean validAddingIngredient(String ing, String untis, String quantity){

        if(ing.length() > 0 && untis.length() > 0 && quantity.length() > 0) {
            return true;
        }
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
                    .setCancelable(true)
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
            if(mInstructionsAdded.length() == 0)
                userHelp += "\n- Instructions";
            if(ingredients.isEmpty())
                userHelp += "\n- Ingredients";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Missing information:\n " + userHelp)
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();

            return false;
        }
    }

    private boolean validAddingInstruction(String instruction){

        if(instruction.length() > 0)
            return true;
        else{

            String userHelp = "";

            if(instruction.length() == 0)
                userHelp += "\n- Instruction";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Missing information:\n " + userHelp)
                    .setCancelable(true)
                    .setPositiveButton("Ok", null);
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    }


}
