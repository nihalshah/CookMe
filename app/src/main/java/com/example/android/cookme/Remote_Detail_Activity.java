package com.example.android.cookme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.android.cookme.data.Recipe;


public class Remote_Detail_Activity extends ActionBarActivity {

    CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote__detail_);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        Recipe  recipe = (Recipe) getIntent().getSerializableExtra(Intent.EXTRA_TEXT);

        collapsingToolbar.setTitle(recipe.getName());

        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new Remote_Detail_ActivityFragment())
                    .commit();
        }
        */
    }

    public void setToolbarTitle(String title) {
        collapsingToolbar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remote__detail_, menu);
        return true;
    }


}
