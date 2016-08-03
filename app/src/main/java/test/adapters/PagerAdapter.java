package test.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;
import test.R;
import test.fragments.GalleryFragment;
import test.fragments.ProjectManagerFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final List<CharSequence> titles;
    private final List<Fragment> fragments;

    @BindString(R.string.projects)
    protected String projects;
    @BindString(R.string.gallery)
    protected String gallery;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        ButterKnife.bind(this,(Activity) context);
        titles = new ArrayList<CharSequence>(){{add(projects);add(gallery);}};

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
