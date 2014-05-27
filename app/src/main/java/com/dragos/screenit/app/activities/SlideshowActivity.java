package com.dragos.screenit.app.activities;


import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.dragos.screenit.app.R;
import com.dragos.screenit.app.fragments.SlideshowPageFragment;

import java.util.ArrayList;

;


public class SlideshowActivity extends FragmentActivity {
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_slideshow);

        mPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<String> imagePaths = getIntent().getStringArrayListExtra("paths");
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), imagePaths);
        mPager.setAdapter(mPagerAdapter);
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55000000")));

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<String> paths;
        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<String> p) {
            super(fm);
            this.paths = p;
        }

        @Override
        public Fragment getItem(int position) {
            SlideshowPageFragment frag = new SlideshowPageFragment();
            Bundle b = new Bundle();
            b.putString("imgPath", paths.get(position));
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return paths.size();
        }
    }
}
