package com.yunju.myfridge.base

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yunju.myfridge.room.FridgeDao

abstract class BaseFragment : Fragment() {
    lateinit var mActivity: AppCompatActivity
    lateinit var mContext: Context
    lateinit var dao: FridgeDao

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as AppCompatActivity
        mContext = mActivity
    }
}