package com.example.android.cookme;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
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
        public final TextView numberIngredients;

        public ViewHolder(View view){
            recipeName = (TextView)view.findViewById(R.id.list_item_recipes_textview);
            recipePicture = (ImageView)view.findViewById(R.id.recipe_picture_imageview);
            numberIngredients = (TextView)view.findViewById(R.id.number_ingredients_textview);
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

        int numberIng = (Utility.getNumberIngredientsPerRecipe(
                context, cursor.getLong(LocalRecipeFragment.COL_RECIPE_ID)));

        viewHolder.numberIngredients.setText("# Ingredients: " + numberIng);

        byte [] image_array = cursor.getBlob(LocalRecipeFragment.COL_PHOTO);
        if(image_array != null){
            Bitmap image = Utility.getImage(image_array);
            viewHolder.recipePicture.setImageBitmap(getCircularBitmap(image));
        }
    }


    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
