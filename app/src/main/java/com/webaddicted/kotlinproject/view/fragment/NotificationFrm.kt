package com.webaddicted.kotlinproject.view.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmNotificationBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility.Companion.getTwoDigitRandomNo
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.constant.AppConstant
import com.webaddicted.kotlinproject.global.services.NotificationBroadcastReceiver
import com.webaddicted.kotlinproject.global.services.NotificationBroadcastReceiver.Companion.ACTION_CALL
import com.webaddicted.kotlinproject.global.services.NotificationBroadcastReceiver.Companion.ACTION_DISMISS
import com.webaddicted.kotlinproject.global.services.NotificationBroadcastReceiver.Companion.ACTION_NO_IDEA
import com.webaddicted.kotlinproject.view.activity.HomeActivity
import com.webaddicted.kotlinproject.view.activity.SplashActivity
import com.webaddicted.kotlinproject.view.base.BaseFragment
import java.util.*

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
        mBinding.btnLargeText.setOnClickListener(this)
        mBinding.btnNormal.setOnClickListener(this)
        mBinding.btnAddMessage.setOnClickListener(this)
        mBinding.btnUrl.setOnClickListener(this)
        mBinding.btnAction.setOnClickListener(this)
        mBinding.btnBigPicture.setOnClickListener(this)
        mBinding.btnCustom.setOnClickListener(this)
        mBinding.btnInboxStyle.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> activity?.onBackPressed()
            R.id.btn_large_text -> largeTextNoti()
            R.id.btn_normal -> normalNoti()
            R.id.btn_add_message -> addMessageNoti()
            R.id.btn_url -> urlNoti()
            R.id.btn_action -> actionNoti()
            R.id.btn_big_picture -> bigPictureNoti()
            R.id.btn_custom -> customNoti()
            R.id.btn_inbox_style -> inboxStyleNoti()
        }
    }

    private fun inboxStyleNoti() {
        val resultintent =
            Intent(activity, SplashActivity::class.java)
        resultintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val piResult = PendingIntent.getActivity(
            activity,
            Calendar.getInstance().timeInMillis.toInt(),
            resultintent,
            0
        )
        val inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("Inbox Notification")
        inboxStyle.addLine("Meaasge Deepak")
        inboxStyle.addLine("Meaasge Raj")
        inboxStyle.addLine("Meaasge Ahmad")
        inboxStyle.addLine("Meaasge Pankaj")
        inboxStyle.addLine("Meaasge Pardeep")
        inboxStyle.setSummaryText("+3 more")
        val builder = getNotiBuilder()
            .setStyle(inboxStyle)
            .addAction(R.mipmap.ic_launcher, "Show Activity", piResult)
        val manager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notiChannel(builder, manager)
        manager.notify(getTwoDigitRandomNo(), builder.build())
    }

    private fun customNoti() {
        val remoteViews = RemoteViews(activity?.packageName,R.layout.row_product_cat)
        val intent = Intent(activity, SplashActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            activity, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews.setImageViewResource(R.id.img_logo, R.drawable.logo)
        remoteViews.setImageViewResource(R.id.img_right, R.drawable.iphnx)
        remoteViews.setTextViewText(R.id.txt_title, getString(R.string.app_name))
        remoteViews.setTextViewText(R.id.txt_body, getString(R.string.dummyText))
        val builder: NotificationCompat.Builder = getNotiBuilder()
            .setSmallIcon(R.drawable.ic_whatsapp) // Set Ticker Message
            .setAutoCancel(true) // Set PendingIntent into Notification
            .setContentIntent(pIntent) // Set RemoteViews into Notification
            .setCustomBigContentView(remoteViews)
        // Locate and set the Image into customnotificationtext.xml ImageViews

        val manager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notiChannel(builder, manager)
        // Build Notification with Notification Manager
        // Build Notification with Notification Manager
        manager.notify(getTwoDigitRandomNo(), builder.build())
    }

    private fun bigPictureNoti() {
        val bigTextStyle: NotificationCompat.BigPictureStyle = NotificationCompat.BigPictureStyle()
        bigTextStyle.bigPicture(BitmapFactory.decodeResource(resources, R.drawable.man))
            .build()
        val manager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val resultintent = Intent(activity, HomeActivity::class.java)
        resultintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val piResult = PendingIntent.getActivity(
            activity,
            Calendar.getInstance().timeInMillis.toInt(),
            resultintent,
            0
        )
        val builder = getNotiBuilder()
            .setSmallIcon(R.drawable.ic_food)
            .setStyle(bigTextStyle)
            .addAction(R.mipmap.ic_launcher, "Show Activity", piResult)
            .addAction(
                android.R.drawable.ic_menu_delete,
                "share",
                PendingIntent.getActivity(activity, 0, resultintent, 0, null)
            )
        notiChannel(builder, manager)
        manager.notify(0, builder.build())
    }

    private fun actionNoti() {
        val intent = Intent(activity, NotificationBroadcastReceiver::class.java)
        val pIntent = PendingIntent.getActivity(activity, 0, intent, 0)
        val builder: NotificationCompat.Builder = getNotiBuilder()
        builder.setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.large_text))
            .setSmallIcon(R.drawable.cart)
            .addAction(android.R.drawable.ic_menu_call, ACTION_CALL, pIntent)
            .addAction(android.R.drawable.ic_menu_delete, ACTION_DISMISS, pIntent)
            .addAction(android.R.drawable.stat_notify_call_mute, ACTION_NO_IDEA, pIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.dummyText)))
            .setContentIntent(pIntent)

        val manager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notiChannel(builder, manager)
        manager.notify(getTwoDigitRandomNo(), builder.build())

    }

    private fun urlNoti() {
        val builder: NotificationCompat.Builder = getNotiBuilder()
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("http://www.androidauthority.com/"))
        val pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)
        builder.setContentIntent(pendingIntent)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("My Second notification")
        builder.setContentText("Check 2")
        val manager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notiChannel(builder, manager)
        manager.notify(getTwoDigitRandomNo(), builder.build())
    }

    private fun addMessageNoti() {
        val resultintent =
            Intent(activity, SplashActivity::class.java)
        resultintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val piResult = PendingIntent.getActivity(
            activity,
            Calendar.getInstance().timeInMillis.toInt(),
            resultintent,
            0
        )
        val inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("Inbox Notification")
        inboxStyle.addLine("Meaasge Deepak")
        val builder: NotificationCompat.Builder = getNotiBuilder()
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.marshmallow))
            .setStyle(inboxStyle)
            .setContentIntent(piResult)
