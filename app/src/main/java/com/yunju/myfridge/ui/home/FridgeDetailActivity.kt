package com.yunju.myfridge.ui.home

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yunju.myfridge.util.MyReceiver
import com.yunju.myfridge.R
import com.yunju.myfridge.databinding.ActivityFridgeDetailBinding
import com.yunju.myfridge.models.Product
import com.yunju.myfridge.room.FridgeEntity
import com.yunju.myfridge.util.DateUtil
import com.yunju.myfridge.util.PreferencesUtil

class FridgeDetailActivity : AppCompatActivity() {

    private val viewModel: FridgeViewModel by viewModels()
    private lateinit var binding: ActivityFridgeDetailBinding
    private lateinit var adapter: FridgeDetailAdapter
    private lateinit var id: String
    private var productList = mutableListOf<Product>()
    private lateinit var alarmManager : AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fridge_detail)
        id = intent.getSerializableExtra("fridgeId").toString()
//        val level33id = intent.getSerializableExtra("fridgeId", String::class.java)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

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

                    setNotify(it)
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

                setNotify(it)
            })
        }
    }

    private fun setNotify(product: Product) {
        // 알림 설정 하지 않았다면 알림 없음
        if(!PreferencesUtil.getNotify(this)) return

        product.dateExpire?.let {
            val today = Calendar.getInstance()
            val expireDate = Calendar.getInstance()
            var id  = 0

            try {
                expireDate.time = DateUtil.FORMAT_DATE_YYYY_YEAR_MM_MONTH_DD_DAY.parse(it)

                // 종료까지 남은 날짜
                val resultSeconds = (expireDate.timeInMillis - today.timeInMillis) / (24 * 60 * 60 * 1000)
                Log.d("yj", "resultSeconds : $resultSeconds")

                expireDate.set(Calendar.HOUR_OF_DAY, 10)
                expireDate.set(Calendar.MINUTE, 0)
                id = ((DateUtil.FORMAT_DATE_YYYY_YEAR_MM_MONTH_DD_DAY.parse(it)?.time ?: 0) / 1000).toInt()

            } catch (e: Exception) {
                Log.d("yj", "setNotify Calendar() Exception : $e")
                return
            }

            val intent = Intent(this, MyReceiver::class.java).apply {
                putExtra("name", product.productName)
                putExtra("dateExpire", product.dateExpire)
                putExtra("id", id)
            }

            val pendingIntent = PendingIntent.getBroadcast(this, expireDate.timeInMillis.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.set(AlarmManager.RTC_WAKEUP, expireDate.timeInMillis, pendingIntent)

            /*
            1. ELAPSED_REALTIME : ELAPSED_REALTIME 사용. 절전모드에 있을 때는 알람을 발생시키지 않고 해제되면 발생시킴.
            2. ELAPSED_REALTIME_WAKEUP : ELAPSED_REALTIME 사용. 절전모드일 때도 알람을 발생시킴.
            3. RTC : Real Time Clock 사용. 절전모드일 때는 알람을 발생시키지 않음.
            4. RTC_WAKEUP : Real Time Clock 사용. 절전모드 일 때도 알람을 발생시킴.
             */

            /**
             * permission : USE_EXACT_ALARM, SCHEDULE_EXACT_ALARM
             * Target SDK 31 요구 사항 : 정확한 알람 권한
             * 핵심 기능으로 정확한 알람을 사용하는 앱에서만 사용 -> 앱 내려갈수도 있음.
             */
        }
    }
}