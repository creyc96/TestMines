package com.creyc.test.fragmets

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.webkit.WebChromeClient.FileChooserParams
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.creyc.test.R
import kotlinx.android.synthetic.main.fragment_web_view.view.*


@Suppress("DEPRECATION")
class WebViewFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_web_view, container, false)
        root.load_progress_bar.visibility = View.VISIBLE
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        webView = root.findViewById(R.id.wv)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                root.load_progress_bar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                //For API <21
                CookieSyncManager.getInstance().sync()
                //root.load_progress_bar.visibility = View.GONE
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                root.load_progress_bar.visibility = View.GONE
            }

            /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            {
                override fun onPageFinished(view: WebView, url: String?) {
                    setTitle(view.title)
                    progressBar.setVisibility(View.GONE)
                    super.onPageFinished(view, url)
                }
            }*/
        }

        webView.webChromeClient = MyChromeClient()
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.isScrollbarFadingEnabled = false

        setSettings()

        val url = arguments?.getSerializable("url_from_splash") as String

        if (savedInstanceState==null){
            webView.post {
                kotlin.run { webView.loadUrl(url)  }
            }
        }

        webView.canGoBack()
        webView.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK &&
                    event.action == MotionEvent.ACTION_UP &&
                        webView.canGoBack()) {
                webView.goBack()
                return@OnKeyListener true
            }
            false
        })

        return root
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun setSettings(){
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webSettings.setAppCacheEnabled(true)
    }

    var filePathCallback: ValueCallback<Array<Uri>>? = null
    private val REQUEST_CODE = 100

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    inner class MyChromeClient : WebChromeClient() {

        override fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {

            filePathCallback = filePath

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                startActivityForResult(
                    Intent.createChooser(intent, "File Browser"),
                    REQUEST_CODE
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val intent = fileChooserParams.createIntent()
                startActivityForResult(intent, REQUEST_CODE)
            }
            return true
        }


        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0

        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(activity!!.applicationContext.resources, 2130837573)
        }

        override fun onHideCustomView() {
            (activity!!.window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            activity!!.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            activity?.requestedOrientation = mOriginalOrientation
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View?,
            paramCustomViewCallback: CustomViewCallback?
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility = activity!!.window.decorView.systemUiVisibility
            mOriginalOrientation = activity?.requestedOrientation!!
            mCustomViewCallback = paramCustomViewCallback
            (activity!!.window.decorView as FrameLayout).addView(
                mCustomView,
                FrameLayout.LayoutParams(-1, -1)
            )
            activity!!.window.decorView.systemUiVisibility = 3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            filePathCallback!!.onReceiveValue(FileChooserParams.parseResult(resultCode, intent))
            filePathCallback = null
        }
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val result = intent!!.data as Array<Uri>
            filePathCallback!!.onReceiveValue(result)
            filePathCallback = null
        }
    }

}
