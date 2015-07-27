package com.example.android.cookme;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.cookme.data.RecipeContract;

/**
 * Created by eduardovaca on 27/07/15.
 */
public class RecipeAdapter extends CursorAdapter {

    public RecipeAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipes, viewGroup, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tv = (TextView)view.findViewById(R.id.list_item_recipes_textview);
        tv.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COL_NAME)));
    }
}
