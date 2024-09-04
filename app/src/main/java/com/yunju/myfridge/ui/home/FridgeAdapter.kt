package com.yunju.myfridge.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.FridgeListItemBinding

class FridgeAdapter(val removeFridge: (fridgeId: String) -> Unit): RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder>(){

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
                val intent = Intent(itemView.context, FridgeDetailActivity::class.java)
                intent.putExtra("fridgeId", fridgeId)
                itemView.context.startActivity(intent)
            }

            binding.fridgeItem.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("${fridgeId} 목록을 삭제하시겠습니까?")
                    .setPositiveButton("확인", object: DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            removeFridge(fridgeId)
                        }
                    })
                    .setNeutralButton("취소", null)
                    .show()
                false
            }
        }
    }
}