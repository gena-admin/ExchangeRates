package com.example.android.exchangerates.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ExchangeRatesSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static ExchangeRatesSyncAdapter sExchangeRatesSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("ExchangeRatesSyncService", "onCreate - ExchangeRatesSyncService");
        synchronized (sSyncAdapterLock) {
            if (sExchangeRatesSyncAdapter == null) {
                sExchangeRatesSyncAdapter = new ExchangeRatesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sExchangeRatesSyncAdapter.getSyncAdapterBinder();
    }
}