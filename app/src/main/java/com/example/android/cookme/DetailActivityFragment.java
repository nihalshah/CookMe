package com.example.android.cookme;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.cookme.data.Recipe;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent receivedIntent = getActivity().getIntent();
        if(receivedIntent != null){

            String uriS = receivedIntent.getDataString();

            if(uriS != null){
                TextView recipeName = (TextView) rootView.findViewById(R.id.recipeName_textView);
                recipeName.setText(uriS);
            }
        }

        return rootView;
    }
}
