package com.example.android.exchangerates.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.exchangerates.R;
import com.example.android.exchangerates.adapter.BanksAdapter;
import com.example.android.exchangerates.data.BanksContract;
import com.example.android.exchangerates.data.BanksContract.BankEntry;

public class BanksFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = BanksFragment.class.getSimpleName();
    private BanksAdapter mBanksAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private static final int BANKS_LOADER = 0;
    private final String UPDATE_DATE = "update_date";

    private static final String[] BANK_COLUMNS = {
            BanksContract.BankEntry.TABLE_NAME + "." + BanksContract.BankEntry._ID,
            BankEntry.COLUMN_BANK_NAME,
            BankEntry.COLUMN_BANK_OLD_ID,
    };
    public static final int COL_BANK_OLD_ID = 2;


    public interface Callback {
        public void onItemSelected(Uri dateUri);
    }

    public BanksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(BANKS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(BankEntry
                                    .buildBankWithRates(cursor.getString(COL_BANK_OLD_ID)));
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mBanksAdapter = new BanksAdapter(getActivity(), null, 0);
        mListView = (ListView) rootView.findViewById(R.id.listview_banks);
        mListView.setAdapter(mBanksAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri banksUri = BankEntry.buildAllBanks();

        return new CursorLoader(getActivity(),
                banksUri,
                BANK_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(TAG, "In onLoadFinished BanksFragment");
        mBanksAdapter.swapCursor(cursor);

        SharedPreferences prefs = getActivity().getSharedPreferences(UPDATE_DATE, getActivity().MODE_PRIVATE);
        String updateDate = prefs.getString(UPDATE_DATE, "");

        getActivity().setTitle(updateDate);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
        else {
            if ((getActivity().findViewById(R.id.rate_detail_container) != null) && (cursor.getCount() != 0 )) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setItemChecked(0, true);
                        mListView.performItemClick(mListView.getChildAt(0), 0, mListView.getItemIdAtPosition(0));
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mBanksAdapter.swapCursor(null);
    }

}


