package com.example.matteo.trovatutto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class NewReportFragment extends Fragment  implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    public static final int GALLERY_INTENT_CALLED = 1;
    //public static final int GALLERY_KITKAT_INTENT_CALLED = 2;
    public static final int REQUEST_IMAGE_CAPTURE = 12345;

    private AppCompatButton btn_sendreport, btn_insertfoto;
    private Button btn_dialog_gallery, btn_dialog_camera;
    private EditText et_report_title, et_report_subtitle, et_report_address,et_report_description;
    private TextView tv_report_category;
    private Spinner category_spinner;
    private ProgressBar progress;
    private AlertDialog dialog;
    private ImageView iv_report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_report,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        btn_sendreport        = view.findViewById(R.id.btn_sendreport);
        btn_insertfoto        = view.findViewById(R.id.btn_insertfoto);
        btn_dialog_gallery    = view.findViewById(R.id.btn_dialog_gallery);
        btn_dialog_camera     = view.findViewById(R.id.btn_dialog_camera);

        category_spinner      = view.findViewById(R.id.sp_report_category);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.category_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            category_spinner.setAdapter(adapter);

        et_report_title       = view.findViewById(R.id.et_report_title);
        et_report_subtitle    = view.findViewById(R.id.et_report_subtitle);
        tv_report_category    = view.findViewById(R.id.tv_report_category);
        et_report_address     = view.findViewById(R.id.et_report_address);
        et_report_description = view.findViewById(R.id.et_report_description);
        iv_report             = view.findViewById(R.id.iv_report);

        progress              = view.findViewById(R.id.progress);

        btn_sendreport.setOnClickListener(this);
        btn_insertfoto.setOnClickListener(this);

        category_spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_insertfoto:
                showDialog("INSERT PHOTO");
                break;
            case R.id.btn_sendreport:
                break;

        }

    }

    public void sendReportProcess(){
        //send nudes pls
    }

    private void showDialog(String tipo){
        if(tipo.equals("INSERT PHOTO")){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_insert_photo, null);

            btn_dialog_gallery =  view.findViewById(R.id.btn_dialog_gallery);
            btn_dialog_camera =  view.findViewById(R.id.btn_dialog_camera);

            builder.setView(view);
            builder.setTitle("Where?");
            dialog = builder.create();
            dialog.show();

            btn_dialog_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPhoto();
                    dialog.cancel();
                }
            });

            btn_dialog_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callCamera();
                    dialog.cancel();
                }
            });




        }

    }


    public void addPhoto(){

        Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery_intent,GALLERY_INTENT_CALLED);

    }


    public void callCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Fragment frag = this;
        /** Pass your fragment reference **/
        frag.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if(reqCode == GALLERY_INTENT_CALLED && resultCode == RESULT_OK && data != null){

            Uri imageUri = data.getData();
            iv_report.setImageURI(imageUri);
        }
        else {
            Snackbar.make(this.getView(), "You haven't picked Image", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }


    /*    if (resultCode ==  GALLERY_INTENT_CALLED || resultCode ==  GALLERY_KITKAT_INTENT_CALLED ) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = this.getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                iv_report.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this.getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this.getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }*/
    }


}
