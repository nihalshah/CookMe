package com.example.android.cookme.data;

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

/**
 * Created by Nihal on 7/26/15.
 */
public class RemoteServer {

    public String [] getRecipeDataFromString(String recipeList) throws JSONException {

        JSONObject recipeJSON = new JSONObject(recipeList);
        JSONArray recipeArrayJSON = new JSONArray(recipeList);

        int length = recipeArrayJSON.length();
        String []resultStr = new String[length];

        for( int i =0; i< length; i++){

            JSONObject individualRecipe = recipeArrayJSON.getJSONObject(i);
            String recipeName = individualRecipe.getString("name");
            String recipeInstructions = individualRecipe.getString("instructions");

            resultStr[i] = "Recipe Name : " + recipeName + "\nRecipe Instructions : " + recipeInstructions;

        }
        

        return resultStr;



    }

    public String [] getRecipeByName(String recipeName) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String recipeList = null;

        try {
            final String base = "http://45.55.139.196/recipe/";
            final String buildUri = base.concat(recipeName);

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
            try {
                return getRecipeDataFromString(recipeList);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }

}
