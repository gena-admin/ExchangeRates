package com.example.android.exchangerates.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by gena on 24.03.15.
 */
public class BanksContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.exchangerates";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RATE = "rate";
    public static final String PATH_BANK = "bank";

    public static final class BankEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BANK).build();

        public static final String TABLE_NAME = "bank";
        public static final String COLUMN_BANK_NAME = "bank_name";
        public static final String COLUMN_BANK_OLD_ID = "bank_old_id";
        public static final String COLUMN_BANK_ADDRESS = "address";
        public static final String COLUMN_DATE = "date";

        public static Uri buildAllBanks() {
            return CONTENT_URI;
        }

        public static String getBankFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildBankWithRates(String index) {
            return CONTENT_URI.buildUpon().appendPath(index).build();
        }
    }

    public static final class RateEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RATE).build();

        public static final String TABLE_NAME = "rate";
        public static final String COLUMN_BANK_KEY = "bank_id";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_ASK = "ask";
        public static final String COLUMN_BID = "bid";

    }
}
