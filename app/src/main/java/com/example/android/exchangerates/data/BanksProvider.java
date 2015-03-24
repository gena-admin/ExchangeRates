package com.example.android.exchangerates.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by gena on 24.03.15.
 */
public class BanksProvider extends ContentProvider {

    private BanksDbHelper mOpenHelper;
    private static final SQLiteQueryBuilder sQueryBuilder = null;

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
//        switch (sUriMatcher.match(uri)) {
//            // "weather/*/*"
//            case WEATHER_WITH_LOCATION_AND_DATE:
//            {
//                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
//                break;
//            }
//            // "weather/*"
//            case WEATHER_WITH_LOCATION: {
//                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
//                break;
//            }
//            // "weather"
//            case WEATHER: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        WeatherContract.WeatherEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
//            // "location"
//            case LOCATION: {
//                retCursor = mOpenHelper.getReadableDatabase().query(
//                        WeatherContract.LocationEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
//                break;
//            }
//
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
        Log.i("query","query" );
//        retCursor = sQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                projection,
//                null,
//                null,
//                null,
//                null,
//                sortOrder
//        );

        retCursor = mOpenHelper.getReadableDatabase().query(
                BanksContract.BankEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public String getType(Uri uri) {
        Log.i("getType","getType" );
//        // Use the Uri Matcher to determine what kind of URI this is.
//        final int match = sUriMatcher.match(uri);
//
//        switch (match) {
//            // Student: Uncomment and fill out these two cases
//            case WEATHER_WITH_LOCATION_AND_DATE:
//                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
//            case WEATHER_WITH_LOCATION:
//                return WeatherContract.WeatherEntry.CONTENT_TYPE;
//            case WEATHER:
//                return WeatherContract.WeatherEntry.CONTENT_TYPE;
//            case LOCATION:
//                return WeatherContract.LocationEntry.CONTENT_TYPE;
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
        return "1";
    }

    @Override
    public boolean onCreate() {
        Log.i("onCreate", "OnCreate");
        mOpenHelper = new BanksDbHelper(getContext());
        return true;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i("update","update" );
//        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
//        int rowsUpdated;
//
//        switch (match) {
//            case WEATHER:
//                normalizeDate(values);
//                rowsUpdated = db.update(WeatherContract.WeatherEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
//            case LOCATION:
//                rowsUpdated = db.update(WeatherContract.LocationEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//        if (rowsUpdated != 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
//        return rowsUpdated;
        return 1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i("delete","delete" );
//        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
//        int rowsDeleted;
//        // this makes delete all rows return the number of rows deleted
//        if ( null == selection ) selection = "1";
//        switch (match) {
//            case WEATHER:
//                rowsDeleted = db.delete(
//                        WeatherContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            case LOCATION:
//                rowsDeleted = db.delete(
//                        WeatherContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//        // Because a null deletes all rows
//        if (rowsDeleted != 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
//        return rowsDeleted;
        return 1;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        Log.i("insert","insert" );
//        switch (match) {
//            case WEATHER: {
//                normalizeDate(values);
//                long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, values);
//                if ( _id > 0 )
//                    returnUri = WeatherContract.WeatherEntry.buildWeatherUri(_id);
//                else
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                break;
//            }
//            case LOCATION: {
//                long _id = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);
//                if ( _id > 0 )
//                    returnUri = WeatherContract.LocationEntry.buildLocationUri(_id);
//                else
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                break;
//            }
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
//                normalizeDate(value);
                long _id = db.insert(BanksContract.BankEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

//        final int match = sUriMatcher.match(uri);
//        switch (match) {
//            case WEATHER:
//                db.beginTransaction();
//                int returnCount = 0;
//                try {
//                    for (ContentValues value : values) {
//                        normalizeDate(value);
//                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
//                        if (_id != -1) {
//                            returnCount++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
//            default:
//                return super.bulkInsert(uri, values);
//        }
    }

}
