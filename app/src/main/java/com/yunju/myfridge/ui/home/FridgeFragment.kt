package com.yunju.myfridge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
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

    private val viewModel: FridgeViewModel by viewModels()
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

        observe()

        binding.btnAdd.setOnClickListener {
            viewModel.insertData()
        }

        binding.btnRemove.setOnClickListener{
            viewModel.deleteFridge()
        }
    }

    private fun observe() {
        viewModel.fridgeId?.observe(mActivity) {
            Log.d("yj", "id : $it")
        }
    }
}