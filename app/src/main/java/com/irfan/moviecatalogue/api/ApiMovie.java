package com.irfan.moviecatalogue.api;

import com.irfan.moviecatalogue.model.MovieResponse;
import com.irfan.moviecatalogue.model.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiMovie {
    @GET("discover/movie")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey,@Query("language") String language);

    @GET("discover/tv")
    Call<TVShowResponse> getTopRatedTvShow(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("discover/movie")
    Call<MovieResponse> getUpcomingDate(@Query("api_key") String apiKey,@Query("language") String language
            , @Query("primary_release_date.gte") String currentDate
            , @Query("primary_release_date.lte") String currentDate2);

    @GET("search/movie/")
    Call<MovieResponse> getListSearchMovie(@Query("query") String query, @Query("api_key") String apiKey);

    @GET("search/tv")
    Call<TVShowResponse> getListSearchTvs(@Query("query") String query, @Query("api_key") String apiKey);
}

