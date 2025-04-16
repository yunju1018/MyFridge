package com.yunju.myfridge.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.yunju.myfridge.MainActivity
import com.yunju.myfridge.R

class MyReceiver : BroadcastReceiver() {
    // https://hanyeop.tistory.com/217

    private lateinit var notificationManager: NotificationManager

    companion object {
        const val CHANNEL_ID = "notification_channel"
        const val NOTIFICATION_NAME = "냉장고_유통기한_임박_알림"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // BroadCast 수신 시 자동으로 호출
//        val product: Product? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getSerializableExtra("name", Product::class.java)
//        } else {
//            intent.getSerializableExtra("name") as Product
//        }

        val name: String = intent.getStringExtra("name").toString()
        val date: String = intent.getStringExtra("dateExpire").toString()

        val id: Int = intent.getIntExtra("id", 0)

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(context)
        deliveryNotification(context, id, name, date)
    }

    private fun createNotificationChannel(context: Context) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)
        /*
        1. IMPORTANCE_HIGH = 알림음이 울리고 헤드업 알림으로 표시
        2. IMPORTANCE_DEFAULT = 알림음 울림
        3. IMPORTANCE_LOW = 알림음 없음
        4. IMPORTANCE_MIN = 알림음 없고 상태줄 표시 X
        */

        notificationChannel.enableLights(true)           // 불빛
        notificationChannel.enableVibration(true)      // 진동
        notificationChannel.description = context.getString(R.string.app_name)  // 앱 이름
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun deliveryNotification(context: Context, notificationId: Int, name: String, dateExpire: String) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(context,       // 특정 시점 실행 될 Intent (보류중인 Intent)
            notificationId,                                                // requestCode
            contentIntent,                                                  // 알림 선택 시 실행 될 인텐트
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        /*
        ** Android 12 (API 레벨 31) 이상 에서 PendingIntent 생성할 때 FLAG_IMMUTABLE 또는 FLAG_MUTABLE 플래그 중 하나를 명시 해야 하는 보안 요구 사항 **
        ** LAG_IMMUTABLE: PendingIntent 변경이 불가능, 주로 알람 또는 고정된 작업을 실행할 때 사용, 기존 플래그와 함께 사용 가능
        1. FLAG_UPDATE_CURRENT : 현재 PendingIntent 유지, 해당 intent extra data 변경
        2. FLAG_CANCEL_CURRENT : 등록된 intent 삭제, 다시 등록
        3. FLAG_NO_CREATE : 등록된 intent 없다면 null 반환, 생성된 intent 있다면 해당 intent 반환
        4. FLAG_ONE_SHOT : 한번 사용 되면, 그 다음에 다시 사용 하지 않음
        */

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_noti)        // icon 필수
            .setContentTitle("유통기한 임박 알림")
            .setContentText("$name 상품의 유통기한이 $dateExpire 까지입니다.")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(notificationId, builder)
    }
}