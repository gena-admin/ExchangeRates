package com.example.android.exchangerates.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.android.exchangerates.R;
import com.example.android.exchangerates.fragment.DetailFragment;


/**
 * Created by gena on 23.03.15.
 */
public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rate_detail_container, fragment)
                    .commit();
        }
    }
}
