package com.irfan.moviecatalogue.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Response
public class TVShowResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<TVShow> results;

    @SerializedName("total_results")


    public List<TVShow> getResults() {
        return results;
    }


}
