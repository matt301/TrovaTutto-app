package com.example.matteo.trovatutto;

import android.app.Activity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import 	android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;



public class LoginActivity extends Activity {

    private GestureDetector  mGesture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mGesture = new GestureDetector(this, mOnGesture);
    }

    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,  float velocityX,
                               float velocityY)
        {
            if (e1.getX()>e2.getX())
                Toast.makeText(getApplicationContext(), "verso sx", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "verso dx", Toast.LENGTH_SHORT).show();
            return true;
        }


    };


}


