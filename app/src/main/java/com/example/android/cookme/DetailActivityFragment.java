package com.example.android.cookme;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeContract;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    private static long mActualRecipeId;

    private static final String[] RECIPE_COLUMNS = {
            RecipeContract.RecipeEntry.TABLE_NAME + "." + RecipeContract.RecipeEntry._ID,
            RecipeContract.RecipeEntry.COL_NAME,
            RecipeContract.RecipeEntry.COL_INSTRUCTIONS,
            RecipeContract.RecipeEntry.COL_PHOTO,
            RecipeContract.IngredientEntry.TABLE_NAME + "." + RecipeContract.IngredientEntry._ID,
            RecipeContract.IngredientEntry.COL_NAME,
            RecipeContract.RecipeIngredientRelationship.COL_UNITS,
            RecipeContract.RecipeIngredientRelationship.COL_QUANTITY
    };

    private static final int COL_RECIPE_ID = 0;
    private static final int COL_RECIPE_NAME = 1;
    private static final int COL_INSTRUCTIONS = 2;
    private static final int COL_PHOTO = 3;
    private static final int COL_INGREDIENT_ID = 4;
    private static final int COL_INGREDIENT_NAME = 5;
    private static final int COL_UNITS = 6;
    private static final int COL_QUANTITY = 7;

    private TextView mNameView;
    private ImageView mPhotoView;
    private ListView mIngredientListView;
    private TextView mInstructionsView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mNameView = (TextView)rootView.findViewById(R.id.recipeName_textView);
        mPhotoView = (ImageView)rootView.findViewById(R.id.recipe_picture_imageview);
        mIngredientListView = (ListView)rootView.findViewById(R.id.ingredients_list);
        mInstructionsView = (TextView)rootView.findViewById(R.id.instructions_textView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getActivity().getIntent();
        if(intent == null)
            return null;

        return new CursorLoader(getActivity(),
                intent.getData(),
                RECIPE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(!data.moveToFirst())
            return;

        mActualRecipeId = data.getLong(COL_RECIPE_ID);

        mNameView.setText(data.getString(COL_RECIPE_NAME));
        mInstructionsView.setText(data.getString(COL_INSTRUCTIONS));

        //Get image from DB
        byte [] array_picture = data.getBlob(COL_PHOTO);
        if(array_picture != null){
            //TODO:Fixing the size of picture
            Bitmap picture = Utility.getImage(array_picture);
            mPhotoView.setImageBitmap(picture);
        }

        ArrayList<String> ingredients = new ArrayList<>();

        String actualIngredient = data.getString(COL_INGREDIENT_NAME);
        actualIngredient += " " + data.getDouble(COL_QUANTITY) + " " + data.getString(COL_UNITS);
        ingredients.add(actualIngredient);

        while (data.moveToNext()){
            actualIngredient = data.getString(COL_INGREDIENT_NAME);
            actualIngredient += " " + data.getDouble(COL_QUANTITY) + " " + data.getString(COL_UNITS);
            ingredients.add(actualIngredient);
        }

        ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_ingredients,     //id of the item layout
                R.id.list_item_ingredients_textView,//id of the textView to populate with
                ingredients
        );

        mIngredientListView.setAdapter(ingredientAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static long getRecipeId(){

        return mActualRecipeId;
    }
}
