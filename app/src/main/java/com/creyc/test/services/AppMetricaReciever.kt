package com.creyc.test.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yandex.metrica.push.YandexMetricaPush

class AppMetricaReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val loadedStr = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD)
    }
}