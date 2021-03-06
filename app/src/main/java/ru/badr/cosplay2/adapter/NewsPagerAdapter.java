package ru.badr.cosplay2.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.histler.insta.fragment.InstagramFragment;

import ru.badr.cosplay2.fragment.TwitterFragment;
import ru.badr.opencon.R;


/**
 * Created by ABadretdinov
 * 23.09.2015
 * 16:34
 */
public class NewsPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public NewsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mTitles = context.getResources().getStringArray(R.array.news_tabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TwitterFragment();
            case 1:
            default:
                return new InstagramFragment();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
