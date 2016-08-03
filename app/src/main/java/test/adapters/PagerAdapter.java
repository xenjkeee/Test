package test.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import test.R;
import test.fragments.GalleryFragment;
import test.fragments.ProjectManagerFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final List<CharSequence> titles;
    private final List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = new ArrayList<>();
        Resources resources = context.getResources();
        titles.add(resources.getString(R.string.projects));
        titles.add(resources.getString(R.string.gallery));
        fragments = new ArrayList<>();
        fragments.add(new ProjectManagerFragment());
        fragments.add(new GalleryFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
