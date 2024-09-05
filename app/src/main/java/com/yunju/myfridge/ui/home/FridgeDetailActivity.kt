package com.yunju.myfridge.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
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
    private lateinit var id: String
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
        adapter = FridgeDetailAdapter(
            editProduct = { position, product ->
                val argInfo = ProductDetailDialog.ArgInfo(product.productName, product.dateAdded, product.dateExpire)
                ProductDetailDialog.newInstance(this, argInfo, addProduct = {
                    productList[position].productName = it.productName
                    productList[position].dateAdded = it.dateAdded
                    productList[position].dateExpire = it.dateExpire

                    val entity = FridgeEntity(id, productList)
                    viewModel.updateFridgeData(id, entity)
                })

        }, removeProduct = {
            AlertDialog.Builder(this)
                .setTitle("${productList[it].productName} 항목을 삭제하시겠습니까?")
                .setPositiveButton("확인", object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        productList.removeAt(it)
                        val entity = FridgeEntity(id, productList)
                        viewModel.updateFridgeData(id, entity)
                    }
                })
                .setNeutralButton("취소", null)
                .show()
        })

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