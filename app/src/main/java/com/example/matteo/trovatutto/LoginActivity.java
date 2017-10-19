package com.example.matteo.trovatutto;

import android.app.ActionBar;
import android.app.Activity;

import android.os.Bundle;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;

import 	android.view.MotionEvent;

import android.widget.Toast;
import android.app.FragmentManager
import 	android.app.FragmentTransaction




public class LoginActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LandingFragment())).commit();

        }


    }




}


