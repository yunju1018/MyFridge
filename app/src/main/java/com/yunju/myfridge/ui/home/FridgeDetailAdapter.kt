package com.yunju.myfridge.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.FridgeDetailItemBinding
import com.yunju.myfridge.models.Product

class FridgeDetailAdapter(val removeProduct: (position: Int) -> Unit): RecyclerView.Adapter<FridgeDetailAdapter.FridgeViewHolder>(){

    private var productList = listOf<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeViewHolder {
        val binding : FridgeDetailItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fridge_detail_item, parent, false)
        return FridgeViewHolder(binding)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: FridgeViewHolder, position: Int) {
        Log.d("yj", "onBindViewHolder")
        holder.bind(productList[position], position)
    }

    fun setList(productList: List<Product>) {
        Log.d("yj", "setList")
        this.productList = productList
        notifyDataSetChanged()
    }

    inner class FridgeViewHolder(private val binding: FridgeDetailItemBinding): RecyclerView.ViewHolder (binding.root) {
        fun bind(product: Product, position: Int) {
            with(binding) {
                productName.text = product.productName
                dateAdded.text = product.dateAdded
                dateExpire.text = product.dateExpire

                itemView.setOnLongClickListener {
                    AlertDialog.Builder(itemView.context)
                        .setTitle("${product.productName} 삭제하시겠습니까?")
                        .setPositiveButton("확인", object: DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                removeProduct(position)
                            }
                        })
                        .setNeutralButton("취소", null)
                        .show()
                    false
                }
            }
        }
    }
}