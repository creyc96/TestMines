package com.creyc.test.fragmets

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.creyc.test.R
import com.creyc.test.adapters.MineAdapter
import com.creyc.test.presenters.MinesSweeperPresenter
import com.creyc.test.views.MinesSweeperView
import kotlinx.android.synthetic.main.fragment_minesweeper.view.*

class MinesSweeperFragment : MvpAppCompatFragment(), MinesSweeperView {

    @InjectPresenter
    lateinit var minesSweeperPresenter: MinesSweeperPresenter

    private lateinit var navController: NavController
    private lateinit var root: View

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_minesweeper, container, false)
        navController = NavHostFragment.findNavController(this)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        minesSweeperPresenter.setup()
        root.field.setOnItemClickListener { _, _, i, _ ->
            minesSweeperPresenter.pressed(i / 8, i % 8, "")
        }
        root.field.setOnItemLongClickListener { _, _, i, _ ->
            minesSweeperPresenter.pressed(i / 8, i % 8, "long_click")
            true
        }
        root.restart.setOnClickListener {
            minesSweeperPresenter.setup()
        }
        return root
    }

    override fun setAdapter(mineAdapter: MineAdapter) {
        root.field.adapter = mineAdapter
    }

    override fun endGame(win: Boolean) {
        root.field.isEnabled = false
        if (win){
            root.ivWin.visibility = View.VISIBLE
        }
        else {
            root.ivLose.visibility = View.VISIBLE
        }

    }

    override fun startGame() {
        root.field.isEnabled = true
        root.ivWin.visibility = View.GONE
        root.ivLose.visibility = View.GONE
    }

}