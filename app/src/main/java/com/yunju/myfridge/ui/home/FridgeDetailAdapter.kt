package com.yunju.myfridge.ui.home

import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.FridgeDetailItemBinding
import com.yunju.myfridge.models.Product
import com.yunju.myfridge.util.DateUtil

class FridgeDetailAdapter(val editProduct: (position: Int, product: Product) -> Unit, val removeProduct: (position: Int) -> Unit): RecyclerView.Adapter<FridgeDetailAdapter.FridgeViewHolder>(){

    private var productList = listOf<Product>()
    private val today = Calendar.getInstance()

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

                if(product.dateExpire.isNullOrEmpty()) {
                    hyphenTextView.visibility = View.GONE
                    dateExpire.visibility = View.GONE
                } else {
                    hyphenTextView.visibility = View.VISIBLE
                    dateExpire.visibility = View.VISIBLE
                }

                setRemainDate(product.dateAdded, product.dateExpire)

                itemView.setOnClickListener {
                    editProduct(position, product)
                }

                itemView.setOnLongClickListener {
                    removeProduct(position)
                    false
                }
            }
        }

        private fun setRemainDate(addedDate: String?, expireDate: String?) {
            val calendar = Calendar.getInstance()
            val remainTextView = binding.remainTextView
            if (!expireDate.isNullOrEmpty()) {
                calendar.time = DateUtil.FORMAT_DATE_YYYY_YEAR_MM_MONTH_DD_DAY.parse(expireDate)

                val remainTime = calendar.time.time - today.time.time
                val remainDate = (remainTime / (24 * 60 * 60 * 1000)).toInt()

                Log.d("yj", "remainDate : $remainDate")

                if(remainDate > 0) {
                    remainTextView.text = "D - $remainDate"
                    remainTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                } else if (remainDate == 0) {
                    remainTextView.text = "오늘까지"
                    remainTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                } else {
                    remainTextView.text = "${-remainDate} 일 지남"
                    remainTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                }
            } else {
                addedDate?.let {
                    calendar.time = DateUtil.FORMAT_DATE_YYYY_YEAR_MM_MONTH_DD_DAY.parse(addedDate)

                    val addedTime = today.time.time - calendar.time.time
                    val elapsedTime = (addedTime / (24 * 60 * 60 * 1000)).toInt()

                    remainTextView.text = "${elapsedTime} 일 경과"
                    remainTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
                }
            }
        }
    }
}