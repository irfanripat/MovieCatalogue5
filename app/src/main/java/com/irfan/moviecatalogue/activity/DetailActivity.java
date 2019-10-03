package com.irfan.moviecatalogue.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.irfan.moviecatalogue.R;
import com.irfan.moviecatalogue.model.Movie;
import com.irfan.moviecatalogue.room.DBRoom;
import com.irfan.moviecatalogue.model.TVShow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//Activity to show detail of the movie
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_data";
    private TextView txtProgress;
    private ProgressBar progressBar, progressBarDetail;
    private int pStatus = 0;
    private TextView tvTitle, tvRelease, tvOverview;
    private ImageView imgPoster, imgCover;
    private boolean isMovie;
    Movie movie;
    TVShow tvShow;
    private Menu menu;
    private boolean favoriteChecker = false;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        tvTitle = findViewById(R.id.tv_title);
        tvRelease = findViewById(R.id.tv_release);
        tvOverview = findViewById(R.id.tv_desc);
        imgCover = findViewById(R.id.img_Photo2);
        imgPoster = findViewById(R.id.img_photo);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarDetail = (ProgressBar) findViewById(R.id.progress_bar_detail);
        progressBarDetail.setVisibility(View.VISIBLE);

        isMovie = getIntent().getBooleanExtra("isMovie", true);
        if (isMovie) {
            showMovie();
        } else {
            showTV();
        }
        getFavorite();


    }

    private void showMovie() {
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        String cover = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        String poster = "https://image.tmdb.org/t/p/original" + movie.getBackdropPath();
        String overview = movie.getOverview();
        String title = movie.getTitle();
        String release = movie.getReleaseDate();
        double score = movie.getVoteAverage();
        int rate = (int)(score * 10);

        Glide.with(this)
                .load(cover)
                .apply(new RequestOptions().override(300, 300))
                .into(imgCover);
        Glide.with(this)
                .load(poster)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBarDetail.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBarDetail.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgPoster);
        tvTitle.setText(title);
        tvOverview.setText(overview);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            Date date = parser.parse(release);
            SimpleDateFormat formatter= new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formatedDate=formatter.format(date);
            tvRelease.setText(formatedDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        progressBar.setProgress(rate);
        txtProgress.setText(rate + " %");


    }

    private void addToOrRemoveFromFav() {
        if (favoriteChecker) {
            if (isMovie) {
                DBRoom.getInstance(this).movieDao().delMovie(movie);
                title = movie.getTitle();
            } else {
                DBRoom.getInstance(this).tvDao().delTv(tvShow);
                title = tvShow.getName();
            }
            Toast.makeText(getApplicationContext(), "Removed " + title + " from favorite", Toast.LENGTH_SHORT).show();

        } else {
            if (isMovie) {
                DBRoom.getInstance(this).movieDao().addMovie(movie);
                title = movie.getTitle();
            } else {
                DBRoom.getInstance(this).tvDao().addTv(tvShow);
                title = tvShow.getName();
            }
            Toast.makeText(getApplicationContext(), "Added " + title + " to favorite", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeImage() {
        if (favoriteChecker)
            menu.getItem(0).setIcon(R.drawable.ic_favorite_black_24dp);
        else menu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
    }

    private void showTV() {
        tvShow = getIntent().getParcelableExtra(EXTRA_MOVIE);
        String cover = "https://image.tmdb.org/t/p/w500" + tvShow.getPosterPath();
        String poster = "https://image.tmdb.org/t/p/original" + tvShow.getBackdropPath();
        String overview = tvShow.getOverview();
        String title = tvShow.getName();
        String release = tvShow.getFirstAirDate();
        double score = tvShow.getVoteAverage();
        int rate = (int)(score * 10);
        Glide.with(this)
                .load(cover)
                .apply(new RequestOptions().override(300, 300))
                .into(imgCover);
        Glide.with(this)
                .load(poster)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBarDetail.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBarDetail.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgPoster);
        tvTitle.setText(title);
        tvOverview.setText(overview);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            Date date = parser.parse(release);
            SimpleDateFormat formatter= new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formatedDate=formatter.format(date);
            tvRelease.setText(formatedDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        progressBar.setProgress(rate);
        txtProgress.setText(rate + " %");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        getMenuInflater().inflate(R.menu.favorite, item);
        menu = item;
        changeImage();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            addToOrRemoveFromFav();
            favoriteChecker = !favoriteChecker;
            changeImage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFavorite() {
        if (isMovie) {
            favoriteChecker = DBRoom.getInstance(this)
                    .movieDao()
                    .findMovieById(movie.getId()) != null;
        } else {
            favoriteChecker = DBRoom.getInstance(this)
                    .tvDao()
                    .findTvById(tvShow.getId()) != null;
        }
    }
}
