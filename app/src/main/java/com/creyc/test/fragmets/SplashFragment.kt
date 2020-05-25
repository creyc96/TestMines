package com.creyc.test.fragmets


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.creyc.test.R
import com.creyc.test.presenters.SplashPresenter
import com.creyc.test.views.SplashView
import kotlinx.android.synthetic.main.fragment_splash.*
import java.util.*

class SplashFragment : MvpAppCompatFragment(), SplashView {

    @InjectPresenter
    lateinit var splashPresenter: SplashPresenter

    private lateinit var navController: NavController

    @SuppressLint("HardwareIds", "SourceLockedOrientationActivity")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_splash, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        navController = NavHostFragment.findNavController(this)
        val ANDROID_ID = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        val LOCALE: String = Locale.getDefault().language
        splashPresenter.requestServer(ANDROID_ID, LOCALE)
        return root
    }

    override fun startChecking(msg: String) {
        splash_progress_bar.visibility = View.VISIBLE
        pb_text.text = msg
    }

    override fun endChecking() {
        splash_progress_bar.visibility = View.GONE
    }

    override fun toastMe(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    override fun changeFragmentToMinesweeper() {
        navController.navigate(R.id.minesSweeperFragment)
    }

    override fun changeFragmentToWebView(url: String) {
        val bundle = bundleOf("url_from_splash" to url)
        navController.navigate(R.id.webViewFragment, bundle)
    }
}