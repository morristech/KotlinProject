package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.iid.FirebaseInstanceId
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.view.base.BaseActivity
import com.webaddicted.kotlinproject.view.fragment.TaskFrm

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class HomeActivity : BaseActivity() {

    private lateinit var mBinding: ActivityCommonBinding

    companion object {
        val TAG: String = HomeActivity::class.java.simpleName
        fun newIntent(activity: Activity) {
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_common
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
        Lg.d(TAG, "ok token - "+FirebaseInstanceId.getInstance().token)
        navigateScreen(TaskFrm.TAG)
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            TaskFrm.TAG -> frm = TaskFrm.getInstance(Bundle())
        }
        if (frm != null) navigateFragment(R.id.container, frm, false)
    }
}