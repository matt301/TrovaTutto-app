package com.example.matteo.trovatutto;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matteo.trovatutto.models.ReportAdapter;
import com.example.matteo.trovatutto.models.Segnalazione;
import com.example.matteo.trovatutto.models.ServerRequest;
import com.example.matteo.trovatutto.models.ServerResponse;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallbacks, OnConnectionFailedListener {

    boolean viewIsHome = true;
    private SharedPreferences pref;
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private ArrayList<Segnalazione> reportList;
    private ArrayList<Segnalazione> segnalazioni;

    private FloatingActionButton update;
    private Animation rotate_360;
    private ProgressDialog progressUpdate;
    private MenuItem search;
    private float radius;
    private Geocoder geoCoder;
    private Boolean showAll;



    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private List<Address> address_geo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.home_drawer_home);

        geoCoder = new Geocoder(HomeActivity.this, Locale.getDefault());
        pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);



        if( showAll = (preferences.getBoolean("ch_radius_preference",false)))
            radius=40000000;
        else
            radius = (preferences.getInt("seekbar_preference",0))*1000;

        //showAll = (preferences.getBoolean("ch_radius_preference",false));


        Log.e("radius",String.valueOf(radius));


        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();

        }


        progressUpdate = new ProgressDialog(this);

        update = (FloatingActionButton) findViewById(R.id.fab_update);
        rotate_360 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_360);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reportList = new ArrayList<>();
                segnalazioni = new ArrayList<>();
                update.startAnimation(rotate_360);
                adapter = new ReportAdapter(HomeActivity.this, reportList);
                recyclerView.setAdapter(adapter);
                new DownloadReports().execute();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        new ShowReports().execute();

                    }
                }, 500);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView txtProfileName = navigationView.getHeaderView(MODE_PRIVATE).findViewById(R.id.nome1);
        txtProfileName.setText((pref.getString(Constants.NAME, "") + " " + (pref.getString(Constants.SURNAME, ""))));
        TextView txtEmail = navigationView.getHeaderView(MODE_PRIVATE).findViewById(R.id.tv_email);
        txtEmail.setText(pref.getString(Constants.EMAIL, ""));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        reportList = new ArrayList<>();
        segnalazioni = new ArrayList<>();

        //adapter = new ReportAdapter(this, reportList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);  // spanCount = numero di cards per riga
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);


    }


    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.device_supp, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();

        }

        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(viewIsHome){

                reportList = new ArrayList<>();
                adapter = new ReportAdapter(this, reportList);
                recyclerView.setAdapter(adapter);
            new DownloadReports().execute();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    new ShowReports().execute();

                }
            }, 500);
        }

        checkPlayServices();

    }

    @Override
    public void onBackPressed() {

        if(viewIsHome) {

            if (doubleBackToExitPressedOnce) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                super.onBackPressed();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.back_again, Toast.LENGTH_SHORT).show();


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;

                }
            }, 2000);


        } else{
            displayView(R.id.nav_home);
        }
    }


    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        //displayLocation();
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    /**
     * AsyncTask per mostrare segnalazioni
     */
    private class ShowReports extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {

            progressUpdate.setCancelable(false);
            progressUpdate.setMessage(getString(R.string.loading_reports));
            progressUpdate.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressUpdate.setProgress(0);
            progressUpdate.setMax(100);
            progressUpdate.show();


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressUpdate.setProgress(progress[0]);
        }


        @Override
        protected Void doInBackground(Void... params) {
            chooseReports();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressUpdate.dismiss();
            adapter.notifyDataSetChanged();
            Snackbar.make(findViewById(R.id.drawer_layout), R.string.reports_updated, Snackbar.LENGTH_LONG).show();

        }

        private void chooseReports() {

            for (int i = 0; i < segnalazioni.size(); i++) {
                Log.e("Sengalazione",String.valueOf(segnalazioni.get(i).getIndirizzo()));
                if (distance(segnalazioni.get(i).getIndirizzo()) <= radius) {
                    reportList.add(segnalazioni.get(i));
                    Log.e("s", segnalazioni.get(i).toString());
                }

            }

        }
    }

    @SuppressLint("MissingPermission")
    private Float distance(String indirizzo){

        float[] result = new float[1];


        try {
            address_geo = geoCoder.getFromLocationName(indirizzo, 1);

        } catch (IOException e) {

            e.printStackTrace();
        }

        if(address_geo.size() != 0 && mLastLocation != null ) {
            Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(), address_geo.get(0).getLatitude(),address_geo.get(0).getLongitude(), result);

        }
        Log.e("distanza", String.valueOf(result[0]));
        return result[0];
    }


    /**
     * AsyncTask per download segnalazioni
     */
    private class DownloadReports extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {


        }
        @Override
        protected void onProgressUpdate(Integer...progress) {

        }


        @Override
        protected Void doInBackground(Void...params) {
            prepareReports();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }

        private void prepareReports() {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            ServerRequest request = new ServerRequest();
            request.setOperation(Constants.DOWNLOAD_REPORTS);
            Call<ServerResponse> response = requestInterface.operation(request);

            response.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                    ServerResponse resp = response.body();

                    if(resp.getResult().equals(Constants.SUCCESS)){


                        for(int i = 0; i < resp.getSegnalazioni().size(); i++){

                            Segnalazione segnalazione = new Segnalazione();
                            segnalazione.setID(resp.getSegnalazioni().get(i).getID());
                            segnalazione.setAutore(resp.getSegnalazioni().get(i).getAutore());
                            segnalazione.setTitolo(resp.getSegnalazioni().get(i).getTitolo());
                            segnalazione.setSottotitolo(resp.getSegnalazioni().get(i).getSottotitolo());
                            segnalazione.setCategoria(resp.getSegnalazioni().get(i).getCategoria());
                            segnalazione.setDescrizione(resp.getSegnalazioni().get(i).getDescrizione());
                            segnalazione.setIndirizzo(resp.getSegnalazioni().get(i).getIndirizzo());
                            segnalazione.setFoto("https://webdev.dibris.unige.it/~S4094311/TROVATUTTO/img/img-segnalazioni/"+resp.getSegnalazioni().get(i).getFoto());


                            segnalazioni.add(segnalazione);
                           /* if(distance(segnalazione.getIndirizzo())< radius)
                                reportList.add(segnalazione);*/

                        }
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    Snackbar.make(findViewById(R.id.drawer_layout),t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

                }
            });
        }
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    boolean doubleBackToExitPressedOnce = false;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        search = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.search) {
        //}

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //  int id = item.getItemId();

        displayView(item.getItemId());
        return true;

    }


    //--nuova
    public void displayView(int viewId) {

        Fragment fragment;
        String title;

        switch (viewId) {
            case R.id.nav_home:
                fragment =null;
                title =getString(R.string.home_drawer_home);
                recyclerView.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                search.setVisible(true);
                viewIsHome = true;
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                break;
            case R.id.nav_profilo:
                fragment=new ProfileFragment();
                title = getString(R.string.home_drawer_profile);
                update.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                search.setVisible(false);
                viewIsHome= false;
                break;
            case R.id.nav_mie_segnalazioni:
                fragment = new MyReportFragment();
                title = getString(R.string.home_drawer_show_my_rep);
                update.setVisibility(View.INVISIBLE);
                search.setVisible(false);
                viewIsHome= false;
                break;
            case R.id.nav_add_segnalazioni:
                fragment = new NewReportFragment();
                title = getString(R.string.home_drawer_send_rep);
                update.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                search.setVisible(false);
                viewIsHome= false;
                break;
            case R.id.nav_settings:
                fragment=null;
                title = getString(R.string.home_drawer_setting);
                update.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                search.setVisible(false);
                viewIsHome= false;
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;

            case R.id.nav_logout:
                fragment = null;
                title = "";
                viewIsHome= false;
                logout();
                break;


            default:
                fragment =null;
                title = getString(R.string.home_drawer_home);
                recyclerView.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                search.setVisible(true);
                viewIsHome = true;
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }


    }

    private void logout() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.NAME,"");
        editor.clear();
        editor.apply();

        this.finish();
        goToLogin();
    }

    private void goToLogin(){


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



}
