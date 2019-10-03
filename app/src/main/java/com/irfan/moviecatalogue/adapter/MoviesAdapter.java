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
import com.irfan.moviecatalogue.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Adapter Movies
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvTitle.setText(movies.get(viewHolder.getAdapterPosition()).getTitle());
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            Date date = parser.parse(movies.get(viewHolder.getAdapterPosition()).getReleaseDate());
            SimpleDateFormat formatter= new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formatedDate=formatter.format(date);
            viewHolder.tvRelease.setText(formatedDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if (movies.get(viewHolder.getAdapterPosition()).getOverview().length() == 0) {
            movies.get(viewHolder.getAdapterPosition()).setOverview("Maaf belum tersedia dalam bahasa indonesia");
            viewHolder.tvOverview.setText(movies.get(viewHolder.getAdapterPosition()).getOverview());
        } else {
            viewHolder.tvOverview.setText(movies.get(viewHolder.getAdapterPosition()).getOverview());
        }
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w154" + movies.get(viewHolder.getAdapterPosition()).getPosterPath())
                .into(viewHolder.imgPoster);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, DetailActivity.class);
                detailIntent.putExtra(DetailActivity.EXTRA_MOVIE, movies.get(viewHolder.getAdapterPosition()));
                detailIntent.putExtra("isMovie",true);
                context.startActivity(detailIntent);
            }
        });



    }

    @Override
    public int getItemCount() {
            return movies.size();
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
