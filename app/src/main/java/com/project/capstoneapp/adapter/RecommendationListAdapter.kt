package com.project.capstoneapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.capstoneapp.databinding.ItemActivityRecommendationListBinding
import com.project.capstoneapp.data.model.RecommendationActivity

class RecommendationListAdapter (
    private val recommendationList: ArrayList<RecommendationActivity>
) : RecyclerView.Adapter<RecommendationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemActivityRecommendationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(recommendationList[position])
    }

    override fun getItemCount() = recommendationList.size

    class ViewHolder(private var itemBinding: ItemActivityRecommendationListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun setData(recommendationActivity: RecommendationActivity) {
            itemBinding.tvRecommendationName.text = recommendationActivity.name
            itemBinding.tvRecommendationTime.text = StringBuilder("${recommendationActivity.time} Mins")
            Glide.with(itemBinding.root)
                .load(recommendationActivity.image)
                .into(itemBinding.ivRecommendationImage)
        }
    }
}