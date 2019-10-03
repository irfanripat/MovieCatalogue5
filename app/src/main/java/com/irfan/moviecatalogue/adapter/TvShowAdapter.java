package com.irfan.moviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.irfan.moviecatalogue.activity.DetailActivity;
import com.irfan.moviecatalogue.R;
import com.irfan.moviecatalogue.model.TVShow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Adapter TVShow
public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {
    private Context context;
    private List<TVShow> tvData;

    public TvShowAdapter(Context context, List<TVShow> tvData) {
        this.context = context;
        this.tvData = tvData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvTitle.setText(tvData.get(viewHolder.getAdapterPosition()).getName());
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            Date date = parser.parse(tvData.get(viewHolder.getAdapterPosition()).getFirstAirDate());
            SimpleDateFormat formatter= new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formatedDate=formatter.format(date);
            viewHolder.tvRelease.setText(formatedDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if (tvData.get(viewHolder.getAdapterPosition()).getOverview().length() == 0) {
            tvData.get(viewHolder.getAdapterPosition()).setOverview("Maaf belum tersedia dalam bahasa indonesia");
            viewHolder.tvOverview.setText(tvData.get(viewHolder.getAdapterPosition()).getOverview());
        } else {
            viewHolder.tvOverview.setText(tvData.get(viewHolder.getAdapterPosition()).getOverview());
        }
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w154" + tvData.get(viewHolder.getAdapterPosition()).getPosterPath())
                .into(viewHolder.imgPoster);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, DetailActivity.class);
                detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, tvData.get(viewHolder.getAdapterPosition()));
                detailIntent.putExtra("isMovie",false);
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRelease, tvOverview;
        ImageView imgPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_name);
            tvRelease = itemView.findViewById(R.id.tv_release);
            tvOverview = itemView.findViewById(R.id.tv_desc);
            imgPoster = itemView.findViewById(R.id.img_photo);
        }
    }
}
