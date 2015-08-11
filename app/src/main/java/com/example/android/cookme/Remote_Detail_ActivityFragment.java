package com.example.android.cookme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.cookme.data.Ingredient;
import com.example.android.cookme.data.Recipe;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A placeholder fragment containing a simple view.
 */
public class Remote_Detail_ActivityFragment extends Fragment {

    private TextView mRecipeName;
    private TextView mInstructions;
    private ImageView mRecipePhoto;
    private ListView mIngredientList;
    private ArrayList<Ingredient> ingredients;


    public Remote_Detail_ActivityFragment() {
    }
/*

    The Details of a Remote Recipe once a User has clicked on the recipe from the list.

 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_remote__detail_, container, false);

        mRecipeName = (TextView) rootView.findViewById(R.id.detail_recipe_name);
        mInstructions = (TextView) rootView.findViewById(R.id.remote_instructions);
        mRecipePhoto = (ImageView) rootView.findViewById(R.id.remote_recipe_picture_imageview);

        mIngredientList = (ListView) rootView.findViewById(R.id.ingredients_list_remote);


        Intent intent = getActivity().getIntent();

        if(intent!= null && intent.hasExtra(Intent.EXTRA_TEXT)){

            Recipe r = (Recipe) intent.getSerializableExtra(Intent.EXTRA_TEXT);

            mRecipeName.setText(r.getName());
            mInstructions.setText(r.getInstructions());
            LinkedList<Ingredient> listIngredients = r.getIngredients();

            RemoteIngredientAdapter remoteIngredientAdapter = new RemoteIngredientAdapter(getActivity(), R.id.ingredients_list_remote, listIngredients);

            ingredients = new ArrayList<>(listIngredients);

            mIngredientList.setAdapter(remoteIngredientAdapter);
            setListViewHeightBasedOnItems(mIngredientList);

            String imageString = r.getImage();
            byte [] decodedString = Base64.decode(imageString, Base64.URL_SAFE);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mRecipePhoto.setImageBitmap(decodedByte);
        }

        return rootView;
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
