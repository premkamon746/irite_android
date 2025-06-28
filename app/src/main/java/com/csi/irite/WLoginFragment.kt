package com.csi.irite

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.csi.irite.data.repo.TokenManager
import com.csi.irite.room.dao.LifeReportDao
import com.csi.irite.room.data.LoginRequest
import com.csi.irite.room.data.LoginResponse
import com.csi.irite.sync.RetrofitClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call


class WLoginFragment : BaseFragment() {
    private lateinit var authenticator: GoogleAuthenticator2FA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authenticator = GoogleAuthenticator2FA()
        val view =  inflater.inflate(R.layout.fragment_incident, container, false)
        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        webView.loadUrl("file:///android_asset/w-login.html")
        var interConUid = uid

        val itc = object : InterConnect{


            override fun submitForm(it:String): String {

                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)
                val button = jsonObject.get("button").asString.trim()
                var msg = ""
               if(button == "login") {
                    jsonObject.remove("button")
                    val email = jsonObject.get("email").asString.trim()
                    val password = jsonObject.get("password").asString.trim()
                    val twoFACode = jsonObject.get("_2fa").asString.trim()
                    msg = performLogin(email, password, twoFACode)
                }
                return msg
            }

            override fun getUpdateData():String {
                return ""
            }

            override fun getJsonData(ref: String, func:String,option:String): String {
                var returnData = ""
                val gson = GsonBuilder()
                    .serializeNulls() // Include null fields
                    .setPrettyPrinting() // Optional: for pretty printing
                    .create()
                if(func == "config2fa") {
                    val intent = Intent(requireActivity().applicationContext, Setup2FAActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                return returnData
            }
        }
        webView.addJavascriptInterface(JavaScriptInterface(activity ,itc ), "Android")
        return view
    }

    fun performLogin(
        email: String,
        password: String,
        twoFACode: String
    ): String {
        if (email.isEmpty() || password.isEmpty()) {
            return "Please fill in all fields"
        }

        val request = LoginRequest(email, password)
        val call = RetrofitClient.instance.login(request)

        return try {
            val url = call.request().url.toString()  // get full URL from the call
            Log.d("_Login","Calling URL: $url")
            val response = call.execute() // synchronous call
            Log.d("_Login","response body : "+response.body().toString())
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    if (twoFACode.isEmpty()) {
                        return "Please enter 2FA code"
                    }

                    val secretKey = getSavedSecretKey()
                    return if (secretKey != null && authenticator.validateTOTP(secretKey, twoFACode)) {
                        body.accessToken?.let { TokenManager.saveTokens(it, body.refreshToken) }
                        loginSuccess()
                        "success"
                    } else {
                        "Invalid 2FA code"
                    }
                } else {
                    "Login failed: ${body?.message ?: "unknown error"}"
                }
            } else {
                "Server error: ${response.code()}"
            }
        } catch (e: Exception) {
            val url = call.request().url.toString()
            "Network error calling $url : ${e.message}"
        }
    }


    private fun validateCredentials(email: String, password: String, callback: (Boolean) -> Unit) {
        // Implement your credential validation logic here
        // This could be a network call to your backend

        // Example implementation:
        Thread {
            try {
                // Simulate network call
                Thread.sleep(1000)

                // Replace with actual validation logic
                val isValid = email == "user@example.com" && password == "password123"
                requireActivity().runOnUiThread {
                    if (isValid) {
                        // Save user session
                        saveUserSession(email)
                    }
                    callback(isValid)
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    callback(false)
                }
            }
        }.start()
    }

    private fun saveUserSession(email: String) {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("user_email", email)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    private fun is2FAEnabled(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("2fa_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("is_2fa_enabled", false)
    }

    private fun getSavedSecretKey(): String? {
        val sharedPref = requireActivity().getSharedPreferences("2fa_prefs", Context.MODE_PRIVATE)

        Log.d("_Login",sharedPref.getString("secret_key", null).toString())
        return sharedPref.getString("secret_key", null)
    }

    private fun loginSuccess() {
        Toast.makeText(requireActivity().applicationContext, "Login successful!", Toast.LENGTH_SHORT).show()
        // Navigate to main activity
        val intent = Intent(requireActivity().applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

}