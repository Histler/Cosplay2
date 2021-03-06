package ru.badr.cosplay2.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;

import ru.badr.base.fragment.BaseViewPagerFragment;
import ru.badr.cosplay2.adapter.SchemePagerAdapter;
import ru.badr.opencon.R;

/**
 * Created by ABadretdinov
 * 03.11.2015
 * 17:43
 */
public class SchemeFragment extends BaseViewPagerFragment {
    @Override
    public int getLayoutId() {
        return R.layout.simple_viewpager_layout;
    }

    @Override
    @TabLayout.Mode
    public int getTabMode() {
        return TabLayout.MODE_SCROLLABLE;
    }

    @Override
    public FragmentPagerAdapter getAdapter() {
        return new SchemePagerAdapter(getActivity(), getChildFragmentManager());
    }

    @Override
    protected String getTitle() {
        return getString(R.string.scheme);
    }
}
