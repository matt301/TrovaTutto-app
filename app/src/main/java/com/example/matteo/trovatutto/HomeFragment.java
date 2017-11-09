package com.example.matteo.trovatutto;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeFragment extends Fragment  implements View.OnClickListener {

    private  FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
       return view;
    }


    private void initViews(View view){

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    @Override

        public void onClick(View view) {
            Snackbar.make(view, "Eh volevi!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }




}

