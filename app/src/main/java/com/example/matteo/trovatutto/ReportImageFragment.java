package com.example.matteo.trovatutto;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
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


    private TouchImageView iv_image;


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
        Glide.with(view.getContext()).load(immagine).into(iv_image);
        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().remove(ReportImageFragment.this).commit();
            }
        });



    }


}
