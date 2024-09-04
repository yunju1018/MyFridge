package com.yunju.myfridge.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yunju.myfridge.MyApplication
import com.yunju.myfridge.models.Product
import com.yunju.myfridge.room.FridgeDao
import com.yunju.myfridge.room.FridgeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FridgeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: FridgeDao = (application as MyApplication).fridgeDao

    var fridgeId : LiveData<List<String>>? = dao.getFridgeId()
    private val _fridgeData = MutableLiveData<FridgeEntity>()
    val fridgeData: LiveData<FridgeEntity> get() = _fridgeData

    fun insertData(data: String) {
        // viewModelScope는 생명주기에 따라 관리됨, 기본 Dispatchers : Main
        viewModelScope.launch {
            // withContext : 코루틴 내부에서 다른 Coroutine Dispatchers 전환 시 사용
            // 지정된 코루틴 context 써서 일시 중단 블록을 호출, 완료될 때까지 일시 중단한 다음 결과를 반환
            val hasId = withContext(Dispatchers.IO) {
                dao.countById(data) > 0
            }

            if (!hasId) {
                dao.insertData(FridgeEntity(data, listOf<Product>()))
            } else {
                Toast.makeText(getApplication(), "중복된 이름입니다. 다른 이름을 설정해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getFridgeData(id: String) {
        Log.d("yj", "getFridgeData()")
        viewModelScope.launch {
            _fridgeData.value = dao.getFridgeData(id)
        }
    }

    fun updateFridgeData(id: String, fridgeEntity: FridgeEntity) {
        Log.d("yj", "updateFridgeData()")
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateFridgeData(fridgeEntity)
            val fridgeData = dao.getFridgeData(id)

            withContext(Dispatchers.Main) {
                _fridgeData.value = fridgeData
            }
        }
    }

    fun deleteFridge() {
        Log.d("yj", "deleteFridge()")
        viewModelScope.launch {
            dao.deleteAllFridge()
        }
    }
}