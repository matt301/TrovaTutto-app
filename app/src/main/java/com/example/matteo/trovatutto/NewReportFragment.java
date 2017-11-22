package com.example.matteo.trovatutto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
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

import com.example.matteo.trovatutto.models.Segnalazione;
import com.example.matteo.trovatutto.models.ServerRequest;
import com.example.matteo.trovatutto.models.ServerResponse;
import com.example.matteo.trovatutto.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class NewReportFragment extends Fragment  implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    public static final int GALLERY_INTENT_CALLED = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 12345;

    private AppCompatButton btn_sendreport, btn_insertfoto;
    private Button btn_dialog_gallery, btn_dialog_camera;
    private EditText et_report_title, et_report_subtitle, et_report_address,et_report_description;
    private TextView tv_report_category;
    private Spinner category_spinner;
    private String categoryChoose;
    private ProgressBar progress;
    private AlertDialog dialog;
    private ImageView iv_report;
    private String mCurrentPhotoPath;
    private SharedPreferences userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_report,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        userInfo = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

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
        categoryChoose = category_spinner.getItemAtPosition(pos).toString();
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
                String title = et_report_title.getText().toString();
                String subtitle = et_report_subtitle.getText().toString();
                String description = et_report_description.getText().toString();
                String address = et_report_address.getText().toString();

                if(categoryChoose != null) {
                    String category = categoryChoose;

                    //Codifica immagine
                    if(iv_report.getDrawable() != null) {
                        Bitmap image = ((BitmapDrawable) iv_report.getDrawable()).getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    // fine codifica

                        if (!title.isEmpty() && !subtitle.isEmpty() && !description.isEmpty() && !address.isEmpty()) {


                            progress.setVisibility(View.VISIBLE);
                            sendReportProcess(userInfo.getString(Constants.EMAIL, ""), title, subtitle, category, description, address, encodedImage);

                        } else {

                            Snackbar.make(getView(), "Fields are empty ", Snackbar.LENGTH_LONG).show();
                        }
                    }else{
                        Snackbar.make(getView(), "Immage is empty !", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(getView(), "Category is empty !", Snackbar.LENGTH_LONG).show();
                }
                break;

        }

    }

    public void sendReportProcess(String email,String title,String subtitle, String category, String description, String address, String foto){


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        //Creazione dell'oggetto Segnalazione contenente tutte le info
        Segnalazione report = new Segnalazione();
        report.setAutore(email);
        report.setTitolo(title);
        report.setSottotitolo(subtitle);
        report.setDescrizione(description);
        report.setCategoria(category);
        report.setIndirizzo(address);
        report.setFoto(foto);

        //Invio richiesta di registrazione al server
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.INSERT_NEW_REPORT);
        request.setSegnalazione(report);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                progress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });
    }

    private void showDialog(String tipo){
        if(tipo.equals("INSERT PHOTO")){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_insert_photo, null);

            btn_dialog_gallery =  view.findViewById(R.id.btn_dialog_gallery);
            btn_dialog_camera =  view.findViewById(R.id.btn_dialog_camera);

            builder.setView(view);
            builder.setTitle("From?");
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

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,GALLERY_INTENT_CALLED);

    }


    public void callCamera(){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Fragment frag = this;
        frag.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == GALLERY_INTENT_CALLED) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                iv_report.setImageURI(imageUri);
            } else {
                Snackbar.make(this.getView(), "You haven't picked Image", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
        //TODO:-- L'immagine è una preview bisogna inserire la foto vera -> non è vero
        if (reqCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                iv_report.setImageBitmap(imageBitmap);

            } else {
                Snackbar.make(this.getView(), "You haven't made a photo", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
            }

    }


}
