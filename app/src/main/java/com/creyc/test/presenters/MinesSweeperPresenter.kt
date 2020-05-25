package com.creyc.test.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.creyc.test.adapters.MineAdapter
import com.creyc.test.models.Cells
import com.creyc.test.views.MinesSweeperView
import kotlin.random.Random


@InjectViewState
class MinesSweeperPresenter : MvpPresenter<MinesSweeperView>() {

    private var width: Int = 8
    private var height: Int = 8
    private var found: Int = 0
    private var total: Int = 0
    private var loseFlag: Boolean = false
    private var winFlag: Boolean = false
    private var numMines: Int = 10
    private lateinit var cells: Array<Array<Cells>>
    private lateinit var mineAdapter: MineAdapter

    fun setup() {
        cells = Array(width) { Array(height) {
                Cells(
                    mines = 0,
                    flags = false,
                    opened = false,
                    near = 0
                )
            }
        }
        found=0
        total = 0
        loseFlag = false
        winFlag = false
        mineAdapter = MineAdapter(cells)
        setFields()
        viewState.startGame()
        viewState.setAdapter(mineAdapter)
    }

    private fun placeMines() {
        var i = 0
        while (i < numMines) {
            val x = Random.nextInt(0, width)
            val y = Random.nextInt(0, height)
            if (cells[x][y].mines == 1) continue
            cells[x][y].mines = 1
            i++
        }
    }

    private fun clearMines() {
        for (x in 0 until width)
            for (y in 0 until height)
                cells[x][y].mines = 0
    }


    private fun outOfBounds(x: Int, y: Int): Boolean = (x < 0 || y < 0 || x >= width || y >= height)

    private fun findNear(x: Int, y: Int): Int {
        if (outOfBounds(x, y)) return 0
        var i = 0
        for (offsetX in -1..1) {
            for (offsetY in -1..1) {
                if (outOfBounds(offsetX + x, offsetY + y)) continue
                i += cells[offsetX + x][offsetY + y].mines
            }
        }
        return i
    }

    private fun placeNear() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                cells[x][y].near = findNear(x, y)
            }
        }
    }

    private fun open(x: Int, y: Int) {
        if (outOfBounds(x, y)) return
        if (cells[x][y].opened) return
        cells[x][y].opened = true
        total++
        if (findNear(x, y) != 0) return
        open(x - 1, y - 1)
        open(x - 1, y + 1)
        open(x + 1, y - 1)
        open(x + 1, y + 1)
        open(x - 1, y)
        open(x + 1, y)
        open(x, y - 1)
        open(x, y + 1)
    }

    private fun setFields(){
        clearMines()
        placeMines()
        placeNear()
    }

    fun pressed(x: Int, y: Int, type: String) {
        if (type == "long_click" && !cells[x][y].opened) {
            cells[x][y].flags = !cells[x][y].flags
            if (cells[x][y].flags) total++
            else total--
            if (cells[x][y].mines==1) {
                if (cells[x][y].flags) found++
                else found--

            }
        } else {
            if (cells[x][y].mines != 0) {
                cells[x][y].opened = true
                loseFlag = true
            } else
                open(x, y)
        }

        if (total==64 && found==10) winFlag = true
        if (loseFlag) {
            for (i in 0 until width)
                for (j in 0 until height)
                    if (cells[i][j].mines==1)
                        cells[i][j].opened =true
            viewState.endGame(false)
        }
        if (winFlag)
            viewState.endGame(true)
        mineAdapter.update()
    }

}