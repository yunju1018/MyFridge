package com.yunju.myfridge.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.yunju.myfridge.R
import com.yunju.myfridge.base.BaseFragment
import com.yunju.myfridge.databinding.EditDialogBinding
import com.yunju.myfridge.databinding.FragmentFridgeBinding
import kotlinx.coroutines.withContext

class FridgeFragment : BaseFragment() {

    companion object {
        fun newInstance() = FridgeFragment()
    }

    private val viewModel: FridgeViewModel by viewModels()
    private lateinit var binding: FragmentFridgeBinding
    private lateinit var fridgeAdapter: FridgeAdapter

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
            val editBinding = EditDialogBinding.inflate(layoutInflater)

            AlertDialog.Builder(mContext)
                .setTitle("이름을 입력하세요")
                .setView(editBinding.root)
                .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val id = editBinding.editText.text.toString()
                        if(id.isEmpty()) {
                            Toast.makeText(mContext, "냉장고 이름은 필수 입력 사항입니다.", Toast.LENGTH_SHORT).show()
                            return
                        }
                        viewModel.insertData(id)
                        Log.d("yj", "id : $id")
                    }
                })
                .setNegativeButton("취소", null)
                .show()
        }

        binding.btnRemove.setOnClickListener{
            AlertDialog.Builder(mContext)
                .setTitle("냉장고 목록을 전체 삭제합니다.")
                .setPositiveButton("확인", object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        viewModel.deleteFridge()
                    }
                })
                .setNeutralButton("취소", null)
                .show()
        }

        fridgeAdapter = FridgeAdapter() {
            viewModel.deleteByFridgeId(it)
        }

        binding.fridgeRecyclerView.apply {
            adapter = fridgeAdapter
        }
    }

    private fun observe() {
        viewModel.fridgeId?.observe(mActivity) {
            fridgeAdapter.setList(it)
        }
    }
}