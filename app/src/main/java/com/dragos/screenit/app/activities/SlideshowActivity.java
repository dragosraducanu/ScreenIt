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
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.dragos.screenit.app.R;
import com.dragos.screenit.app.fragments.SlideshowPageFragment;
import com.dragos.screenit.app.server.S3Uploader;
import com.dragos.screenit.app.server.Service;

import java.util.ArrayList;

;


public class SlideshowActivity extends FragmentActivity {
    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_slideshow);




        setupViewPager();
        setupTransparentActionBar();

        setupProgressBar(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.getBoolean("upload_started")) {
            startUpload();
        }






        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Service.getInstance().goToPageInBrowser(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("upload_started", true);
        savedInstanceState.putBoolean("progress_bar_visible", mProgressBar.getVisibility() == View.VISIBLE);
    }

    public static void incrementProgress(){
        mProgressBar.incrementProgressBy(1);
    }
    public static void setProgressBarVisibility(final int visibility){
        final boolean show = visibility == View.VISIBLE;

        float start = 0f;
        float end = 1.0f;

        if(!show) {
            start += end;
            end = start - end;
            start -= end;
        }

        AlphaAnimation alpha = new AlphaAnimation(start, end);
        alpha.setFillAfter(true);
        alpha.setDuration(500);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(show){
                    mProgressBar.setVisibility(visibility);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
               if(!show) {
                   mProgressBar.setVisibility(visibility);
               }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mProgressBar.startAnimation(alpha);


        //mProgressBar.setVisibility(visibility);
    }



    private void setupProgressBar(Bundle savedInstanceState){
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        int max = getIntent().getStringArrayListExtra("paths").size();
        mProgressBar.setMax(max);
        mProgressBar.setIndeterminate(false);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mProgressBar.getLayoutParams();

        if(savedInstanceState == null || !savedInstanceState.getBoolean("progress_bar_visible")) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    private int getActionBarHeight(){
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            return TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return -1;
    }

    private void setupViewPager(){
        mPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<String> imagePaths = getIntent().getStringArrayListExtra("paths");
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), imagePaths);
        mPager.setAdapter(mPagerAdapter);
    }

    private void setupTransparentActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#55000000")));
    }
    private void startUpload(){
        Log.w("act", "starting upload");
        S3Uploader uploader = new S3Uploader(this, "AKIAJCPU6BGAQFE4LRSA", "TZRwpTUBljDz8yOFP0WVFgVJCkOF2NZn7wFT5dP3");
      /*  ArrayList<String> demoURLS = new ArrayList<String>();
        demoURLS.add("http://miriadna.com/desctopwalls/images/max/Ideal-landscape.jpg");
        demoURLS.add("http://ww1.prweb.com/prfiles/2013/11/11/11318192/Landscape%20Design%20Bergen%20County%20NJ.jpg");
        demoURLS.add("http://www.w8themes.com/wp-content/uploads/2013/11/Free-Landscape-Wallpaper.jpg");*/
        ArrayList<String> imagePaths = getIntent().getStringArrayListExtra("paths");
        uploader.startBatchAsyncUpload(imagePaths, this);
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
