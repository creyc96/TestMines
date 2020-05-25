package com.creyc.test.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleTagStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
@StateStrategyType(value = AddToEndSingleTagStrategy::class)
interface SplashView: MvpView {
    fun startChecking(msg: String)
    fun endChecking()
    fun toastMe(msg: String)
    @StateStrategyType(value = SkipStrategy::class)
    fun changeFragmentToMinesweeper()
    @StateStrategyType(value = SkipStrategy::class)
    fun changeFragmentToWebView(url: String)
}