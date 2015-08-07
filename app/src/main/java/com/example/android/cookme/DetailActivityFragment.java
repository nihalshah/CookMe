package com.example.android.cookme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    private static long mActualRecipeId;
    private String mShareString;
    private ShareActionProvider mShareActionProvider;
    private static final String HASHTAG = "#EasyCook";


    private static final String[] RECIPE_COLUMNS = {
            RecipeContract.RecipeEntry.TABLE_NAME + "." + RecipeContract.RecipeEntry._ID,
            RecipeContract.RecipeEntry.COL_NAME,
            RecipeContract.RecipeEntry.COL_INSTRUCTIONS,
            RecipeContract.RecipeEntry.COL_PHOTO,
            RecipeContract.RecipeEntry.COL_PATH_PHOTO,
            RecipeContract.IngredientEntry.TABLE_NAME + "." + RecipeContract.IngredientEntry._ID,
            RecipeContract.IngredientEntry.COL_NAME,
            RecipeContract.RecipeIngredientRelationship.COL_UNITS,
            RecipeContract.RecipeIngredientRelationship.COL_QUANTITY
    };

    private static final int COL_RECIPE_ID = 0;
    private static final int COL_RECIPE_NAME = 1;
    private static final int COL_INSTRUCTIONS = 2;
    private static final int COL_PHOTO = 3;
    private static final int COL_PATH_PHOTO = 4;
    private static final int COL_INGREDIENT_ID = 5;
    static final int COL_INGREDIENT_NAME = 6;
    static final int COL_UNITS = 7;
    static final int COL_QUANTITY = 8;

    private TextView mNameView;
    private ImageView mPhotoView;
    private ListView mIngredientListView;
    private TextView mInstructionsView;
    private CardView mCardViewTitle;

    private IngredientAdapter mIngedientAdapter;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mNameView = (TextView)rootView.findViewById(R.id.recipeName_textView);
        mPhotoView = (ImageView)rootView.findViewById(R.id.recipe_picture_imageview);
        mIngredientListView = (ListView)rootView.findViewById(R.id.ingredients_list);
        mInstructionsView = (TextView)rootView.findViewById(R.id.instructions_textView);

        mShareString = "Check out this recipe!";

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.detail_fragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createRecipeShareIntent());
        }else{
            Log.v(LOG_TAG, "The Share Action Provider is null");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_delete_recipe:{
                deleteConfirmation();
                return true;
            }
            case R.id.action_share:{

            }
        }
        return false;
    }


    public Intent createRecipeShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareString);

        return shareIntent;
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

        String name = data.getString(COL_RECIPE_NAME);
        mNameView.setText(name);
        mShareString += "\n\n" + name.toUpperCase() + "\n";

        String instructions = data.getString(COL_INSTRUCTIONS);
        mInstructionsView.setText(instructions);

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
        mShareString += "\n" + actualIngredient;

        while (data.moveToNext()){
            actualIngredient = data.getString(COL_INGREDIENT_NAME);
            actualIngredient += " " + data.getDouble(COL_QUANTITY) + " " + data.getString(COL_UNITS);
            ingredients.add(actualIngredient);
            mShareString += "\n" + actualIngredient;
        }
        /*
        ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_ingredients,     //id of the item layout
                R.id.list_item_ingredients_textView,//id of the textView to populate with
                ingredients
        );
        */

        mIngedientAdapter = new IngredientAdapter(getActivity(), null, 0);

        mIngredientListView.setAdapter(mIngedientAdapter);
        //TO DO CHECK IF FALSE;
        setListViewHeightBasedOnItems(mIngredientListView);

        mShareString += "\n\nSteps: \n" + instructions + "\n\n" + HASHTAG;
        mShareString += "\n" + "https://www.facebook.com";

        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createRecipeShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public void deleteRecipe(long id){

        Utility.deleteRecipeFromDb(getActivity(), id);
        CharSequence text = "Recipe Deleted!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getActivity(), text, duration);
        toast.show();

        getActivity().finish();
    }

    public void deleteConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you wanna delete this recipe?")
                .setCancelable(false)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteRecipe(mActualRecipeId);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}
