package com.example.android.cookme;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cookme.data.Recipe;


/**
 * A placeholder fragment containing a simple view.
 */
public class Remote_Detail_ActivityFragment extends Fragment {

    public Remote_Detail_ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_remote__detail_, container, false);

        TextView name = (TextView) rootView.findViewById(R.id.detail_recipe_name);
        TextView ins = (TextView) rootView.findViewById(R.id.remote_instructions);
        Intent intent = getActivity().getIntent();

        if(intent!= null && intent.hasExtra(Intent.EXTRA_TEXT)){

            Recipe r = (Recipe) intent.getSerializableExtra(Intent.EXTRA_TEXT);
            name.setText(r.getName());
            ins.setText(r.getInstructions());
        }



        return rootView;
    }
}
