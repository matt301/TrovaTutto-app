package com.example.matteo.trovatutto;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView report_title, report_subtitle, report_autor, report_description, report_address;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar_report);
        toolbar.setTitle(getIntent().getStringArrayListExtra("info").get(0));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        report_title = (TextView) findViewById(R.id.reportACT_tv_title);
        report_title.setText(getIntent().getStringArrayListExtra("info").get(0));

        report_subtitle = (TextView) findViewById(R.id.reportACT_tv_subtitle);
        report_subtitle.setText(getIntent().getStringArrayListExtra("info").get(1));

        report_description = (TextView) findViewById(R.id.reportACT_tv_description);
        report_description.setText(getIntent().getStringArrayListExtra("info").get(2));

        report_address = (TextView) findViewById(R.id.reportACT_tv_address);
        SpannableString content_add = new SpannableString(getIntent().getStringArrayListExtra("info").get(3));
        content_add.setSpan(new UnderlineSpan(), 0, content_add.length(), 0);
        report_address.setText(content_add);

        report_autor = (TextView) findViewById(R.id.reportACT_tv_autor);
        SpannableString content_aut = new SpannableString(getIntent().getStringArrayListExtra("info").get(4));
        content_aut.setSpan(new UnderlineSpan(), 0, content_aut.length(), 0);
        report_autor.setText(content_aut);


        image = findViewById(R.id.reportACT_iv_image);
        try {
            Glide.with(this).load(getIntent().getExtras().getString("immagine")).into((ImageView) findViewById(R.id.reportACT_iv_image));
        } catch (Exception e) {
            e.printStackTrace();
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ReportImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("immagine", getIntent().getStringExtra("immagine"));
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(R.id.report_frame_layout, fragment).commit();
            }
        });

        report_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        report_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ReportActivity.this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ADDRESS",getIntent().getStringArrayListExtra("info").get(3).toString());
                startActivity(intent);

            }
        });

    }


    private void goToHome() {

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

}