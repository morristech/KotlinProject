package com.webaddicted.kotlinproject.view.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmNotificationBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.constant.AppConstant
import com.webaddicted.kotlinproject.view.base.BaseFragment


class NotificationFrm : BaseFragment() {
    private lateinit var mBinding: FrmNotificationBinding

    companion object {
        val TAG = NotificationFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): NotificationFrm {
            val fragment = NotificationFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_notification
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmNotificationBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.notification_title)
    }

    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnLaunch.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_launch -> {
                val icon1 =
                    BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
                val bigTextStyle: NotificationCompat.BigTextStyle =
                    NotificationCompat.BigTextStyle()
                bigTextStyle.bigText(getString(R.string.dummyText))
                bigTextStyle.setBigContentTitle(getString(R.string.app_name))
                bigTextStyle.setSummaryText("By :Deepak Sharma")
                val notificationSound = RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_NOTIFICATION
                )

                val notification: NotificationCompat.Builder = NotificationCompat.Builder(activity)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Big Text Style Notification")
                    .setContentText("This is Big text of Notification")
                    .setLargeIcon(icon1)
                    .setAutoCancel(false)
                    .setSound(notificationSound)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(longArrayOf(100, 250))
                    .setStyle(bigTextStyle) as NotificationCompat.Builder
                // .addAction(R.mipmap.ic_launcher, "Show Activity", piResult);
                // .addAction(R.mipmap.ic_launcher, "Show Activity", piResult);
                val notificationManager =
                    activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val notificationChannel = NotificationChannel(
                        AppConstant.NOTIFICATION_CHANNEL_ID,
                        "NOTIFICATION_CHANNEL_NAME",
                        importance
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.enableVibration(true)
                    notificationChannel.vibrationPattern = longArrayOf(1000, 1000)
                    assert(notificationManager != null)
                    notification.setChannelId(AppConstant.NOTIFICATION_CHANNEL_ID)
                    notificationManager.createNotificationChannel(notificationChannel)
                }
                notificationManager.notify(GlobalUtility.getTwoDigitRandomNo(), notification.build())
            }
        }
    }
}

