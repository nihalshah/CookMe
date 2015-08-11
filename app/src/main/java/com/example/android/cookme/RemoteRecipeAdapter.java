package com.example.android.cookme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cookme.data.Recipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nihal on 8/3/15.
 */
public class RemoteRecipeAdapter extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> remote_recipes;
    private Context context;


    public RemoteRecipeAdapter(Context context, int resource, ArrayList<Recipe> objects) {
        super(context, resource, objects);
        this.remote_recipes = objects;
        this.context = context;

    }


    public int getCount() {
        if (remote_recipes != null)
            return remote_recipes.size();
        return 0;
    }

    public Recipe getItem(int position) {
        if (remote_recipes != null)
            return remote_recipes.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (remote_recipes != null)
            return remote_recipes.get(position).hashCode();
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_recipes, null);
        }

        Recipe r = remote_recipes.get(position);

        TextView text = (TextView) v.findViewById(R.id.list_item_recipes_textview);
        ImageView img = (ImageView) v.findViewById(R.id.recipe_picture_imageview);
        text.setText(r.getName());

        String imageStringReference = r.getImage();
        File filePath = new File(imageStringReference);

        int length = (int) filePath.length();
        byte [] bytes = new byte[length];

        FileInputStream input = null;
        try {
            input = new FileInputStream(filePath);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            input.read(bytes);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            input.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String imageString = new String(bytes);
        Log.i("Image in Adapter", imageString);
        byte [] decodedString = Base64.decode(imageString, Base64.URL_SAFE);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        img.setImageBitmap(getCircularBitmap(decodedByte));

        return v;
    }

    public List<Recipe> getItemList() {
        return remote_recipes;
    }

    public void setItemList(ArrayList<Recipe> itemList) {
        this.remote_recipes = itemList;
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
