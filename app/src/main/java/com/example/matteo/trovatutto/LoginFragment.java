package com.example.matteo.trovatutto;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText Emailbox = (EditText) view.findViewById(R.id.email);
        final EditText Passwordbox = (EditText) view.findViewById(R.id.password);

        Button btHome = (Button)view.findViewById(R.id.email_sign_in_button);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(((Emailbox.getText().toString()).equals(getString(R.string.Email)))&&((Passwordbox.getText().toString()).equals(getString(R.string.Password)))){
                    Intent openHome = new Intent(getActivity(),HomeActivity.class);
                    startActivity(openHome);
                }


            }
        });

        return view;


    }








}