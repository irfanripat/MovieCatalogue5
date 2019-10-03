package com.irfan.moviecatalogue.fragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.irfan.moviecatalogue.R;
import com.irfan.moviecatalogue.adapter.TvShowAdapter;
import com.irfan.moviecatalogue.model.TVShow;
import com.irfan.moviecatalogue.viewmodel.MyViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MyViewModel myViewModel;
    private TvShowAdapter adapter;
    private ProgressBar progressBar;

    private Observer<List<TVShow>> getTvs = new Observer<List<TVShow>>() {
        @Override
        public void onChanged(@Nullable List<TVShow> tvShows) {
            if (tvShows.size()>0) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new TvShowAdapter(getContext(), tvShows);
                mRecyclerView.setAdapter(adapter);
                showLoading(false);
            }

        }
    };

    private Observer<List<TVShow>> getSearchTv = new Observer<List<TVShow>>() {
        @Override
        public void onChanged(@Nullable List<TVShow> tvShows) {
            if (tvShows.size() > 0) {
                adapter = new TvShowAdapter(getContext(), tvShows);
                mRecyclerView.setAdapter(adapter);
            }
        }
    };

    public TvShowsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tvshow, container, false);
        mRecyclerView = v.findViewById(R.id.rv_tvshow);
        progressBar = v.findViewById(R.id.progress_bar_tvshow);
        showLoading(true);
        return v;
    }
    private void loadTv() {
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getTvs(getActivity()).observe(getActivity(), getTvs);
    }


    //To show progressbar or not
    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void loadSearch(String query) {
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        myViewModel.getSearchTv(query).observe(getActivity(), getSearchTv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadTv();
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    loadSearch(newText);
                }
                return true;
            }
        });
    }
}
