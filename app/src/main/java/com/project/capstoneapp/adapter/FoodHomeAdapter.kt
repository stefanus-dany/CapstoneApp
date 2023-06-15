package com.project.capstoneapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.capstoneapp.databinding.ItemHomeFoodListBinding
import com.project.capstoneapp.data.model.Food

class FoodHomeAdapter(
    private val foodList: ArrayList<Food>
) : RecyclerView.Adapter<FoodHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeFoodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (foodList.isNotEmpty()) {
            holder.setData(foodList[position])
        }
    }

    override fun getItemCount(): Int {
        return if (foodList.isEmpty()) {
            1
        } else {
            foodList.size
        }
    }

    class ViewHolder(private val itemBinding: ItemHomeFoodListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun setData(food: Food) {
            itemBinding.tvFoodName.text = food.foodName
            itemBinding.tvFoodDetail.text = food.foodDetail
            itemBinding.tvFoodTime.text = food.foodTime
            itemBinding.tvCal.text = StringBuilder("${food.foodCal} cal")
        }
    }


}