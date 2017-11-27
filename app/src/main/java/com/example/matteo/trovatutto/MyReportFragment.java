package com.example.matteo.trovatutto;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matteo.trovatutto.models.ReportAdapter;
import com.example.matteo.trovatutto.models.Segnalazione;
import com.example.matteo.trovatutto.models.ServerRequest;
import com.example.matteo.trovatutto.models.ServerResponse;
import com.example.matteo.trovatutto.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class MyReportFragment extends Fragment  implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private ArrayList<Segnalazione> reportList;
    private ArrayList<Segnalazione> myReportList;
    private SharedPreferences pref;
    private View view;
    private FloatingActionButton updateMy;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_report, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        updateMy = (FloatingActionButton) view.findViewById(R.id.my_fab_update);
        updateMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myReportList = new ArrayList<>();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        new DownloadReports().execute();
                    }
                }, 1000);

            }
        });


        myReportList = new ArrayList<>();

        adapter = new ReportAdapter(view.getContext(), myReportList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);  // TODO: spanCount = numero di cards per riga
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        new DownloadReports().execute();


    }



    private class DownloadReports extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void...params) {
            chooseMyReport();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            Snackbar.make(getActivity().findViewById(R.id.my_content_frame), "miei report", Snackbar.LENGTH_LONG).show();

        }

    private void chooseMyReport(){

     //   final List<Segnalazione> tempList = new ArrayList<>();
        User user = new User();
        user.setEmail(pref.getString(Constants.EMAIL,""));



        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);


        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.DOWNLOAD_MY_REPORTS);
        request.setUser(user);
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

                        myReportList.add(segnalazione);

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

    }
    }





    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

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

    @Override
    public void onClick(View v) {

    }






}
