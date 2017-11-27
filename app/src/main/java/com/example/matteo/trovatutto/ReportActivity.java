package com.example.matteo.trovatutto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.TextView;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView report_title, report_subtitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView image;
    private int[] tabIcons = {
            R.drawable.ic_rep_text,
         //   R.drawable.ic_rep_image,
            R.drawable.ic_rep_map
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();


        image = (ImageView) findViewById(R.id.backdrop);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //image.startAnimation(zoom);
            }
        });

        try {
            Glide.with(this).load(getIntent().getExtras().getString("immagine") ).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }


        report_title = (TextView) findViewById(R.id.report_detail_title);
        report_title.setText(getIntent().getStringArrayListExtra("info").get(0));
        report_subtitle = (TextView) findViewById(R.id.report_detail_subtitle);
        report_subtitle.setText(getIntent().getStringArrayListExtra("info").get(1));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle_info = new Bundle();
        bundle_info.putStringArrayList("info", getIntent().getStringArrayListExtra("info"));

        //ReportImageFragment ImaF = new ReportImageFragment();
        //ImaF.setArguments(bundle_im);

        ReportTextFragment TxtF = new ReportTextFragment();
        TxtF.setArguments(bundle_info);


        adapter.addFragment(TxtF, "REPORT");
        //adapter.addFragment(ImaF, "IMAGE");
        adapter.addFragment(new ReportMapFragment(), "MAP");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void goToHome(){

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToHome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(report_title.getText());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



}