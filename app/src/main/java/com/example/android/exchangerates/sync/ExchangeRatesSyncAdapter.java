package com.example.android.exchangerates.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.android.exchangerates.R;
import com.example.android.exchangerates.Utility;
import com.example.android.exchangerates.data.BanksContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class ExchangeRatesSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = ExchangeRatesSyncAdapter.class.getSimpleName();
    private final String BANKS_BASE_URL = "http://resources.finance.ua/ua/public/currency-cash.json";
    private final String UPDATE_DATE = "update_date";

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    public ExchangeRatesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }


    private void getBanksDataFromJson(String banksJsonStr)
            throws JSONException {
        final String ORG_LIST = "organizations";
        final String CURRENCY_LIST = "currencies";
        final String CITY_LIST = "cities";
        final String BANK_TITLE = "title";
        final String BANK_OLD_ID = "oldId";
        final String BANK_CITY_ID = "cityId";
        final String DATE = "date";

        final String BANK_ADDRESS = "address";
        final String RATE_ASK = "ask";
        final String RATE_BID = "bid";

        Date updateDate = new Date();


        JSONObject banksJson = new JSONObject(banksJsonStr);
        JSONArray banksArray = banksJson.getJSONArray(ORG_LIST);
        JSONObject citiesJson = banksJson.getJSONObject(CITY_LIST);
        String strDate = banksJson.getString(DATE);


        Vector<ContentValues> banksVector = new Vector<ContentValues>(banksArray.length());

        Vector<ContentValues> rateVector = new Vector<ContentValues>();

        for (int i = 0; i < banksArray.length(); i++) {

            JSONObject oneBank = banksArray.getJSONObject(i);
            JSONObject rates = oneBank.getJSONObject(CURRENCY_LIST);

            Iterator<?> keys = rates.keys();
            while (keys.hasNext()) {
                ContentValues rateValues = new ContentValues();
                String key = (String) keys.next();
                Object obj = rates.get(key);
                if (obj instanceof JSONObject) {
                    rateValues.put(BanksContract.RateEntry.COLUMN_BANK_KEY, oneBank.getString(BANK_OLD_ID));
                    rateValues.put(BanksContract.RateEntry.COLUMN_CURRENCY, key.toString());
                    rateValues.put(BanksContract.RateEntry.COLUMN_ASK, ((JSONObject) obj).getString(RATE_ASK));
                    rateValues.put(BanksContract.RateEntry.COLUMN_BID, ((JSONObject) obj).getString(RATE_BID));
                }
                rateVector.add(rateValues);
            }

            String updateStrDate;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            try {
                updateDate = dateFormat.parse(strDate);
                updateStrDate = updateDate.toLocaleString();
            } catch (ParseException e) {
                e.printStackTrace();
                updateDate = new Date();
                updateStrDate = dateFormat.format(updateDate);
            }

            SharedPreferences.Editor sharedPrefs = getContext().getSharedPreferences(UPDATE_DATE, getContext().MODE_PRIVATE).edit();
            sharedPrefs.putString(UPDATE_DATE, updateStrDate);
            sharedPrefs.commit();

            ContentValues banksValues = new ContentValues();
            banksValues.put(BanksContract.BankEntry.COLUMN_DATE, updateDate.getTime());
            banksValues.put(BanksContract.BankEntry.COLUMN_BANK_NAME, oneBank.getString(BANK_TITLE));
            banksValues.put(BanksContract.BankEntry.COLUMN_BANK_OLD_ID, oneBank.getString(BANK_OLD_ID));
            String city = citiesJson.getString(oneBank.getString(BANK_CITY_ID));
            banksValues.put(BanksContract.BankEntry.COLUMN_BANK_ADDRESS, Utility.getFullAddress(city, oneBank.getString(BANK_ADDRESS)));
            banksVector.add(banksValues);
        }


        int insertedRates = 0;
        int insertedBanks = 0;
        if (banksVector.size() > 0) {
            ContentValues[] bankArray = new ContentValues[banksVector.size()];
            ContentValues[] rateArray = new ContentValues[rateVector.size()];
            banksVector.toArray(bankArray);
            rateVector.toArray(rateArray);
            insertedBanks = getContext().getContentResolver().bulkInsert(BanksContract.BankEntry.CONTENT_URI, bankArray);
            insertedRates = getContext().getContentResolver().bulkInsert(BanksContract.RateEntry.CONTENT_URI, rateArray);
        }

        Log.d(LOG_TAG, "ExchangeRatesAdapter Complete. " + insertedBanks + " Banks Inserted");
        Log.d(LOG_TAG, "ExchangeRatesAdapter Complete. " + insertedRates + " Rates Inserted");


        int deletedBanks = 0;
        deletedBanks = getContext().getContentResolver().delete(BanksContract.BankEntry.CONTENT_URI,
                 BanksContract.BankEntry.COLUMN_DATE + " < ?",
                 new String[] {Long.toString(updateDate.getTime())});

        Log.d(LOG_TAG, "ExchangeRatesAdapter Complete. " + deletedBanks + " Banks Deleted");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String banksJsonStr = null;
        try {
            URL url = new URL(BANKS_BASE_URL);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                banksJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                banksJsonStr = null;
            }
            banksJsonStr = buffer.toString();


        } catch (IOException e) {
            banksJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }

        try {
            Log.i(LOG_TAG, banksJsonStr);
            getBanksDataFromJson(banksJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return;

    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if (null == accountManager.getPassword(newAccount)) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {

        ExchangeRatesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}