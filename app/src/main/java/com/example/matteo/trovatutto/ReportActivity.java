package com.example.matteo.trovatutto;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView report_title, report_subtitle, report_author, report_description, report_address, author_name,author_email,author_ntel;
    private ImageView image;
    private AlertDialog authorProfile;
    private Button send_email,new_contact

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

        report_author = (TextView) findViewById(R.id.reportACT_tv_autor);
        SpannableString content_aut = new SpannableString(getIntent().getStringArrayListExtra("info").get(4));
        content_aut.setSpan(new UnderlineSpan(), 0, content_aut.length(), 0);
        report_author.setText(content_aut);


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

                Intent intent = new Intent(ReportActivity.this, MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ADDRESS",getIntent().getStringArrayListExtra("info").get(3).toString());
                startActivity(intent);

            }
        });


        report_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_change_profile, null);

                author_name     = v.findViewById(R.id.tv_pp_author_name);
                author_email    = v.findViewById(R.id.tv__pp_author_email);
                author_ntel     = v.findViewById(R.id.tv_pp_author_ntel);
                send_email      = v.findViewById(R.id.btn_pp_send_email);
                new_contact     = v.findViewById(R.id.btn_pp_new_contact);


                author_name.setText("nome e cognome");
                author_email.setText(report_author.getText());
                author_ntel.setText("numero telefono azzurro");



                builder.setView(view);
                builder.setTitle("Author");
                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        authorProfile.dismiss();
                    }
                });

                authorProfile = builder.create();
                authorProfile.show();
               /* authorProfile.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String new_nome         = et_new_nome.getText().toString();
                        String new_cognome      = et_new_cognome.getText().toString();
                        String new_address      = et_new_address.getText().toString();
                        String new_birthdate    = et_new_birthdate.getText().toString();
                        String new_ntel         = et_new_ntel.getText().toString();
                        String new_description  = et_new_description.getText().toString();

                        if (!new_nome.isEmpty() && !new_cognome.isEmpty()) {
                            progress.setVisibility(View.VISIBLE);
                            changeProfileProcess(pref.getString(Constants.EMAIL, ""), new_nome, new_cognome, new_address, new_birthdate, new_ntel, new_description);
                        }
                        else{
                            tv_message.setVisibility(View.VISIBLE);
                            tv_message.setText("Name o Surname are empty");
                        }

                    }
                });
                */

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