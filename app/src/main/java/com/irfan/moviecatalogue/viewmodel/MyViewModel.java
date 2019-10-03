package com.irfan.moviecatalogue.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.widget.Toast;

import com.irfan.moviecatalogue.BuildConfig;
import com.irfan.moviecatalogue.retrofitservice.RetrofitService;
import com.irfan.moviecatalogue.api.ApiMovie;
import com.irfan.moviecatalogue.model.Movie;
import com.irfan.moviecatalogue.model.MovieResponse;
import com.irfan.moviecatalogue.room.DBRoom;
import com.irfan.moviecatalogue.model.TVShow;
import com.irfan.moviecatalogue.model.TVShowResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyViewModel extends ViewModel {
    public static final String API_KEY = BuildConfig.TMDB_API_KEY;
    public static String LANGUAGE = "en-US";

    private MutableLiveData<List<Movie>> listMovies = new MutableLiveData<>();
    private MutableLiveData<List<TVShow>> listTvs = new MutableLiveData<>();

    public LiveData<List<TVShow>> getTvs(Context context) {
        loadTvs(context);
        return listTvs;
    }

    public LiveData<List<Movie>> getMovies(Context context) {

        loadMovies(context);

        return listMovies;
    }

    public LiveData<List<Movie>> getSearchMovies(String newText) {
        searchMovie(newText);
        return listMovies;
    }

    public LiveData<List<TVShow>> getSearchTv(String newText) {
        searchTv(newText);
        return listTvs;
    }

    private void loadMovies(final Context context) {
        if (Locale.getDefault().getLanguage().equals(new Locale("id").getLanguage())) {
            LANGUAGE = "id-ID";
        } else {
            LANGUAGE = "en-US";
        }

        final ApiMovie apiInterface = RetrofitService.createService(ApiMovie.class);
        Call<MovieResponse> call = apiInterface.getTopRatedMovies(API_KEY,LANGUAGE);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                listMovies.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public LiveData<List<Movie>> getFavList(Context context) {
        MutableLiveData<List<Movie>> movieData = new MutableLiveData<>();
        List<Movie> test;
        test = DBRoom.getInstance(context.getApplicationContext()).movieDao().getFavouriteMovieList();
        movieData.setValue(test);
        return movieData;
    }

    public LiveData<List<TVShow>> getFavListTv(Context context) {
        MutableLiveData<List<TVShow>> tvData = new MutableLiveData<>();
        List<TVShow> test;
        test = DBRoom.getInstance(context.getApplicationContext()).tvDao().getFavouriteTvList();
        tvData.setValue(test);
        return tvData;
    }



    private void loadTvs(final Context context) {
        if (Locale.getDefault().getLanguage().equals(new Locale("id").getLanguage())) {
            LANGUAGE = "id-ID";
        } else {
            LANGUAGE = "en-US";
        }
        ApiMovie apiInterface = RetrofitService.createService(ApiMovie.class);
        Call<TVShowResponse> call = apiInterface.getTopRatedTvShow(API_KEY,LANGUAGE);
        call.enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                listTvs.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<TVShowResponse> call, Throwable t) {
            }
        });
    }

    private void searchMovie(final String newText) {

        final ApiMovie apiInterface = RetrofitService.createService(ApiMovie.class);

        Call<MovieResponse> call = apiInterface.getListSearchMovie(newText, API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                listMovies.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    private void searchTv(final String newText) {

        ApiMovie apiInterface = RetrofitService.createService(ApiMovie.class);
        Call<TVShowResponse> call = apiInterface.getListSearchTvs(newText, API_KEY);
        call.enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                listTvs.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<TVShowResponse> call, Throwable t) {
            }
        });
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


}
