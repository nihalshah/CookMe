package com.example.android.cookme;

/**
 * Created by armando on 7/30/15.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                //Fragement for Local Recipe
                return new RemoteRecipeFragment();
            case 1:
                //Fragment for Remote Recipe
                return new LocalRecipeFragment();
        }
        return null;

    }

    @Override
    public int getCount() {

        return 2; //No of Tabs
    }

}