package com.webaddicted.kotlinproject.view.fcmkit

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmFcmEmailAuthBinding
import com.webaddicted.kotlinproject.global.common.GlobalUtility
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.ValidationHelper
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.fragment.ZoomImageFrm
import com.webaddicted.kotlinproject.viewModel.fcmkit.FcmFoodViewModel
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FcmEmailAuthFrm : BaseFragment() {
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var mBinding: FrmFcmEmailAuthBinding
    private val viewModel: FcmFoodViewModel by viewModel()

    companion object {
        val TAG = FcmEmailAuthFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): FcmEmailAuthFrm {
            val fragment = FcmEmailAuthFrm()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_fcm_email_auth
    }

    override fun initUI(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmFcmEmailAuthBinding
        init()
        clickListener()
    }

    private fun init() {
        mBinding.toolbar.imgNavRight.gone()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.email_auth)
        fireAuth = FirebaseAuth.getInstance()
    }

    private fun clickListener() {
        mBinding.toolbar.imgNavLeft.setOnClickListener(this)
        mBinding.btnLogin.setOnClickListener(this)
        mBinding.btnSignup.setOnClickListener(this)
        mBinding.btnForgotPass.setOnClickListener(this)
        mBinding.btnUpdate.setOnClickListener(this)
        mBinding.btnFirePush.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_nav_left -> (activity as FcmFoodHomeActivity).openCloseDrawer(true)
            R.id.btn_login -> emailLogin()
            R.id.btn_signup -> emailSignup()
            R.id.btn_forgot_pass -> emailForgotPass()
            R.id.btn_update -> emailUpdate()
            R.id.btn_fire_push -> {
                GlobalScope.launch(Dispatchers.Main + Job()) {
                    withContext(Dispatchers.Default) {
                        val result = firePushNoti()
                        try {
                            val resultJson = JSONObject(result)
                            val success: Int
                            val failure: Int
                            success = resultJson.getInt("success")
                            failure = resultJson.getInt("failure")
                            Log.d(TAG, "onResponse: onPostExecute: $success")
                            GlobalUtility.showToast("Notification send successfully.\nMessage Success: $success \nMessage Failed: $failure")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            GlobalUtility.showToast("Message Failed, Unknown error occurred.\n${e.message}")
                        }
                    }
                }
            }
        }
    }

    private fun emailLogin() {
        if (!ValidationHelper.validateEmail(mBinding.edtEmail, mBinding.wrapperEmail) ||
            !ValidationHelper.validatePwd(mBinding.edtPwd, mBinding.wrapperPwd)
        ) return
        fireAuth.signInWithEmailAndPassword(
            mBinding.edtEmail.text.toString(),
            mBinding.edtEmail.text.toString()
        ).addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful)
                mBinding.txtLoginRespo.text = "Login Respo : \nUser loggedin successfully.\n"
            else mBinding.txtLoginRespo.text = "Login Respo : \n${task.exception?.message}\n"
        }
    }

    private fun emailSignup() {
        if (!ValidationHelper.validateEmail(mBinding.edtEmail, mBinding.wrapperEmail) ||
            !ValidationHelper.validatePwd(mBinding.edtPwd, mBinding.wrapperPwd)
        ) return
        fireAuth.createUserWithEmailAndPassword(
            mBinding.edtEmail.text.toString(),
            mBinding.edtEmail.text.toString()
        ).addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful)
                mBinding.txtSignupRespo.text = "Signup Respo : \nSuccessfully register.\n"
            else mBinding.txtSignupRespo.text = "Signup Respo : \n${task.exception?.message}\n"
        }
    }

    private fun emailForgotPass() {
        if (!ValidationHelper.validateEmail(mBinding.edtEmail, mBinding.wrapperEmail)) return
        fireAuth.sendPasswordResetEmail(mBinding.edtEmail.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    mBinding.txtForgotRespo.text =
                        "ForgotPass Respo : \nLink is Successfully sent to your email id.\n"
                else mBinding.txtForgotRespo.text =
                    "ForgotPass Respo : \n${task.exception?.message}\n"
            }
    }

    private fun emailUpdate() {
        //get current user
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updateEmail(mBinding.edtEmail.text.toString().trim { it <= ' ' })
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        mBinding.txtUpdateUser.text =
                            "UpdateUser Respo : \nSuccessfully email id updated.\n"
                    else mBinding.txtUpdateUser.text =
                        "UpdateUser Respo : \n${task.exception?.message}\n"
                }
        } else mBinding.txtUpdateUser.text =
            "User not exist, please login first\n"
    }

    private fun navigateScreen(tag: String, bundle: Bundle) {
        var frm: Fragment? = null
        when (tag) {
            FcmOtpFrm.TAG -> frm = FcmOtpFrm.getInstance(bundle)
            ZoomImageFrm.TAG -> frm = ZoomImageFrm.getInstance(bundle)
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }


    private fun firePushNoti(): String? {
        try {
            val root = JSONObject()
            val notification = JSONObject()
            notification.put("body", "body")
            notification.put("title", "title")
            //                    notification.put("icon", icon);
            val data = JSONObject()
            data.put("message", "message")
            root.put("notification", notification)
            root.put("data", data)
//            root.put("registration_ids", "recipients")
            val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
            val JSON =
                MediaType.parse("application/json; charset=utf-8")

            val body: RequestBody = RequestBody.create(JSON, root.toString())
            val request =
                Request.Builder()
//                    .url(FCM_MESSAGE_URL)
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(body)
                    .addHeader(
                        "Authorization",
                        "key=" + "AAAAsi55X5Y:APA91bGoXntyJcBoW8evgk8DuZNDfhb6m7iu4Vs8Uf-cW2IqjlcM0GOu6DzqOZxbJkQdnXvq9E3pmdi9CieSBA9A0vU4un9ja6_KiFT9r8k9pk3QZIxxe7wLRJEqErxa1sS8O_ZMyjOK"//server key
                    )
                    .build()
            val response: Response = OkHttpClient().newCall(request).execute()
            return response.body()?.string()
            Lg.d(TAG, "Noti Respo : ${response.body()?.string()}")
        } catch (ex: Exception) {
            ex.printStackTrace()
            return ex.message
        }
    }

    fun openImg(image: String?) {
        val bundle = Bundle()
        bundle.putString(ZoomImageFrm.IMAGE_PATH, image)
        bundle.putBoolean(ZoomImageFrm.IS_LOCAL_FILE, false)
        navigateScreen(ZoomImageFrm.TAG, bundle)
    }
}

