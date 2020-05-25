package com.creyc.test.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.creyc.test.R
import com.creyc.test.models.Cells
import kotlinx.android.synthetic.main.cell.view.*

class MineAdapter(cellsList: Array<Array<Cells>>) : BaseAdapter() {

    private val digits = arrayListOf(
        R.drawable.ic_empty, //0
        R.drawable.ic_one,
        R.drawable.ic_two,
        R.drawable.ic_three,
        R.drawable.ic_four,
        R.drawable.ic_five,
        R.drawable.ic_six,
        R.drawable.ic_seven,
        R.drawable.ic_eight,
        R.drawable.ic_flag,  //9
        R.drawable.ic_cell,   //10
        R.drawable.ic_bomb  //11
    )

    private var cellList = cellsList

    override fun getCount(): Int {
        return cellList.size*cellList.size
    }

    override fun getItem(position: Int): Any {
        return cellList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun update(){
        notifyDataSetChanged()
    }

    private fun chooseDrawable(item: Cells): Int {
        if (item.flags)
            return 9
        if (item.mines!=0 && item.opened)
            return 11
        if (item.opened && item.mines==0)
            return item.near
        return 10
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cell = this.cellList[position / 8][position % 8]
        val inflater = parent?.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.cell, null)
        view.cell.setBackgroundResource(digits[chooseDrawable(cell)])
        return view
    }
}