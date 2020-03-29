package com.webaddicted.kotlinproject.view.fcmkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmFcmSocialLoginBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.sociallogin.OnSocialLoginListener
import com.webaddicted.kotlinproject.global.sociallogin.SocialLogin
import com.webaddicted.kotlinproject.global.sociallogin.auth.FacebookAuth
import com.webaddicted.kotlinproject.global.sociallogin.auth.GoogleAuth
import com.webaddicted.kotlinproject.global.sociallogin.auth.TwitterAuth
import com.webaddicted.kotlinproject.global.sociallogin.model.SocialLoginResponse
import com.webaddicted.kotlinproject.view.base.BaseFragment


class FcmSocialLoginFrm : BaseFragment(), OnSocialLoginListener {
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var mBinding: FrmFcmSocialLoginBinding

    companion object {
        val TAG = FcmSocialLoginFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): FcmSocialLoginFrm {
            val fragment = FcmSocialLoginFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_fcm_social_login
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmFcmSocialLoginBinding
        init()
        clickListener()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        // Configure Google Sign In
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        callbackManager = CallbackManager.Factory.create()
    }


    private fun clickListener() {
        mBinding.btnGoogle.setOnClickListener(this)
        mBinding.btnFb.setOnClickListener(this)
        mBinding.btnTwitter.setOnClickListener(this)
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.btn_google -> {
                SocialLogin.logout(activity)
                GoogleAuth.googleLogin(
                    activity!!,
                    getString(R.string.default_web_client_id),
                    this
                )
            }
            R.id.btn_fb -> {
//            Log.d("TAG", FacebookAuth.getHashKey(activity!!))
                SocialLogin.logout(activity)
                FacebookAuth.fbLogin(activity!!, this)
            }
            R.id.btn_twitter -> {
//                SocialLogin.logout(activity)
                TwitterAuth.twitterLogin(
                    activity!!,
                    getString(R.string.twitter_consumer_key),
                    getString(R.string.twitter_consumer_secret),
                    this
                )
            }
            R.id.btn_login -> {
            }
            R.id.btn_register -> {
            }
        }
    }


    override fun onSocialLoginSuccess(loginResponse: SocialLoginResponse?) {
        GlobalUtility.showToast(Gson().toJson(loginResponse))
        Lg.d("TAG", "Social login : " + Gson().toJson(loginResponse))
    }

    override fun onSocialLoginSuccess(success: String?) {
        GlobalUtility.showToast(success!!)
        Lg.d("TAG", success)
    }

    override fun onSocialLoginFailure(failure: String?) {
        GlobalUtility.showToast(failure!!)
        Lg.d("TAG", failure)
    }
}

