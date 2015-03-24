package com.example.android.exchangerates;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BanksFragment extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;
    private BanksAdapter mBanksAdapter;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
//    private static final String ARG_SECTION_NUMBER = "section_number";
//
//    /**
//     * Returns a new instance of this fragment for the given section
//     * number.
//     */
//    public static BanksFragment newInstance(int sectionNumber) {
//        BanksFragment fragment = new BanksFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public BanksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> forecastList = new ArrayList<String>() {{
            add("Today - Sunny- 88/63");
            add("Tomorrow - Foggy - 70/46");
            add("Weds - Claudy - 72/63 ");
            add("Thurs - Rainy - 64/51 ");
            add("Fri - Foggy - 70/46 ");
            add("Sat - Sanny - 76/68 ");
        }};

//        mBanksAdapter = new BanksAdapter(getActivity(), null, 0);


        mForecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_bank,
                R.id.list_item_bank_textview,
                forecastList);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_banks);
//        listView.setAdapter(mForecastAdapter);

        listView.setAdapter(mForecastAdapter);


        new FetchBanksTask(getActivity(), mForecastAdapter).execute("94043");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
//                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
//                if (cursor != null) {
//                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("index", Integer.toString(position));
                    startActivity(intent);
//                }
            }
        });

        return rootView;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(ARG_SECTION_NUMBER));
//    }




}