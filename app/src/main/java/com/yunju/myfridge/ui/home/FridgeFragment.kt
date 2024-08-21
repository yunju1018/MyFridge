package com.yunju.myfridge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yunju.myfridge.MyApplication
import com.yunju.myfridge.R
import com.yunju.myfridge.base.BaseFragment
import com.yunju.myfridge.databinding.FragmentFridgeBinding
import com.yunju.myfridge.room.FridgeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FridgeFragment : BaseFragment() {

    companion object {
        fun newInstance() = FridgeFragment()
    }

    private lateinit var binding: FragmentFridgeBinding
    private var test = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_fridge, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = (mActivity.application as? MyApplication)?.fridgeDao

        binding.btnAdd.setOnClickListener {
            if (dao != null) {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        // 데이터베이스 작업을 IO 스레드에서 수행
                        dao.insertData(FridgeEntity(test.toString()))
                    }
                    // UI 업데이트는 메인 스레드에서 수행
                    test++
                    val id = withContext(Dispatchers.IO) { dao.getFridgeId() }
                    Log.d("yj", "id : $id")
                }
            } else {
                Log.d("yj", "dao is null")
            }
        }
    }
}