package com.example.android.cookme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.cookme.data.Ingredient;
import com.example.android.cookme.data.Recipe;
import com.example.android.cookme.data.RecipeProviderByJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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


    private RemoteRecipeAdapter mRemoteRecipeAdapter;

    private ListView mRecipesList;
    private EditText mIngredientInput;
    private boolean inSearch = false;
    private Dialog splash;
    private ProgressDialog Searchdialog;
    private boolean haveRequested;

    private static final String STATE_HAVE_REQUESTED = "haveRequestedState";


    public RemoteRecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_remote_recipe, container, false);

        ArrayList<Recipe> list = new ArrayList<Recipe>();
        mRemoteRecipeAdapter  = new RemoteRecipeAdapter(getActivity(),0, list);


        mIngredientInput = (EditText) rootView.findViewById(R.id.ingredient_input);
        mRecipesList = (ListView) rootView.findViewById(R.id.recipes_list);
        //mSearchButton = (ImageButton) rootView.findViewById(R.id.search_ingredient_button);


        mRecipesList.setAdapter(mRemoteRecipeAdapter);

        // Exec async load task
        if(!haveRequested){
            final RemoteRecipeTask remoteRecipeTask = new RemoteRecipeTask();
            remoteRecipeTask.execute("Get");
        }

        mRecipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Recipe actualRecipe = mRemoteRecipeAdapter.getItem(i);
                String actualImage = actualRecipe.getImage();
                String imagePath = actualImage;
                try {
                    imagePath = insertBase64IntoLocalFile(actualImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Recipe copyRecipe = new Recipe(actualRecipe.getName(),actualRecipe.getIngredients(), actualRecipe.getInstructions(),imagePath);
                Intent intent = new Intent(getActivity(), Remote_Detail_Activity.class).
                        putExtra(Intent.EXTRA_TEXT, copyRecipe);
                startActivity(intent);
            }
        });

        mIngredientInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //TODO Search into remote function
                    inSearch = true;
                    new RemoteRecipeTask().execute("Search", mIngredientInput.getText().toString());


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


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(haveRequested){
            outState.putBoolean(STATE_HAVE_REQUESTED, haveRequested);
        }
        super.onSaveInstanceState(outState);
    }

    private String insertBase64IntoLocalFile(String image) throws IOException {

        String storagePath = getActivity().getFilesDir().getPath();
        File fileHoldingImagePath = new File(storagePath + "imagePath.txt");

        FileOutputStream stream = new FileOutputStream(fileHoldingImagePath);
        try{
            stream.write(image.getBytes());
        }finally {
            stream.close();
        }
        return fileHoldingImagePath.getPath();
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

            Makes an HTTP connection with Remote Server.
            "GET's" data from Remote DataBase.

         */
        @Override
        protected void onPreExecute(){
            Searchdialog = new ProgressDialog(getActivity());

            splash = new Dialog(getActivity(), R.style.splash);
            if(inSearch){
               Searchdialog.setMessage("Searching in the server...");
                 Searchdialog.show();
            } else{
                splash.show();
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> result) {

            super.onPostExecute(result);
            haveRequested = true;
            if (inSearch){
                if(Searchdialog.isShowing()) {
                    Searchdialog.dismiss();
                }

            }
            if(splash.isShowing()){
                splash.dismiss();
            }
            if( result != null){

               mRemoteRecipeAdapter.clear();

                for(Recipe recipe : result){
                    mRemoteRecipeAdapter.add(recipe);
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
                String base = "http://45.55.139.196:5000/";
                String buildUri;
                if( params[0].equals("Search")) {
                    buildUri = base.concat("ingredient/"+params[1]);
                }
                else{
                    buildUri = base;
                }

                URL url = new URL(buildUri);
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
                for(int i =0; i < arr.length(); i++){
                    JSONObject r = arr.getJSONObject(i);
                    result.add(convertRecipe(r));
                }


                return result;


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


        private Recipe convertRecipe(JSONObject obj) throws JSONException, IOException {
            String name = obj.getString("name");
            String instructions = obj.getString("instructions");
            LinkedList<Ingredient> ingredients = getIngredients(obj);
            String image = obj.getString("image");

            return new Recipe(name, ingredients, instructions, image);
        }


    }

}
