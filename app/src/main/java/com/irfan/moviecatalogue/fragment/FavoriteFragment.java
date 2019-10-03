package com.irfan.moviecatalogue.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.irfan.moviecatalogue.R;
import com.irfan.moviecatalogue.adapter.FavoritePagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.content_main, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        FavoritePagerAdapter tabpagerAdapter = new FavoritePagerAdapter(getContext(), getChildFragmentManager());
        viewPager.setAdapter(tabpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
