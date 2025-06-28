package com.csi.irite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.csi.irite.data.repo.TokenManager
import com.csi.irite.ui.LoginActivity
import android.util.Log

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        //Log.d("xxxxxxxxxx", TokenManager.isLoggedIn().toString())
        if (!TokenManager.isLoggedIn() &&/**/ this::class.simpleName != "LoginActivity") {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}