package com.example.matteo.trovatutto;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.matteo.trovatutto.models.ServerRequest;
import com.example.matteo.trovatutto.models.ServerResponse;
import com.example.matteo.trovatutto.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.matteo.trovatutto.Constants.TAG;


public class ReportActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView report_title, report_subtitle, report_author, report_description, report_address, author_name,author_email,author_ntel,author_bio;
    private ImageView image;
    private AlertDialog authorProfile;
    private ImageButton send_email,new_contact;

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
                view = inflater.inflate(R.layout.dialog_public_profile, null);

                author_name     = view.findViewById(R.id.tv_pp_author_name);
                author_email    = view.findViewById(R.id.tv_pp_author_email);
                author_ntel     = view.findViewById(R.id.tv_pp_author_ntel);
                author_bio      = view.findViewById(R.id.tv_pp_author_description);
                send_email      = (ImageButton)view.findViewById(R.id.btn_pp_send_email);
                new_contact     = (ImageButton)view.findViewById(R.id.btn_pp_new_contact);

                showAuthorProfile(report_author.getText().toString());


                send_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(findViewById(R.id.report_activity_layout),"Invia email", Snackbar.LENGTH_LONG).show();
                    }
                });
                new_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });


                builder.setView(view);
                builder.setTitle("Author");
                builder.setIcon(R.drawable.ic_person);
               /* builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                */
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

    private void showAuthorProfile(String email){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.SHOW_PUBLIC_PROFILE);
        request.setUser(user);
        final Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                if(resp.getResult().equals(Constants.SUCCESS)){

                    String nome = resp.getUser().getName() +' '+ resp.getUser().getCognome();
                    String tel = resp.getUser().getNtel();
                    String desc = resp.getUser().getDescrizione();
                    author_name.setText(nome);
                    author_email.setText(report_author.getText());
                    author_ntel.setText(tel);
                    author_bio.setText(desc);

                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(TAG,"failed");
                Snackbar.make(findViewById(R.id.report_activity_layout), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

}