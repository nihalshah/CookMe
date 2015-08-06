package com.example.android.cookme;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by eduardovaca on 27/07/15.
 */
public class RecipeAdapter extends CursorAdapter {


    public static class ViewHolder{
        public final TextView recipeName;
        public final ImageView recipePicture;

        public ViewHolder(View view){
            recipeName = (TextView)view.findViewById(R.id.list_item_recipes_textview);
            recipePicture = (ImageView)view.findViewById(R.id.recipe_picture_imageview);
        }
    }

    public RecipeAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipes, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.recipeName.setText(cursor.getString(LocalRecipeFragment.COL_RECIPE_NAME));

        byte [] image_array = cursor.getBlob(LocalRecipeFragment.COL_PHOTO);
        if(image_array != null){
            Bitmap image = Utility.getImage(image_array);
           viewHolder.recipePicture.setImageBitmap(image);
        }
    }
}
