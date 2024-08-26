package com.yunju.myfridge.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.yunju.myfridge.MyApplication
import com.yunju.myfridge.room.FridgeDao
import com.yunju.myfridge.room.FridgeEntity
import kotlinx.coroutines.launch

class FridgeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: FridgeDao = (application as MyApplication).fridgeDao

    var test = 1

    var fridgeId : LiveData<List<String>>? = dao.getFridgeId()

    fun insertData() {
        viewModelScope.launch {
            dao.insertData(FridgeEntity(test.toString()))
            test++
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