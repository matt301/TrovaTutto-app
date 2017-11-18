package com.example.matteo.trovatutto;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.example.matteo.trovatutto.models.Segnalazione;


public class ReportImageFragment extends Fragment {


    private ImageView iv_image;


    public ReportImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_image,container,false);
        initViews(view);
        return view;

    }

    private void initViews(View view){

        String immagine = getArguments().getString("immagine");
        iv_image = view.findViewById(R.id.report_iv_image);
        Glide.with(getContext()).load(immagine).into(iv_image);

    }


}
