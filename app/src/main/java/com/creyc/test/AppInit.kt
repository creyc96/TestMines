package com.creyc.test

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.yandex.metrica.push.YandexMetricaPush

private val key = "01bf73bd-6f72-49dd-b550-9229f099a58e"


class AppInit : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = YandexMetricaConfig.newConfigBuilder(key).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetricaPush.init(applicationContext)
    }
}