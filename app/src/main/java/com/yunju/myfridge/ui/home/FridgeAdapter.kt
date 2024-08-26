package com.yunju.myfridge.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.FridgeListItemBinding

class FridgeAdapter: RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder>(){

    private var fridgeList = listOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeViewHolder {
        val binding : FridgeListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fridge_list_item, parent, false)
        return FridgeViewHolder(binding)
    }

    override fun getItemCount() = fridgeList.size

    override fun onBindViewHolder(holder: FridgeViewHolder, position: Int) {
        Log.d("yj", "onBindViewHolder")
        holder.bind(fridgeList[position])
    }

    fun setList(fridgeList: List<String>) {
        Log.d("yj", "setList")
        this.fridgeList = fridgeList
        notifyDataSetChanged()
    }

    inner class FridgeViewHolder(private val binding: FridgeListItemBinding): RecyclerView.ViewHolder (binding.root) {
        fun bind(fridgeId: String) {
            binding.fridgeId.text = fridgeId

            binding.fridgeItem.setOnClickListener {

            }
        }
    }
}