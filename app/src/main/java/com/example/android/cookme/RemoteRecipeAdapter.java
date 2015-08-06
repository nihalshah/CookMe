package com.example.android.cookme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cookme.data.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nihal on 8/3/15.
 */
public class RemoteRecipeAdapter extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> remote_recipes;
    private Context context;

//    public static class ViewHolder{
//        public final TextView recipeName;
//        public final ImageView recipePicture;
//
//        public ViewHolder(View view){
//            recipeName = (TextView)view.findViewById(R.id.list_item_recipes_textview);
//            recipePicture = (ImageView)view.findViewById(R.id.recipe_picture_imageview);
//        }
//    }



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
            v = inflater.inflate(R.layout.list_item_remote_recipes, null);
        }

        Recipe r = remote_recipes.get(position);
        TextView text = (TextView) v.findViewById(R.id.list_item_recipes_textview);
        text.setText(r.getName());

        return v;
    }

    public List<Recipe> getItemList() {
        return remote_recipes;
    }

    public void setItemList(ArrayList<Recipe> itemList) {
        this.remote_recipes = itemList;
    }




//    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipes, viewGroup, false);
//
//        ViewHolder viewHolder = new ViewHolder(view);
//        view.setTag(viewHolder);
//
//        return view;
//    }
//
//
//    public void bindView(View view, Context context,Recipe recipe) {
//
//        ViewHolder viewHolder = (ViewHolder) view.getTag();
//
//        viewHolder.recipeName.setText(String.valueOf(recipe.getName()));
//
////        byte [] image_array = cursor.getBlob(LocalRecipeFragment.COL_PHOTO);
////        if(image_array != null){
////            Bitmap image = Utility.getImage(image_array);
////            viewHolder.recipePicture.setImageBitmap(image);
////        }
//    }
//

}
