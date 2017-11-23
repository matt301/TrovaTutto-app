package com.example.matteo.trovatutto.models;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.matteo.trovatutto.R;
import com.example.matteo.trovatutto.ReportActivity;

import java.util.ArrayList;



public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<Segnalazione> reportList;
    private ArrayList<String> info = new ArrayList<String>();
    private ArrayList<Segnalazione> reportFilteredList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,subtitle;
        public ImageView thumbnail, overflow;


        public MyViewHolder(View view) {
            super(view);
            title       = (TextView)  view.findViewById(R.id.tv_report_title);
            subtitle    = (TextView)  view.findViewById(R.id.tv_report_subtitle);
            thumbnail   = (ImageView) view.findViewById(R.id.iv_report_thumbnail);
            overflow    = (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public ReportAdapter(Context mContext, ArrayList<Segnalazione> reportList) {
        this.mContext = mContext;
        this.reportList = reportList;
        this.reportFilteredList = reportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Segnalazione report = reportFilteredList.get(position);
        holder.title.setText(report.getTitolo());
        holder.subtitle.setText(report.getSottotitolo());


        // loading report images using Glide library
        Glide.with(mContext).load(report.getFoto()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                info.clear();

                info.add(report.getTitolo());
                info.add(report.getSottotitolo());
                info.add(report.getDescrizione());
                info.add(report.getIndirizzo());
                info.add(report.getAutore());
                Log.e("info ",info.toString());


                Intent openReport = new Intent(view.getContext(), ReportActivity.class);
                openReport.putExtra("immagine",report.getFoto());
                openReport.putExtra("info",info);
                view.getContext().startActivity(openReport);

            }
        });


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                info.clear();
                info.add(report.getTitolo());
                info.add(report.getSottotitolo());
                info.add(report.getDescrizione());
                info.add(report.getIndirizzo());
                info.add(report.getAutore());
                Log.e("info ",info.toString());

                Intent openReport = new Intent(view.getContext(), ReportActivity.class);
                openReport.putExtra("immagine",report.getFoto());
                openReport.putStringArrayListExtra("info",info);
                view.getContext().startActivity(openReport);

            }
        });
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                Log.e("tettinesode",charString);

                if (charString.isEmpty()) {

                    reportFilteredList = reportList;

                } else {

                   ArrayList<Segnalazione> filteredList = new ArrayList<>() ;


                    for (Segnalazione report : reportList) {

                        if (report.getTitolo().toLowerCase().contains(charString) || report.getSottotitolo().toLowerCase().contains(charString) || report.getCategoria().toLowerCase().contains(charString)) {

                            filteredList.add(report);
                        }
                    }

                    reportFilteredList = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = reportFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                reportFilteredList = (ArrayList<Segnalazione>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    /**
     * Showing popup menu when tapping on 3 dots
     */
   /* private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    */
    /**
     * Click listener for popup menu items
     */
    /*
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    */
    @Override
    public int getItemCount() {
        return reportFilteredList.size();
    }
}