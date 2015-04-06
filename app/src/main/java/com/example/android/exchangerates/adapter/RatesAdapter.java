package com.example.android.exchangerates.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.exchangerates.R;
import com.example.android.exchangerates.Utility;
import com.example.android.exchangerates.fragment.DetailFragment;

/**
 * Created by gena on 23.03.15.
 */

/**
 * {@link RatesAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class RatesAdapter extends CursorAdapter {
    public RatesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_rate, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.iconView.setImageResource(Utility.getIconForRate(context, cursor.getString(DetailFragment.COL_RATE_CURRENCY)));
        viewHolder.currencyView.setText(cursor.getString(DetailFragment.COL_RATE_CURRENCY));
        viewHolder.askView.setText(cursor.getString(DetailFragment.COL_RATE_ASK));
        viewHolder.bidView.setText(cursor.getString(DetailFragment.COL_RATE_BID));
        view.setTag(viewHolder);
    }

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView currencyView;
        public final TextView askView;
        public final TextView bidView;

        public ViewHolder(View view) {

            iconView = (ImageView) view.findViewById(R.id.list_item_currency_icon);
            currencyView = (TextView) view.findViewById(R.id.list_item_currency_textview);
            askView = (TextView) view.findViewById(R.id.list_item_ask_textview);
            bidView = (TextView) view.findViewById(R.id.list_item_bid_textview);
        }
    }

}
