package com.example.matteo.trovatutto;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


public class NewReportFragment extends Fragment  implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;

    private AppCompatButton btn_sendreport, btn_insertfoto;
    private Button btn_dialog_gallery, btn_dialog_camera;
    private EditText et_report_title, et_report_subtitle, et_report_address,et_report_description;
    private TextView tv_report_category;
    private Spinner category_spinner;
    private ProgressBar progress;
    private AlertDialog dialog;

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
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_spinner.setAdapter(adapter);

        et_report_title       = view.findViewById(R.id.et_report_title);
        et_report_subtitle    = view.findViewById(R.id.et_report_subtitle);
        tv_report_category    = view.findViewById(R.id.tv_report_category);
        et_report_address     = view.findViewById(R.id.et_report_address);
        et_report_description = view.findViewById(R.id.et_report_description);

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

            btn_dialog_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPhoto();
                }
            });

            btn_dialog_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callCamera();
                }
            });


            dialog = builder.create();
            dialog.show();

        }

    }


    public void addPhoto(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            getActivity().startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }


    public void callCamera(){
        Snackbar.make(getView(), "SEND NUDES!", Snackbar.LENGTH_LONG).show();
    }


}
