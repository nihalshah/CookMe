package com.example.android.cookme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cookme.data.Ingredient;

import java.util.LinkedList;

/**
 * Created by Nihal on 8/9/15.
 */
public class RemoteIngredientAdapter extends ArrayAdapter<Ingredient> {

    private LinkedList<Ingredient> remote_ingredients;
    private Context context;


    public RemoteIngredientAdapter(Context context, int resource, LinkedList<Ingredient> remote_ingredients) {
        super(context, resource);
        this.context = context;
        this.remote_ingredients = remote_ingredients;
    }

    /*

        Inflates the ListView of Ingredients

     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_ingredients, null);
        }

        Ingredient ing = remote_ingredients.get(position);
        TextView ingName = (TextView) v.findViewById(R.id.list_item_ingredients_textView);
        TextView ingAttr = (TextView) v.findViewById(R.id.list_item_ingredients_attributesTextView);
        ingName.setText(ing.getName());

        String attributes = "Units: " + ing.getUnits()+
                            "  Quantity: " + ing.getQuantity();

        ingAttr.setText(attributes);
        return v;
    }



    public int getCount() {
        if (remote_ingredients != null)
            return remote_ingredients.size();
        return 0;
    }

    public Ingredient getItem(int position) {
        if (remote_ingredients != null)
            return remote_ingredients.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (remote_ingredients != null)
            return remote_ingredients.get(position).hashCode();
        return 0;
    }





}
