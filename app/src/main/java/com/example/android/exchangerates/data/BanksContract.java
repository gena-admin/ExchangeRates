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

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.exchangerates.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_WEATHER = "rate";
    public static final String PATH_LOCATION = "bank";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
//    public static long normalizeDate(long startDate) {
//        // normalize the start date to the beginning of the (UTC) day
//        Time time = new Time();
//        time.set(startDate);
//        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
//        return time.setJulianDay(julianDay);
//    }

    /* Inner class that defines the table contents of the location table */
    public static final class BankEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        // Table name
        public static final String TABLE_NAME = "bank";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
//        public static final String COLUMN_LOCATION_SETTING = "location_setting";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_BANK_NAME = "bank_name";

        public static final String COLUMN_BANK_OLD_ID = "bank_old_id";

        public static Uri buildAllBanks() {
            return CONTENT_URI;
        }

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
//        public static final String COLUMN_COORD_LAT = "coord_lat";
//        public static final String COLUMN_COORD_LONG = "coord_long";

//        public static Uri buildLocationUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class RateEntry implements BaseColumns {

//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static final String TABLE_NAME = "rate";

        // Column with the foreign key into the location table.
        public static final String COLUMN_BANK_KEY = "bank_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_CURRENCY = "currency";

        public static final String COLUMN_VALUE = "value";

//        // Weather id as returned by API, to identify the icon to be used
//        public static final String COLUMN_WEATHER_ID = "weather_id";
//
//        // Short description and long description of the weather, as provided by API.
//        // e.g "clear" vs "sky is clear".
//        public static final String COLUMN_SHORT_DESC = "short_desc";
//
//        // Min and max temperatures for the day (stored as floats)
//        public static final String COLUMN_MIN_TEMP = "min";
//        public static final String COLUMN_MAX_TEMP = "max";
//
//        // Humidity is stored as a float representing percentage
//        public static final String COLUMN_HUMIDITY = "humidity";
//
//        // Humidity is stored as a float representing percentage
//        public static final String COLUMN_PRESSURE = "pressure";
//
//        // Windspeed is stored as a float representing windspeed  mph
//        public static final String COLUMN_WIND_SPEED = "wind";
//
//        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
//        public static final String COLUMN_DEGREES = "degrees";
//
//        public static Uri buildWeatherUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }

        /*
            Student: This is the buildWeatherLocation function you filled in.
         */
//        public static Uri buildWeatherLocation(String locationSetting) {
//            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
//        }
//
//        public static Uri buildWeatherLocationWithStartDate(
//                String locationSetting, long startDate) {
//            long normalizedDate = normalizeDate(startDate);
//            return CONTENT_URI.buildUpon().appendPath(locationSetting)
//                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
//        }
//
//        public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {
//            return CONTENT_URI.buildUpon().appendPath(locationSetting)
//                    .appendPath(Long.toString(normalizeDate(date))).build();
//        }
//
//        public static String getLocationSettingFromUri(Uri uri) {
//            return uri.getPathSegments().get(1);
//        }
//
//        public static long getDateFromUri(Uri uri) {
//            return Long.parseLong(uri.getPathSegments().get(2));
//        }
//
//        public static long getStartDateFromUri(Uri uri) {
//            String dateString = uri.getQueryParameter(COLUMN_DATE);
//            if (null != dateString && dateString.length() > 0)
//                return Long.parseLong(dateString);
//            else
//                return 0;
//        }
    }
}
