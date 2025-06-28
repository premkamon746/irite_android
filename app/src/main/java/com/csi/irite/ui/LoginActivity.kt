package com.csi.irite.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.csi.irite.BaseActivity
import com.csi.irite.BaseFragment.InterConnect
import com.csi.irite.BaseFragment.JavaScriptInterface
import com.csi.irite.HomeFragment
import com.csi.irite.IncidentFragment
import com.csi.irite.MainActivity
import com.csi.irite.R
import com.csi.irite.WLoginFragment
import com.csi.irite.data.repo.AuthRepository
import com.csi.irite.room.database.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.csi.irite.data.repo.TokenManager
import com.csi.irite.data.repo.TokenManager.sharedPreferences
import com.csi.irite.sync.ApiService
import com.csi.irite.utils.NetworkCheck

// LoginActivity.kt
class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel
    var db: AppDatabase? = null
    protected lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        TokenManager.init(this)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        loadFragment(WLoginFragment())
    }
}
