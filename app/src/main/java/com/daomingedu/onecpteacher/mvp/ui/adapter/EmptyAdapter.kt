package com.daomingedu.onecpteacher.mvp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.daomingedu.onecpteacher.R

/**
 * 数据为空适配器
 */
class EmptyAdapter(val context: Context, val list: List<String>) : RecyclerView.Adapter<EmptyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_empty, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
    }


    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){

    }
}