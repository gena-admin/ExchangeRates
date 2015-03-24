package com.example.android.exchangerates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gena on 23.03.15.
 */
public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("index")) {
            String forecastStr = intent.getStringExtra("index");
            ((TextView) rootView.findViewById(R.id.detail_text))
                    .setText(forecastStr);
        }

        return rootView;
    }

}
