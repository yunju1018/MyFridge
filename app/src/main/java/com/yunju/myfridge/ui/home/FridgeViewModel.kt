package com.yunju.myfridge.ui.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.yunju.myfridge.MyApplication
import com.yunju.myfridge.room.FridgeDao
import com.yunju.myfridge.room.FridgeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FridgeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: FridgeDao = (application as MyApplication).fridgeDao

    var fridgeId : LiveData<List<String>>? = dao.getFridgeId()

    fun insertData(data: String) {
        // viewModelScope는 생명주기에 따라 관리됨, 기본 Dispatchers : Main
        viewModelScope.launch {
            // withContext : 코루틴 내부에서 다른 Coroutine Dispatchers 전환 시 사용
            // 지정된 코루틴 context 써서 일시 중단 블록을 호출, 완료될 때까지 일시 중단한 다음 결과를 반환
            val hasId = withContext(Dispatchers.IO) {
                dao.countById(data) > 0
            }

            if (!hasId) {
                dao.insertData(FridgeEntity(data))
            } else {
                Toast.makeText(getApplication(), "중복된 이름입니다. 다른 이름을 설정해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateFridgeData(fridgeEntity: FridgeEntity) {
        viewModelScope.launch {
            dao.updateFridgeData(fridgeEntity)
        }
    }

    fun deleteFridge() {
        viewModelScope.launch {
            dao.deleteAllFridge()
        }
    }
}