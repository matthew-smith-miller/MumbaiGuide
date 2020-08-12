package com.example.mumbaiguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GuideItemFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_OBJECT);
            GuideItemAdapter guideItemAdapter;
            switch (position) {
                case 0:
                    guideItemAdapter = new GuideItemAdapter(
                            getContext(), (MainActivity.getSights()));
                    ((ListView) view.findViewById(R.id.list_view)).setAdapter(guideItemAdapter);
                    break;
                case 1:
                    guideItemAdapter = new GuideItemAdapter(
                            getContext(), (MainActivity.getEatingDrinking()));
                    ((ListView) view.findViewById(R.id.list_view)).setAdapter(guideItemAdapter);
                    break;
                case 2:
                    guideItemAdapter = new GuideItemAdapter(
                            getContext(), (MainActivity.getShopping()));
                    ((ListView) view.findViewById(R.id.list_view)).setAdapter(guideItemAdapter);
                    break;
                case 3:
                    guideItemAdapter = new GuideItemAdapter(
                            getContext(), (MainActivity.getActivities()));
                    ((ListView) view.findViewById(R.id.list_view)).setAdapter(guideItemAdapter);
                    break;
            }
        }
    }
}
