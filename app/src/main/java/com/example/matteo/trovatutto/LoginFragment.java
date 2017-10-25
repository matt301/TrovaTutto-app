package com.example.matteo.trovatutto;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btHome = (Button)view.findViewById(R.id.email_sign_in_button);
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent openHome = new Intent(getActivity(),HomeActivity.class);
                startActivity(openHome);
            }
        });

        return view;


    }








}