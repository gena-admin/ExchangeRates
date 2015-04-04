package com.example.android.exchangerates;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.exchangerates.data.BanksContract;


/**
 * Created by gena on 23.03.15.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final String EXCHANGERATES_SHARE_HASHTAG = " #ExchangeRatesApp: I like the bank";
    private static final int DETAIL_LOADER = 0;
    private final String BANK_ADDRESS = "bank_address";
    static final String DETAIL_URI = "URI";
    static String mbankName = "";
    private Uri mUri;
    private ImageButton mMapButton;
    private RatesAdapter mRatesAdapter;

    private static final String[] BANKS_COLUMNS = {
            BanksContract.BankEntry.TABLE_NAME + "." + BanksContract.BankEntry._ID,
            BanksContract.BankEntry.COLUMN_BANK_NAME,
            BanksContract.BankEntry.COLUMN_BANK_OLD_ID,
            BanksContract.RateEntry.COLUMN_CURRENCY,
            BanksContract.RateEntry.COLUMN_ASK,
            BanksContract.RateEntry.COLUMN_BID,
            BanksContract.BankEntry.COLUMN_BANK_ADDRESS
    };

    static final int COL_BANK_NAME = 1;
    static final int COL_BANK_OLD_ID = 2;
    static final int COL_RATE_CURRENCY = 3;
    static final int COL_RATE_ASK = 4;
    static final int COL_RATE_BID = 5;
    static final int COL_BANK_ADDRESS = 6;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mRatesAdapter = new RatesAdapter(getActivity(), null, 0);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_rates);
        listView.setAdapter(mRatesAdapter);

        mMapButton = (ImageButton) rootView.findViewById(R.id.mapButton);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = getActivity().getSharedPreferences(BANK_ADDRESS, getActivity().MODE_PRIVATE);
                String bank_name = prefs.getString(BANK_ADDRESS, null);
                Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                        .appendQueryParameter("q", bank_name)
                        .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(TAG, "Couldn't call no receiving apps installed!");
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, EXCHANGERATES_SHARE_HASHTAG + ' ' + mbankName);
        return shareIntent;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    BANKS_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(TAG, "In onLoadFinished");
        if (!cursor.moveToFirst()) {
            return;
        }
        mRatesAdapter.swapCursor(cursor);
        String bankName = cursor.getString(COL_BANK_NAME);
        String bankAddress = cursor.getString(COL_BANK_ADDRESS);
        mbankName = bankName;
        SharedPreferences.Editor sharedPrefs = getActivity().getSharedPreferences(BANK_ADDRESS, getActivity().MODE_PRIVATE).edit();
        sharedPrefs.putString(BANK_ADDRESS, bankAddress);
        sharedPrefs.commit();
        TextView bankNameTextView = (TextView) getView().findViewById(R.id.bank_name);
        bankNameTextView.setText(bankName);
        ImageView bankIconView = (ImageView) getView().findViewById(R.id.bank_icon);
        bankIconView.setImageResource(Utility.getIconForBank(getActivity(), cursor.getInt(DetailFragment.COL_BANK_OLD_ID), true));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mRatesAdapter.swapCursor(null);
    }
}
