package com.example.android.exchangerates.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.exchangerates.data.BanksContract.BankEntry;
import com.example.android.exchangerates.data.BanksContract.RateEntry;


/**
 * Created by gena on 24.03.15.
 */
public class BanksDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "banks.db";

    public BanksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_BANK_TABLE = "CREATE TABLE " + BankEntry.TABLE_NAME + " (" +
                BankEntry._ID + " INTEGER PRIMARY KEY, " +
                BankEntry.COLUMN_BANK_NAME + " TEXT NOT NULL, " +
                BankEntry.COLUMN_BANK_OLD_ID + " INTEGER NOT NULL, " +
                " UNIQUE (" + BankEntry.COLUMN_BANK_OLD_ID + ") ON CONFLICT REPLACE);";



        final String SQL_CREATE_RATE_TABLE = "CREATE TABLE " + RateEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                RateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // the ID of the location entry associated with this weather data
                RateEntry.COLUMN_BANK_KEY + " INTEGER NOT NULL, " +
                RateEntry.COLUMN_CURRENCY + " TEXT NOT NULL, " +
                RateEntry.COLUMN_VALUE + " REAL NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + RateEntry.COLUMN_BANK_KEY + ") REFERENCES " +
                BankEntry.TABLE_NAME + " (" + BankEntry._ID + "));";


        sqLiteDatabase.execSQL(SQL_CREATE_BANK_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BankEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RateEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

