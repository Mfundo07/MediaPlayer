package com.example.android.mediaplayer;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Admin on 6/28/2017.
 */

public class MusicCategoryAdapter extends FragmentPagerAdapter {
    Context sContext;
    @Override
    public CharSequence getPageTitle(int position) {

            return sContext.getString(R.string.category_songs);

    }

    public MusicCategoryAdapter(Context context,FragmentManager fm) {
        super(fm);
        sContext = context;
    }

    @Override
    public Fragment getItem(int position) {

            return new SongsFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }
}
