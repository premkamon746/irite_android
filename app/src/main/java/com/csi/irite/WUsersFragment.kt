package com.csi.irite

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.csi.irite.data.repo.TokenManager
import com.csi.irite.room.data.RefreshRequest
import com.csi.irite.room.data.RefreshResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.csi.irite.sync.ApiService
import com.csi.irite.sync.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WUsersFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_incident, container, false)
        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        val token = TokenManager.getAccessToken()
        webView.loadUrl("http://${BuildConfig.host}:3300/index.html?token=$token")

        var interConUid = uid
        val itc = object : InterConnect{

            @RequiresApi(Build.VERSION_CODES.O)
            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)
                val button = jsonObject.get("button").asString.trim()
                if(button == "add-evident"){
                    val uid = jsonObject.get("uid").asString.toLong()
                }
                return ""
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
                if(func == "base_url") {
                    try {
                        returnData = getParam(requireContext(), "BASE_URL")
                    } catch (e: SocketTimeoutException) {
                        Log.e("syncLog", "$tag timeout: ${e.message}")
                    }
                }else if(func == "getURL") {
                    return getURL()
                }else if(func == "refresh_token"){
                    //Log.d("token", "func refresh_token")
                    val refreshToken = TokenManager.getRefreshToken()

                    CoroutineScope(Dispatchers.IO).launch {
                        val tkRefresh = RefreshRequest(refreshToken!!)
                        val call = RetrofitClient.instance.refresh(tkRefresh)

                        call.enqueue(object : Callback<RefreshResponse> {
                            override fun onResponse(call: Call<RefreshResponse>, response: Response<RefreshResponse>) {
                                if (response.isSuccessful) {
                                    val tokenReq = response.body()
                                    val token = tokenReq!!.accessToken
                                    TokenManager.saveAccessToken(token)
                                    webView.evaluateJavascript("window.onTokenRefreshed('${token}')", null)

                                    Log.d("LOGIN", "Success: ${token}")
                                } else {
                                    // Handle error (e.g. 401, 403)
                                    Log.e("LOGIN", "Failed with code: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<RefreshResponse>, t: Throwable) {
                                // Network or conversion error
                                Log.e("LOGIN", "Error: ${t.localizedMessage}")
                            }
                        })

                    }

                }
                return returnData
            }
        }
        webView.addJavascriptInterface(JavaScriptInterface(activity ,itc ), "Android")
        return view
    }
}