package com.example.matteo.trovatutto;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class ReportTextFragment extends Fragment implements View.OnClickListener{


    private TextView tv_titolo,tv_sottotitolo,tv_descrizione,tv_indirizzo,tv_autore;
    private Button bt_delete;
    private ArrayList<String> info = new ArrayList<String>();

    public ReportTextFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report_text, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){

        info = getArguments().getStringArrayList("info");

        String titolo       = info.get(0);
        String sottotitolo  = info.get(1);
        String descrizione  = info.get(2);
        String indirizzo    = info.get(3);
        String autore       = info.get(4);

        tv_titolo       = view.findViewById(R.id.report_tv_titolo);
        tv_sottotitolo  = view.findViewById(R.id.report_tv_sottotitolo);
        tv_descrizione  = view.findViewById(R.id.report_tv_descrizione);
        tv_indirizzo    = view.findViewById(R.id.report_tv_indirizzo);
        tv_autore       = view.findViewById(R.id.report_tv_autore);
        bt_delete       = view.findViewById(R.id.report_bt_delete);

        tv_titolo.setText(titolo);
        tv_sottotitolo.setText(sottotitolo);
        tv_descrizione.setText(descrizione);
        tv_indirizzo.setText(indirizzo);
        tv_autore.setText(autore);

        bt_delete.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.report_bt_delete:

             //Snackbar.make(getView(), "Cancielo tuto", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
    }


}
