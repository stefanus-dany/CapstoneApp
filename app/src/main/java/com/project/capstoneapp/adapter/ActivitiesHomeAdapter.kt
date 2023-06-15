package com.project.capstoneapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.capstoneapp.databinding.ItemHomeActivityListBinding

class ActivitiesHomeAdapter : RecyclerView.Adapter<ActivitiesHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            ItemHomeActivityListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount() = 15

    class ViewHolder(itemBinding: ItemHomeActivityListBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}