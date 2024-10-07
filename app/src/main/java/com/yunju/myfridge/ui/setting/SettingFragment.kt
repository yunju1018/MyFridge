package com.yunju.myfridge.ui.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.yunju.myfridge.MyReceiver
import com.yunju.myfridge.MyReceiver.Companion.NOTIFICATION_ID
import com.yunju.myfridge.R
import com.yunju.myfridge.base.BaseFragment
import com.yunju.myfridge.databinding.FragmentSettingBinding

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
        val alarmManager = mActivity.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(mActivity, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(mActivity, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        binding.notifySwitch.setOnCheckedChangeListener { view, ischecked ->
            val toggleMessage = if (ischecked) {
                val triggerTime = (SystemClock.elapsedRealtime() // 기기가 부팅된 후 경과한 시간 사용
                        + ALARM_TIMER * 1000) // ms 이기 때문에 초단위로 변환 (*1000)
                alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
                ) // set : 일회성 알림
                "$ALARM_TIMER 초 후에 알림이 발생합니다."
            } else {
                alarmManager.cancel(pendingIntent)
                "알림 예약을 취소하였습니다."
            }

            /*
            1. ELAPSED_REALTIME : ELAPSED_REALTIME 사용. 절전모드에 있을 때는 알람을 발생시키지 않고 해제되면 발생시킴.
            2. ELAPSED_REALTIME_WAKEUP : ELAPSED_REALTIME 사용. 절전모드일 때도 알람을 발생시킴.
            3. RTC : Real Time Clock 사용. 절전모드일 때는 알람을 발생시키지 않음.
            4. RTC_WAKEUP : Real Time Clock 사용. 절전모드 일 때도 알람을 발생시킴.
             */

            Toast.makeText(mActivity, toggleMessage, Toast.LENGTH_SHORT).show()
        }
    }
}