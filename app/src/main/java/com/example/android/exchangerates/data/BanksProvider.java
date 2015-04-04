package com.example.android.exchangerates.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
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
    private static final SQLiteQueryBuilder sQueryBuilder;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final int BANK = 100;
    static final int RATE = 200;
    static final int BANK_WITH_RATE = 300;

    static {
        sQueryBuilder = new SQLiteQueryBuilder();

        sQueryBuilder.setTables(
                BanksContract.BankEntry.TABLE_NAME + " INNER JOIN " +
                        BanksContract.RateEntry.TABLE_NAME +
                        " ON " + BanksContract.BankEntry.TABLE_NAME +
                        "." + BanksContract.BankEntry.COLUMN_BANK_OLD_ID +
                        " = " + BanksContract.RateEntry.TABLE_NAME +
                        "." + BanksContract.RateEntry.COLUMN_BANK_KEY);
    }

    //bank.bank_old_id = ?
    private static final String sBankSelection =
            BanksContract.BankEntry.TABLE_NAME +
                    "." + BanksContract.BankEntry.COLUMN_BANK_OLD_ID + " = ? ";

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BanksContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BanksContract.PATH_BANK, BANK);
        matcher.addURI(authority, BanksContract.PATH_BANK + "/*", BANK_WITH_RATE);
        matcher.addURI(authority, BanksContract.PATH_RATE, RATE);
        return matcher;
    }

    private Cursor getBankWithRatesByBankOldId(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = BanksContract.BankEntry.getBankFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sBankSelection;
        selectionArgs = new String[]{locationSetting};

        return sQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case BANK:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BanksContract.BankEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BANK_WITH_RATE:
                retCursor = getBankWithRatesByBankOldId(uri, projection, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Log.d("query", uri.toString());

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.i("onCreate", "OnCreate");
        mOpenHelper = new BanksDbHelper(getContext());
        return true;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case BANK:
                rowsDeleted = db.delete(
                        BanksContract.BankEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String tableName = "";
        switch (match) {
            case BANK:
                tableName = BanksContract.BankEntry.TABLE_NAME;
                break;
            case RATE:
                tableName = BanksContract.RateEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
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
    }
}