package com.irfan.moviecatalogue.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Response
public class MovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }




}


