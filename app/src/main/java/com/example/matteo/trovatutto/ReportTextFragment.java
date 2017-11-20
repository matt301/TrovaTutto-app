package com.example.matteo.trovatutto;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ReportTextFragment extends Fragment {


    private TextView tv_titolo,tv_sottotitolo,tv_descrizione,tv_indirizzo;
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

        tv_titolo       = view.findViewById(R.id.report_tv_titolo);
        tv_sottotitolo  = view.findViewById(R.id.report_tv_sottotitolo);
        tv_descrizione  = view.findViewById(R.id.report_tv_descrizione);
        tv_indirizzo    = view.findViewById(R.id.report_tv_indirizzo);

        tv_titolo.setText(titolo);
        tv_sottotitolo.setText(sottotitolo);
        tv_descrizione.setText(descrizione);
        tv_indirizzo.setText(indirizzo);


    }



}
