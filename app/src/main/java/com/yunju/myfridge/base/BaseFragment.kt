package com.yunju.myfridge.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yunju.myfridge.room.FridgeDao

abstract class BaseFragment : Fragment() {
    private lateinit var mActivity: AppCompatActivity
    private lateinit var mContext: Context
    lateinit var dao: FridgeDao

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as AppCompatActivity
        mContext = mActivity
    }
}