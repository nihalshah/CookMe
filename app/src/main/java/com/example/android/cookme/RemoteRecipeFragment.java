package com.example.android.cookme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.cookme.data.Ingredient;
import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeProviderByJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A placeholder fragment containing a simple view.
 */
public class RemoteRecipeFragment extends Fragment {


    private static final int RECIPE_LOADER = 0;
    private RecipeProviderByJSON mListRecipes;
    private RecipeAdapter mRecipeAdapter;
    private String mIngredientTyped;
    private String mIngredientsSelected;
    private RemoteRecipeAdapter mremoteRecipeAdapter;

    private ImageButton mSearchButton;
    private ListView mRecipesList;
    private EditText mIngredientInput;
    private TextView mIngredientsQuerying;
    private Button mClearQuery;


    public RemoteRecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_remote_recipe, container, false);

        ArrayList<Recipe> list = new ArrayList<Recipe>();
        mremoteRecipeAdapter  = new RemoteRecipeAdapter(getActivity(),0, list);


        mIngredientTyped = "";
        mIngredientsSelected = "";

        mIngredientInput = (EditText) rootView.findViewById(R.id.ingredient_input);
        mIngredientsQuerying = (TextView) rootView.findViewById(R.id.ingredients_in_query_textview);
        mClearQuery = (Button) rootView.findViewById(R.id.clear_list_ingredients_button);
        mRecipesList = (ListView) rootView.findViewById(R.id.recipes_list);
        mSearchButton = (ImageButton) rootView.findViewById(R.id.search_ingredient_button);


        mRecipesList.setAdapter(mremoteRecipeAdapter);

        // Exec async load task
        RemoteRecipeTask remoteRecipeTask = new RemoteRecipeTask();
        remoteRecipeTask.execute();


        mRecipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Recipe r = mremoteRecipeAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), Remote_Detail_Activity.class).
                        putExtra(Intent.EXTRA_TEXT, r);
                startActivity(intent);
            }
        });

        //Button Pressed event
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIngredientTyped = mIngredientInput.getText().toString();
                mIngredientInput.setText("");

                if (mIngredientsSelected.length() == 0)
                    mIngredientsSelected += mIngredientTyped;
                else
                    mIngredientsSelected += "-" + mIngredientTyped; //DO NOT CHANGE THE '-' ... necessary for later split

                mIngredientsQuerying.setText(mIngredientsSelected);

                //restartLoader();
            }
        });

        mClearQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIngredientsSelected = "";
                mIngredientsQuerying.setText(mIngredientsSelected);

               // restartLoader();
            }
        });

        return rootView;
    }




    public class RemoteRecipeTask extends AsyncTask<String, Void, ArrayList<Recipe>>{


        /*
            Returns a LinkedList of Ingredients and all their information
         */

        public LinkedList<Ingredient> getIngredients(JSONObject dish) {

            JSONArray ingredientInfo = null;
            LinkedList<Ingredient> allIngredients = new LinkedList<Ingredient>();
            try {
                ingredientInfo = dish.getJSONArray("ingredients");
                for(int i = 0; i< ingredientInfo.length(); i++){
                    double quantity = Double.parseDouble(ingredientInfo.getJSONObject(i).getString("ingredient quantity"));
                    String ingredientName = ingredientInfo.getJSONObject(i).getString("ingredient name");
                    String ingredientUnit = ingredientInfo.getJSONObject(i).getString("ingredient units");
                    Ingredient ingredients = new Ingredient(ingredientName, quantity, ingredientUnit);
                    allIngredients.add(ingredients);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return allIngredients;
        }

        /*
            Extracts Recipe Data from JSON
         */

//        public List<Recipe> getRecipeDataFromString(String recipeList) throws JSONException {
//
//            JSONObject recipeJSON = new JSONObject(recipeList);
//            JSONArray recipeArrayJSON = new JSONArray(recipeList);
//
//            int length = recipeArrayJSON.length();
//            List<Recipe> recipeList = new ArrayList<Recipe>();
//
//            for( int i =0; i< length; i++){
//
//                JSONObject individualRecipe = recipeArrayJSON.getJSONObject(i);
//                String recipeName = individualRecipe.getString("name");
//                String recipeInstructions = individualRecipe.getString("instructions");
//                LinkedList<Ingredient> ingredients = getIngredients(individualRecipe);
//                resultStr[i] = "Recipe Name : " + recipeName + "\nRecipe Instructions : " + recipeInstructions;
//
//            }
//
//            return resultStr;
//
//
//
//        }

        /*

            Makes an HTTP connection with Remote Server.
            "GET's" data from Remote DataBase.

         */

        @Override
        protected void onPostExecute(ArrayList<Recipe> result) {

            super.onPostExecute(result);
            if( result != null){

                mremoteRecipeAdapter.clear();

                for(Recipe recipe : result){
                    mremoteRecipeAdapter.add(recipe);
                }

            }
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String recipeList = null;
            ArrayList<Recipe> result = new ArrayList<Recipe>();

            try {
                final String base = "http://45.55.139.196:5000/";
               // final String buildUri = base.concat("burrito");

                URL url = new URL(base);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                recipeList = buffer.toString();
                Log.i("Recipe is : ", recipeList);
                JSONArray arr = new JSONArray(recipeList);
                Log.i("JSON is : ", arr.toString());
                for(int i =0; i < arr.length(); i++){
                    JSONObject r = arr.getJSONObject(i);
                    result.add(convertRecipe(r));
                }


                return result;
//                try {
//                    return getRecipeDataFromString(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }


        private Recipe convertRecipe(JSONObject obj) throws JSONException {
            String name = obj.getString("name");
            String instructions = obj.getString("instructions");
            LinkedList<Ingredient> ingredients = getIngredients(obj);
            String image = obj.getString("image");

            return new Recipe(name, ingredients, instructions, image);
        }

    }

}