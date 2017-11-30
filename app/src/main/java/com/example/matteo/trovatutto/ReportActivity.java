package com.example.matteo.trovatutto;

import android.app.AlertDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView report_title, report_subtitle, report_autor, report_description,report_address;
    private Button bt_BigShow, bt_map;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView image;
    private TouchImageView iv_big_image;
    private int[] tabIcons = {
            R.drawable.ic_rep_text,
         //   R.drawable.ic_rep_image,
            R.drawable.ic_rep_map
    };
    private boolean zoomOut =  false;
    private AlertDialog dialog;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

   /*   toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


        report_title = (TextView) findViewById(R.id.reportACT_tv_title);
        report_title.setText(getIntent().getStringArrayListExtra("info").get(0));

        report_subtitle = (TextView) findViewById(R.id.reportACT_tv_subtitle);
        report_subtitle.setText(getIntent().getStringArrayListExtra("info").get(1));

        report_description = (TextView) findViewById(R.id.reportACT_tv_description);
        report_description.setText(getIntent().getStringArrayListExtra("info").get(2));

        report_address = (TextView) findViewById(R.id.reportACT_tv_address);
        report_address.setText(getIntent().getStringArrayListExtra("info").get(3));

        report_autor = (TextView) findViewById(R.id.reportACT_tv_autor);
        report_autor.setText(getIntent().getStringArrayListExtra("info").get(4));

        bt_BigShow = (Button) findViewById(R.id.reportACT_bt_autor);

        image =  findViewById(R.id.reportACT_iv_image);

        try {
            Glide.with(this).load(getIntent().getExtras().getString("immagine") ).into((ImageView) findViewById(R.id.reportACT_iv_image));
        } catch (Exception e) {
            e.printStackTrace();
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ReportImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("immagine",getIntent().getStringExtra("immagine"));
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.report_frame_layout, fragment).commit();
            }
        });


        bt_BigShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        report_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


      /*  initCollapsingToolbar();


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
        setupTabIcons();*/


    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
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
    }*/



}