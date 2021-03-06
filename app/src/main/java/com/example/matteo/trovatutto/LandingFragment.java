package com.example.matteo.trovatutto;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.View.OnClickListener;



public class LandingFragment extends Fragment implements OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        view.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View view)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.fragment_frame,new LoginFragment());
        transaction.commit();
    }



}
