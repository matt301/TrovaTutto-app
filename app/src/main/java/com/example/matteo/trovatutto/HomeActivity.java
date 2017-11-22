package com.example.matteo.trovatutto;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.res.Resources;
import android.graphics.Rect;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.TypedValue;
import android.widget.Toast;

import com.example.matteo.trovatutto.models.ReportAdapter;
import com.example.matteo.trovatutto.models.Segnalazione;
import com.example.matteo.trovatutto.models.ServerRequest;
import com.example.matteo.trovatutto.models.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.matteo.trovatutto.Constants.TAG;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        boolean viewIsHome = true;
        private SharedPreferences pref;
        private RecyclerView recyclerView;
        private ReportAdapter adapter;
        private List<Segnalazione> reportList;
        private   FloatingActionButton update;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences("userInfo",MODE_PRIVATE);

        update = (FloatingActionButton) findViewById(R.id.fab_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reportList = new ArrayList<>();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        new DownloadReports().execute();
                    }
                }, 1000);

            }
        });

        DrawerLayout  drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView txtProfileName = navigationView.getHeaderView(MODE_PRIVATE).findViewById(R.id.nome1);
        txtProfileName.setText((pref.getString(Constants.NAME, "")+" "+(pref.getString(Constants.SURNAME, ""))));
        TextView txtEmail = navigationView.getHeaderView(MODE_PRIVATE).findViewById(R.id.tv_email);
        txtEmail.setText(pref.getString(Constants.EMAIL, ""));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        reportList = new ArrayList<>();
        adapter = new ReportAdapter(this, reportList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);  // TODO: spanCount = numero di cards per riga
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //prepareReports();// TODO: bindare anche sul bottone "aggiorna"

         new DownloadReports().execute();

        //initFragment(); // TODO: rimuovere HomeFragment


    }


    /**
     * AsyncTask per download segnalazioni
     */
    private class DownloadReports extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void...params) {
            prepareReports();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            Snackbar.make(findViewById(R.id.drawer_layout), "Nuovi report molto belli", Snackbar.LENGTH_LONG).show();

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

                            reportList.add(segnalazione);

                        }


                    }

                    //  Snackbar.make(findViewById(R.id.drawer_layout), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                }


                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                    //progress.setVisibility(View.INVISIBLE);
                    //  Log.d(TAG,"failed");
                    //   Snackbar.make(findViewById(R.id.drawer_layout),t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

                }
            });

            //adapter.notifyDataSetChanged();

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





    private void initFragment(){
        android.app.Fragment fragment;

            fragment = new HomeFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if(viewIsHome) {

            if (doubleBackToExitPressedOnce) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                super.onBackPressed();

            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


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


    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (viewIsHome) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            displayView(R.id.nav_home);

        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        }

        return super.onOptionsItemSelected(item);
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
                fragment = new HomeFragment();
                title = "Home";
                update.setVisibility(View.VISIBLE);
                viewIsHome = true;
                break;
            case R.id.nav_profilo:
                fragment = new ProfileFragment();
                title = "Profile";
                update.setVisibility(View.INVISIBLE);
                viewIsHome= false;
                break;
            case R.id.nav_mie_segnalazioni:
                fragment = new MyReportFragment();
                title = "My Report";
                update.setVisibility(View.INVISIBLE);
                viewIsHome= false;
                break;
            case R.id.nav_add_segnalazioni:
                fragment = new NewReportFragment();
                title = "New Report";
                update.setVisibility(View.INVISIBLE);
                viewIsHome= false;
                break;
            case R.id.nav_logout:
                fragment = null;
                title = "";
                viewIsHome= false;
                logout();
                break;


            default:
                fragment = new HomeFragment();
                title = "Home";
                update.setVisibility(View.VISIBLE);
                viewIsHome = true;
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