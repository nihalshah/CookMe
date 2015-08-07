package com.example.android.cookme;

/**
 * Created by armando on 8/7/15.
 */
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by eduardovaca on 27/07/15.
 */
public class IngredientAdapter extends CursorAdapter {


    public static class ViewHolder{
        public final TextView ingName;
        public final TextView ingAttributes;

        public ViewHolder(View view){
            ingName = (TextView)view.findViewById(R.id.list_item_ingredients_textView);
            ingAttributes = (TextView)view.findViewById(R.id.list_item_ingredients_attributesTextView);
        }
    }

    public IngredientAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_ingredients, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String name = "name";
        name = cursor.getString(DetailActivityFragment.COL_INGREDIENT_NAME);
        viewHolder.ingName.setText(name);
        String attributes = "Units: " + cursor.getString(DetailActivityFragment.COL_UNITS) +
                            "  Quatity: " + cursor.getDouble(DetailActivityFragment.COL_QUANTITY);
        viewHolder.ingAttributes.setText(attributes);


    }



}

