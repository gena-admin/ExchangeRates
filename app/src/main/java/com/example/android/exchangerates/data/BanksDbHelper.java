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

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "banks.db";

    public BanksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_BANK_TABLE = "CREATE TABLE " + BankEntry.TABLE_NAME + " (" +
                BankEntry._ID + " INTEGER PRIMARY KEY, " +
                BankEntry.COLUMN_BANK_OLD_ID + " INTEGER NOT NULL, " +
                BankEntry.COLUMN_BANK_NAME + " TEXT NOT NULL, " +
                BankEntry.COLUMN_BANK_ADDRESS + " TEXT NOT NULL, " +
                BankEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                " UNIQUE (" + BankEntry.COLUMN_BANK_OLD_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_RATE_TABLE = "CREATE TABLE " + RateEntry.TABLE_NAME + " (" +
                RateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RateEntry.COLUMN_BANK_KEY + " INTEGER NOT NULL, " +
                RateEntry.COLUMN_CURRENCY + " TEXT NOT NULL, " +
                RateEntry.COLUMN_ASK + " REAL NOT NULL, " +
                RateEntry.COLUMN_BID + " REAL NOT NULL, " +
                " FOREIGN KEY (" + RateEntry.COLUMN_BANK_KEY + ") REFERENCES " +
                BankEntry.TABLE_NAME + " (" + BankEntry.COLUMN_BANK_OLD_ID + "), " +
                " UNIQUE (" + RateEntry.COLUMN_BANK_KEY + ", " +
                RateEntry.COLUMN_CURRENCY + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_BANK_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BankEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RateEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

