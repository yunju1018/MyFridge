package com.yunju.myfridge.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.yunju.myfridge.R
import com.yunju.myfridge.base.BaseFragment
import com.yunju.myfridge.databinding.FragmentSettingBinding
import com.yunju.myfridge.util.PreferencesUtil

class SettingFragment : BaseFragment() {

    companion object {
        fun newInstance() = SettingFragment()
        const val ALARM_TIMER = 5
    }

    private val viewModel: SettingViewModel by viewModels()
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
    }

    private fun setLayout() {
        val isNotify = PreferencesUtil.getNotify(mActivity)
        binding.notifySwitch.isChecked = isNotify
        binding.notifySwitch.setOnCheckedChangeListener { view, checked ->
            val toggleMessage = if (checked) {
                PreferencesUtil.setNotify(mActivity, true)
                "유통기한 임박 알림이 설정되었습니다."
            } else {
                PreferencesUtil.setNotify(mActivity, false)
                "알림 예약을 취소하였습니다."
            }

            Toast.makeText(mActivity, toggleMessage, Toast.LENGTH_SHORT).show()
        }
    }
}