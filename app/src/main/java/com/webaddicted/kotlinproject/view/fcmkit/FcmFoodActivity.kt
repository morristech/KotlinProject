package com.webaddicted.kotlinproject.view.fcmkit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.global.constant.AppConstant
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.view.fragment.TaskFrm

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class FcmFoodActivity : BaseActivity() {
    private lateinit var mBinding: ActivityCommonBinding

    companion object {
        val TAG: String = FcmFoodActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, FcmFoodActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_common
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
        init()
        setNavigationColor(ContextCompat.getColor(context, R.color.app_color))
    }

    private fun init() {
        navigateScreen(FcmSocialLoginFrm.TAG)
    }

    /**
     * navigate to welcome activity after Splash timer Delay
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            FcmSocialLoginFrm.TAG -> frm = FcmSocialLoginFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }
}