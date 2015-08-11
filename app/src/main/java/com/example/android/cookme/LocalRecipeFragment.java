package com.example.android.cookme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.cookme.data.RecipeContract;
import com.example.android.cookme.data.RecipeProviderByJSON;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class LocalRecipeFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>{


    private static final int RECIPE_LOADER = 0;
    private RecipeProviderByJSON mListRecipes;
    private RecipeAdapter mRecipeAdapter;
    private String mIngredientTyped;
    private String mIngredientsSelected;

    private ImageView mAvatar;
    private ListView mRecipesList;
    private EditText mIngredientInput;
    private TextView mIngredientsQuerying;
    private Button mClearQuery;
    private TextView TitleTransition;
    public boolean InSearchMode = false;

    //Projection for querying
    private static final String[] RECIPE_COLUMNS = {
                RecipeContract.RecipeEntry.TABLE_NAME + "." + RecipeContract.RecipeEntry._ID,
                RecipeContract.RecipeEntry.COL_NAME,
                RecipeContract.RecipeEntry.COL_INSTRUCTIONS,
                RecipeContract.RecipeEntry.COL_PHOTO,
                RecipeContract.RecipeEntry.COL_PATH_PHOTO
    };

    static final int COL_RECIPE_ID = 0;
    static final int COL_RECIPE_NAME = 1;
    static final int COL_INSTRUCTIONS = 2;
    static final int COL_PHOTO = 3;
    static final int COL_PATH_PHOTO = 4;

    public LocalRecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_local_recipe, container, false);

        mIngredientTyped = "";
        mIngredientsSelected = "";

        mIngredientInput = (EditText) rootView.findViewById(R.id.ingredient_input);
        mIngredientsQuerying = (TextView) rootView.findViewById(R.id.ingredients_in_query_textview);
        mClearQuery = (Button) rootView.findViewById(R.id.clear_list_ingredients_button);
        mRecipesList = (ListView) rootView.findViewById(R.id.recipes_list);


        // The CursorAdapter will take data from our cursor and populate the ListView.
        mRecipeAdapter = new RecipeAdapter(getActivity(), null, 0);

        mRecipesList.setAdapter(mRecipeAdapter);

        if(mRecipeAdapter.isEmpty()){
            displayAlertOfNoResults();
        }

        mRecipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class).
                            setData(RecipeContract.RecipeIngredientRelationship.
                                    buildRelationshipUriWithRecipeId(cursor.getLong(COL_RECIPE_ID)));
                    //Apply animation if lollipop is present
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Call some material design APIs here
                        String transitionName = getString(R.string.transition_album_cover);
                        mAvatar = (ImageView) view.findViewById(R.id.recipe_picture_imageview);
                        ActivityOptionsCompat options =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                        mAvatar,   // The view which starts the transition
                                        transitionName    // The transitionName of the view we’re transitioning to
                                );
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                    } else {
                        // Implement this feature without material design
                        startActivity(intent);
                    }


                }
            }
        });

        mIngredientInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mIngredientTyped = mIngredientInput.getText().toString();
                    mIngredientInput.setText("");

                    if (mIngredientsSelected.length() == 0)
                        mIngredientsSelected += mIngredientTyped;
                    else
                        mIngredientsSelected += "-" + mIngredientTyped; //DO NOT CHANGE THE '-' ... necessary for later split

                    mIngredientsQuerying.setText(mIngredientsSelected);
                    restartLoader();
                    InSearchMode = true;

                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        mClearQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIngredientsSelected = "";
                mIngredientsQuerying.setText(mIngredientsSelected);

                restartLoader();
            }
        });

        return rootView;
    }

    public void restartLoader(){
        getLoaderManager().restartLoader(RECIPE_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(RECIPE_LOADER, null,  this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = RecipeContract.RecipeEntry.COL_NAME + " ASC";

        if(mIngredientsSelected.length() == 0){
            return  new CursorLoader(getActivity(),
                    RecipeContract.RecipeEntry.CONTENT_URI,
                    RECIPE_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }else{
            return new CursorLoader(getActivity(),
                    RecipeContract.IngredientEntry.buildRecipesDirUri(mIngredientsSelected),
                    RECIPE_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mRecipeAdapter.swapCursor(cursor);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && InSearchMode) {
                        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        mIngredientsSelected = "";
                        mIngredientsQuerying.setText(mIngredientsSelected);
                        restartLoader();
                        InSearchMode = false;
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipeAdapter.swapCursor(null);
    }


    private void displayAlertOfNoResults(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("No recipes found with that ingredients")
                .setCancelable(true)
                .setPositiveButton("Ok", null);
        AlertDialog alert = builder.create();
        alert.show();
    }


}
