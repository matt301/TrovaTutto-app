package com.example.matteo.trovatutto;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.matteo.trovatutto.models.ServerRequest;
import com.example.matteo.trovatutto.models.ServerResponse;
import com.example.matteo.trovatutto.models.User;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.matteo.trovatutto.Constants.TAG;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tv_nome,tv_email,tv_message,tv_birthdate,tv_address,tv_ntel,tv_description;
    private SharedPreferences pref;
    private AppCompatButton btn_change_password, btn_change_profile,btn_logout;
    private EditText et_old_password,et_new_password;
    private EditText et_new_nome,et_new_cognome,et_new_birthdate,et_new_ntel,et_new_address,et_new_description;
    private AlertDialog dialog;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        tv_nome.setText((pref.getString(Constants.NAME, "")+" "+(pref.getString(Constants.SURNAME, ""))));
        tv_email.setText(pref.getString(Constants.EMAIL,""));
        tv_birthdate.setText(pref.getString(Constants.BIRTHDATE,""));
        tv_ntel.setText(pref.getString(Constants.NTEL,""));
        tv_address.setText(pref.getString(Constants.ADDRESS,""));
        tv_description.setText(pref.getString(Constants.BIO,""));

    }

    private void initViews(View view){

        tv_nome = (TextView)view.findViewById(R.id.tv_nome);
        tv_email = (TextView)view.findViewById(R.id.tv_email);
        tv_birthdate = (TextView)view.findViewById(R.id.tv_birthdate);
        tv_ntel = (TextView)view.findViewById(R.id.tv_ntel);
        tv_address = (TextView)view.findViewById(R.id.tv_address);
        tv_description = (TextView)view.findViewById(R.id.tv_description);
        btn_change_password = (AppCompatButton)view.findViewById(R.id.btn_chg_password);
        btn_change_profile = (AppCompatButton)view.findViewById(R.id.btn_chg_profile);
        btn_logout = (AppCompatButton)view.findViewById(R.id.btn_logout);



        btn_change_password.setOnClickListener(this);
        btn_change_profile.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }

    private void showDialog(String tipo){

        if(tipo.equals("PASSWORD")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_change_password, null);
            et_old_password = (EditText) view.findViewById(R.id.et_old_password);
            et_new_password = (EditText) view.findViewById(R.id.et_new_password);
            tv_message = (TextView) view.findViewById(R.id.tv_message);
            progress = (ProgressBar) view.findViewById(R.id.progress);
            builder.setView(view);
            builder.setTitle("Change Password");
            builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String old_password = et_old_password.getText().toString();
                    String new_password = et_new_password.getText().toString();
                    if (!old_password.isEmpty() && !new_password.isEmpty()) {

                        progress.setVisibility(View.VISIBLE);
                        changePasswordProcess(pref.getString(Constants.EMAIL, ""), old_password, new_password);

                    } else {

                        tv_message.setVisibility(View.VISIBLE);
                        tv_message.setText("Fields are empty");
                    }
                }
            });
        }
        else if(tipo.equals("PROFILE")){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_change_profile, null);

            et_new_nome         = view.findViewById(R.id.et_new_nome);
            et_new_cognome      = view.findViewById(R.id.et_new_cognome);
            et_new_address      = view.findViewById(R.id.et_new_address);
            et_new_birthdate    = view.findViewById(R.id.et_new_birthdate);
            et_new_ntel         = view.findViewById(R.id.et_new_ntel);
            et_new_description  = view.findViewById(R.id.et_new_description);

            tv_message =  view.findViewById(R.id.tv_message);
            progress   =  view.findViewById(R.id.progress);

            et_new_nome.setText(pref.getString(Constants.NAME, ""));
            et_new_cognome.setText(pref.getString(Constants.SURNAME, ""));
            et_new_address.setText(pref.getString(Constants.ADDRESS, ""));
            et_new_birthdate.setText(pref.getString(Constants.BIRTHDATE, ""));
            et_new_ntel.setText(pref.getString(Constants.NTEL, ""));
            et_new_description.setText(pref.getString(Constants.BIO, ""));



            builder.setView(view);
            builder.setTitle("Change Profile");
            builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_chg_password:
                showDialog("PASSWORD");
                break;
            case R.id.btn_chg_profile:
                showDialog("PROFILE");
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.NAME,"");
        editor.clear();
        editor.apply();

        goToLogin();
    }

    private void goToLogin(){


        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void changePasswordProcess(String email,String old_password,String new_password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setOld_password(old_password);
        user.setNew_password(new_password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }else {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(resp.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(TAG,"failed");
                progress.setVisibility(View.GONE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(t.getLocalizedMessage());


            }
        });


    }

    private void changeProfileProcess( String email,String nome, String cognome,String indirizzo,String datadinascita,String ntel, String descrizione){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setName(nome);
        user.setCognome(cognome);
        user.setIndirizzo(indirizzo);
        user.setDatadinascita(datadinascita);
        user.setNtel(ntel);
        user.setDescrizione(descrizione);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PROFILE_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.NAME,resp.getUser().getName());
                    editor.putString(Constants.SURNAME,resp.getUser().getCognome());
                    editor.putString(Constants.BIRTHDATE,resp.getUser().getDatadinascita());
                    editor.putString(Constants.NTEL,resp.getUser().getNtel());
                    editor.putString(Constants.ADDRESS,resp.getUser().getIndirizzo());
                    editor.putString(Constants.BIO,resp.getUser().getDescrizione());
                    editor.apply();
                    dialog.dismiss();
                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }


}
