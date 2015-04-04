package com.example.android.exchangerates;

import android.content.Context;

/**
 * Created by gena on 27.03.15.
 */
public class Utility {

    public static int getIconForBank(Context context, int bankOldId, boolean isBig) {
        String prefix = "";
        if (isBig == true) {
            prefix = "big_";
        }
        int icon = context.getResources().getIdentifier(prefix + "bank_" + bankOldId, "drawable", context.getPackageName());
        if (icon != 0) {
            return icon;
        } else {
            return context.getResources().getIdentifier(prefix + "bank_default", "drawable", context.getPackageName());
        }
    }

    public static int getIconForRate(Context context, String currency) {
        return context.getResources().getIdentifier("cur_" + currency.toLowerCase(), "drawable", context.getPackageName());
    }

    public static String getFullAddress(String city, String address) {
        return city + ", " + address;

    }
}
