package com.creyc.test.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleTagStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.creyc.test.adapters.MineAdapter

@StateStrategyType(value = AddToEndSingleTagStrategy::class)
interface MinesSweeperView: MvpView {
    fun setAdapter(mineAdapter: MineAdapter)
    fun endGame(win: Boolean)
    fun startGame()
}