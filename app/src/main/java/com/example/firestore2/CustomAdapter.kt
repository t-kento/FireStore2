package com.example.firestore2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.firestore_activity.view.*

class CustomAdapter (private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Item>()

    var callback: CustomAdapterCallback? = null

    fun refresh(list: List<Item>) {
        items.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    fun add(item: Item) {
        if (items.map { it.createdAt }.contains(item.createdAt))
            return
        items.add(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.firestore_activity, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder)
            onBindViewHolder(holder, position)
    }

    private fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem: Item = items[position]
        holder.apply {
            editSinger.text = currentItem.editSinger
            singer.text =currentItem.singer
//            deleteButton.setOnClickListener {
//                callback?.onClick(currentItem)
//            }
        }
    }

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val editSinger = view.edit_singer
        val singer = view.singer
//        val deleteButton = view.deleteButton
    }

    interface CustomAdapterCallback {
        fun onClick(data: Item)
    }
}