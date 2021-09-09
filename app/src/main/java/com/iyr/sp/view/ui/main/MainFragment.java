package com.iyr.sp.view.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iyr.sp.R;
import com.iyr.sp.view.ui.main.adapters.RoundedInterestsAdapter;

import org.jetbrains.annotations.NotNull;

class MainFragment extends Fragment
{
    private RecyclerView recyclerInterests;
    private RoundedInterestsAdapter adapterMyInterests;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View layout =
                inflater.inflate(R.layout.fragment_main, container, false);

        recyclerInterests = layout.findViewById(R.id.recycler_interests);
                
    setupUI()   ;
        
        return layout; 
    }

    private void setupUI() {

        adapterMyInterests = new RoundedInterestsAdapter();
        recyclerInterests.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerInterests.setAdapter(adapterMyInterests);

    }


}
