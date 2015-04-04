package com.example.android.exchangerates;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.exchangerates.Utility;

/**
 * Created by gena on 23.03.15.
 */

/**
 * {@link BanksAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class BanksAdapter extends CursorAdapter {
    public BanksAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_bank, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        return cursor.getString(DetailFragment.COL_BANK_NAME);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.iconView.setImageResource(Utility.getIconForBank(context, cursor.getInt(BanksFragment.COL_BANK_OLD_ID), false));
        viewHolder.nameView.setText(convertCursorRowToUXFormat(cursor));
        view.setTag(viewHolder);
    }


    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView nameView;


        public ViewHolder(View view) {

            iconView = (ImageView) view.findViewById(R.id.list_item_bank_icon);
            nameView = (TextView) view.findViewById(R.id.list_item_bank_textview);
        }
    }

}
