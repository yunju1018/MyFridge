package com.yunju.myfridge.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.ActivityFridgeDetailBinding
import com.yunju.myfridge.models.Product
import com.yunju.myfridge.room.FridgeEntity

class FridgeDetailActivity : AppCompatActivity() {

    private val viewModel: FridgeViewModel by viewModels()
    private lateinit var binding: ActivityFridgeDetailBinding
    private lateinit var adapter: FridgeDetailAdapter
    private lateinit var id : String
    private var productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fridge_detail)
        id = intent.getSerializableExtra("fridgeId").toString()
//        val level33id = intent.getSerializableExtra("fridgeId", String::class.java)

        setLayout()
        observe()
        viewModel.getFridgeData(id)
    }

    private fun observe() {
        viewModel.fridgeData.observe(this) { entity ->
            entity.productList?.let {
                adapter.setList(it)
                productList = it.toMutableList()
            }
        }
    }

    private fun setLayout() {
        binding.fridgeId.text = id
        adapter = FridgeDetailAdapter() {
            productList.removeAt(it)
            val entity = FridgeEntity(id, productList)
            viewModel.updateFridgeData(id, entity)
        }
        binding.fridgeRecyclerView.adapter = adapter

        binding.btnAdd.setOnClickListener {
            ProductDetailDialog.newInstance(this, addProduct = {
                productList.add(it)
                val entity = FridgeEntity(id, productList)
                viewModel.updateFridgeData(id, entity)
            })
        }
    }
}