//            .setGroup("GROUP_ID_STRING")
        val manager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notiChannel(builder, manager)
        manager.notify(getTwoDigitRandomNo(), builder.build())
    }

    private fun normalNoti() {
        val builder: NotificationCompat.Builder = getNotiBuilder()
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.mobile_no_linked))
        val manager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notiChannel(builder, manager)
        val notificationIntent = Intent(activity, SplashActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            activity, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(contentIntent)
        manager.notify(getTwoDigitRandomNo(), builder.build())
    }

    private fun largeTextNoti() {
        val bigTextStyle: NotificationCompat.BigTextStyle =
            NotificationCompat.BigTextStyle()
        bigTextStyle.bigText(getString(R.string.dummyText))
        bigTextStyle.setBigContentTitle(getString(R.string.app_name))
        bigTextStyle.setSummaryText("By :Deepak Sharma")
        val notificationManager =
            activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = getNotiBuilder()
        notificationBuilder.setStyle(bigTextStyle)
        notiChannel(notificationBuilder, notificationManager)
        notificationManager.notify(getTwoDigitRandomNo(), notificationBuilder.build())
    }

    private fun notiChannel(
        builder: NotificationCompat.Builder,
        manager: NotificationManager
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                AppConstant.NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME App name",
                importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(1000, 1000)
            builder.setChannelId(AppConstant.NOTIFICATION_CHANNEL_ID)
            manager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotiBuilder(): NotificationCompat.Builder {
        val icon1 =
            BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val notificationSound = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )
        return NotificationCompat.Builder(activity)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker")
            .setContentTitle("Big Text Style Notification")
            .setContentText("This is Big text of Notification")
            .setLargeIcon(icon1)
            .setAutoCancel(false)
            .setSound(notificationSound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setVibrate(longArrayOf(100, 250))
                as NotificationCompat.Builder
    }

}

