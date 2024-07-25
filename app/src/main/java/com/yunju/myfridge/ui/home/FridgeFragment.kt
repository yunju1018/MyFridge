package com.yunju.myfridge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yunju.myfridge.MyApplication
import com.yunju.myfridge.R
import com.yunju.myfridge.base.BaseFragment
import kotlinx.coroutines.launch

class FridgeFragment : BaseFragment() {

    companion object {
        fun newInstance() = FridgeFragment()
    }

    private val viewModel: FridgeModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_fridge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = (requireActivity().application as? MyApplication)?.fridgeDao

        lifecycleScope.launch {
            val id = dao?.getFridgeId()
            Log.d("yj", "id : $id")
        }
    }
}