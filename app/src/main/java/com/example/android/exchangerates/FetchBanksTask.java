package com.example.android.exchangerates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import com.example.android.exchangerates.data.BanksContract.BankEntry;
import com.example.android.exchangerates.data.BanksContract.RateEntry;
/**
 * Created by gena on 24.03.15.
 */
public class FetchBanksTask extends AsyncTask<String, Void, String[]> {

    private ArrayAdapter<String> mForecastAdapter;
    private final Context mContext;
    private final String TAG = FetchBanksTask.class.getSimpleName();
    private final String BANKS_BASE_URL = "http://resources.finance.ua/ua/public/currency-cash.json";

//        private final Integer NUM_DAYS = 7;

        /* The date/time conversion code is going to be moved outside the asynctask later,
       * so for convenience we're breaking it out into its own method now.
       */
//        private String getReadableDateString(long time){
//            // Because the API returns a unix timestamp (measured in seconds),
//            // it must be converted to milliseconds in order to be converted to valid date.
//            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
//            return shortenedDateFormat.format(time);
//        }

    /**
     * Prepare the weather high/lows for presentation.
     */
//        private String formatHighLows(double high, double low) {
//            // For presentation, assume the user doesn't care about tenths of a degree.
//            long roundedHigh = Math.round(high);
//            long roundedLow = Math.round(low);
//
//            String highLowStr = roundedHigh + "/" + roundedLow;
//            return highLowStr;
//        }

    public FetchBanksTask(Context context, ArrayAdapter<String> forecastAdapter) {
        mContext = context;
        mForecastAdapter = forecastAdapter;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getBanksDataFromJson(String banksJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String ORG_LIST = "organizations";
        final String BANK_TITLE = "title";
        final String BANK_OLD_ID = "oldId";


//            final String OWM_TEMPERATURE = "temp";
//            final String OWM_MAX = "max";
//            final String OWM_MIN = "min";
//            final String OWM_DESCRIPTION = "main";

        JSONObject banksJson = new JSONObject(banksJsonStr);
        JSONArray banksArray = banksJson.getJSONArray(ORG_LIST);

        Vector<ContentValues> cVVector = new Vector<ContentValues>(banksArray.length());

//            Time dayTime = new Time();
//            dayTime.setToNow();
//
//            // we start at the day returned by local time. Otherwise this is a mess.
//            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
//
//            // now we work exclusively in UTC
//            dayTime = new Time();
//
        String[] resultStrs = new String[banksArray.length()];
        for(int i = 0; i < banksArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject oneBank = banksArray.getJSONObject(i);
//
//                // The date/time is returned as a long.  We need to convert that
//                // into something human-readable, since most people won't read "1400356800" as
//                // "this saturday".
//                long dateTime;
//                // Cheating to convert this to UTC time, which is what we want anyhow
//                dateTime = dayTime.setJulianDay(julianStartDay+i);
//                day = getReadableDateString(dateTime);
//
//                // description is in a child array called "weather", which is 1 element long.
//                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//                description = weatherObject.getString(OWM_DESCRIPTION);
//
//                // Temperatures are in a child object called "temp".  Try not to name variables
//                // "temp" when working with temperature.  It confuses everybody.
//                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//                double high = temperatureObject.getDouble(OWM_MAX);
//                double low = temperatureObject.getDouble(OWM_MIN);
//
//                highAndLow = formatHighLows(high, low);
//                resultStrs[i] = day + " - " + description + " - " + highAndLow;

            ContentValues banksValues = new ContentValues();
            banksValues.put(BankEntry.COLUMN_BANK_NAME,   oneBank.getString(BANK_TITLE));
            banksValues.put(BankEntry.COLUMN_BANK_OLD_ID, oneBank.getString(BANK_OLD_ID));

            resultStrs[i] = oneBank.getString(BANK_TITLE);
            cVVector.add(banksValues);
        }


        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(BankEntry.CONTENT_URI, cvArray);
        }


        Uri banksUri = BankEntry.buildAllBanks();


        Cursor cur = mContext.getContentResolver().query(banksUri, null, null, null, null);

        cVVector = new Vector<ContentValues>(cur.getCount());
        if ( cur.moveToFirst() ) {
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cur, cv);
                cVVector.add(cv);
            } while (cur.moveToNext());
        }

        Log.d(TAG, "FetchWeatherTask Complete. " + cVVector.size() + " Inserted");

        resultStrs = convertContentValuesToUXFormat(cVVector);


        return resultStrs;

    }

    String[] convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
        // return strings to keep UI functional for now
        String[] resultStrs = new String[cvv.size()];
        for ( int i = 0; i < cvv.size(); i++ ) {
            ContentValues bankValues = cvv.elementAt(i);
            resultStrs[i] = bankValues.getAsString(BankEntry.COLUMN_BANK_NAME);
        }
        return resultStrs;
    }


    protected String[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String banksJsonStr = null;
        try {
//                Uri builtUri = Uri.parse(BANKS_BASE_URL).buildUpon()
//                        .appendQueryParameter("q", params[0])
//                        .appendQueryParameter("mode", "json")
//                        .appendQueryParameter("units", "metric")
//                        .appendQueryParameter("cnt", String.valueOf(NUM_DAYS)).build();
            URL url = new URL(BANKS_BASE_URL);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                banksJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                banksJsonStr = null;
            }
            banksJsonStr = buffer.toString();



        } catch (IOException e) {
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            banksJsonStr = null;
        } finally{
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
            Log.i(TAG, banksJsonStr);
            return getBanksDataFromJson(banksJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
    }

    protected void onPostExecute(String[] result) {
        if (result != null && mForecastAdapter != null ) {
            mForecastAdapter.clear();
            for (String dayForecastStr : result) {
                mForecastAdapter.add(dayForecastStr);
            }
        }

    }
}
