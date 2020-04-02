package com.webaddicted.kotlinproject.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityWebviewBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.global.common.MediaPickerHelper
import com.webaddicted.kotlinproject.global.common.showToast
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.global.misc.WebChromeClientTest
import com.webaddicted.kotlinproject.view.base.BaseActivity


class WebViewActivity : BaseActivity() {
    private var url: String = "http://www.google.com"
    private lateinit var mBinding: ActivityWebviewBinding
    val INPUT_FILE_REQUEST_CODE = 1
    var mUploadMessage: ValueCallback<Array<Uri>>? = null
    var mCameraPhotoPath: String = ""

    companion object {
        val TAG = WebViewActivity::class.java.simpleName
        fun newIntent(context: Context) {
            context.startActivity(Intent(context, WebViewActivity::class.java))
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_webview
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityWebviewBinding
        init()
        clickListener()
        checkStoragePermission()
        normalWebView(mBinding.webview)
    }
    private fun init() {
        mBinding.toolbar.imgBack.visible()
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.webview_title)
    }
    private fun clickListener() {
        mBinding.toolbar.imgBack.setOnClickListener(this)
        mBinding.btnNormalWebview.setOnClickListener(this)
        mBinding.btnWebClick.setOnClickListener(this)
        mBinding.btnWebFileChoose.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_back -> onBackPressed()
            R.id.btn_normal_webview -> normalWebView(mBinding.webview)
            R.id.btn_web_click -> webInterface(mBinding.webview)
            R.id.btn_web_file_choose -> chooseImageWebViewUrl(
                this,
                mBinding.webview,
                "https://en.imgbb.com/"
            )
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.mBinding.webview.canGoBack()) {
            this.mBinding.webview.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun normalWebView(webView: WebView) {
        webView.settings.setJavaScriptEnabled(true)
        webView.settings.setLoadWithOverviewMode(true)
        webView.settings.setUseWideViewPort(true)
        webView.setScrollbarFadingEnabled(true)
        webView.setVerticalScrollBarEnabled(false)
        webView.setWebViewClient(myTestBrowser())
        try {
            webView.loadUrl(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun webInterface(webView: WebView) {
        mBinding.toolbar.imgBack.visibility = View.VISIBLE
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.webview_title)
        webView.settings.setLoadsImagesAutomatically(true);
//        webView.settings.setUserAgentString("android");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.settings.setJavaScriptEnabled(true)
        webView.settings.setLoadWithOverviewMode(true)
        webView.settings.setUseWideViewPort(true)
        webView.setScrollbarFadingEnabled(true)
        webView.setVerticalScrollBarEnabled(false)
        webView.addJavascriptInterface(
            WebAppInterface(
                this
            ), "android"
        )
        webView.setWebViewClient(myTestBrowser())
        val str = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h2>Sample HTML Page</h2>\n" +
                "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n" +
                "<script type=\"text/javascript\">\n" +
                "    function showAndroidToast(toast) {\n" +
                "        android.showToast(toast);\n" +
                "    }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>"
        webView.loadData(str, "text/html", "utf-8")
    }

    private inner class myTestBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            mBinding.progressBar.setVisibility(View.VISIBLE)
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            mBinding.progressBar.setVisibility(View.GONE)
            super.onPageFinished(view, url)
        }
    }

    /** Instantiate the interface and set the context  */
    class WebAppInterface(private val mContext: Context) {
        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * show response url on webview
     *
     * @param webView    view
     * @param contentUrl webview url
     */
    fun chooseImageWebViewUrl(activity: Activity?, webView: WebView, contentUrl: String?) {
        try {
            val cookieManager = CookieManager.getInstance()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeAllCookies(ValueCallback<Boolean> { aBoolean ->
                    // a callback which is executed when the cookies have been removed
//                    Log.d(FragmentActivity.TAG, "Cookie removed: " + aBoolean!!)
                })
            } else
                cookieManager.removeAllCookie()
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.isScrollbarFadingEnabled = true
            webView.isVerticalScrollBarEnabled = false
            webView.settings.userAgentString = "android"
            webView.getSettings().setAppCacheEnabled(false)
            webView.getSettings().domStorageEnabled = true
            webView.clearCache(true)
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().allowFileAccess = (true);
            webView.getSettings().allowContentAccess = (true);
            if (Build.VERSION.SDK_INT >= 19) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
//            webView.getSettings().Access = (true);
            webView.webChromeClient = WebChromeClientTest(activity)
            webView.webViewClient = myTestBrowser()
            webView.loadUrl(contentUrl)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mUploadMessage == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        try {
            if (resultCode == 0) {
                Lg.d(TAG, "when user cancel then reset web chrome client")
                mBinding.webview?.webChromeClient = WebChromeClientTest(this)
                mUploadMessage?.onReceiveValue(arrayOf(Uri.parse("")))
                return
            }
            if (data?.data != null || data?.clipData != null) {
                val files = MediaPickerHelper().getData(this, data)
                val arrayLists = Array(files.size, { i -> Uri.fromFile(files[i]) })
                showToast("selected image size - " + files.size)
                mUploadMessage?.onReceiveValue(arrayLists)
            } else {
                mUploadMessage?.onReceiveValue(arrayOf(Uri.parse(mCameraPhotoPath)))
                showToast("captured image - " + mCameraPhotoPath)
            }
        } catch (e: Exception) {
            Lg.e("Error!", "Error while opening image file: ${e.localizedMessage}")
            mUploadMessage?.onReceiveValue(arrayOf(Uri.parse("")))
            mUploadMessage = null
        }
        mUploadMessage = null
    }
}

