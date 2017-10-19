package com.example.matteo.trovatutto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LandingFragment extends Fragment {

    private OnSelectionListener listener;

    public interface OnSelectionListener{

    }
        public LandingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        return view;
    }

/*
    @Override
    public void onAttach(Activity activity) {

    }

    @Override
    public void onDetach() {

    }
  */

}
