package com.example.matteo.trovatutto.models;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.matteo.trovatutto.R;
import com.example.matteo.trovatutto.ReportActivity;

import java.util.List;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    private Context mContext;
    private List<Segnalazione> reportList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,subtitle;
        public ImageView thumbnail, overflow;


        public MyViewHolder(View view) {
            super(view);
            title       = (TextView)  view.findViewById(R.id.tv_report_title);
            subtitle    = (TextView)  view.findViewById(R.id.tv_report_subtitle);
            thumbnail   = (ImageView) view.findViewById(R.id.iv_report_thumbnail);
            overflow    = (ImageView) view.findViewById(R.id.overflow);

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openReport = new Intent(view.getContext(), ReportActivity.class);
                    view.getContext().startActivity(openReport);
                }
            });
        }
    }


    public ReportAdapter(Context mContext, List<Segnalazione> reportList) {
        this.mContext = mContext;
        this.reportList = reportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Segnalazione report = reportList.get(position);
        holder.title.setText(report.getTitolo());
        holder.subtitle.setText(report.getSottotitolo());

        // loading report images using Glide library
        Glide.with(mContext).load(report.getFoto()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showPopupMenu(holder.overflow);
            }
        });
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
        return reportList.size();
    }
}