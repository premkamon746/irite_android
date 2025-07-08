package com.csi.irite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

class WUploadFragment : BaseFragment() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_incident, container, false)
        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                this@WUploadFragment.filePathCallback?.onReceiveValue(null)
                this@WUploadFragment.filePathCallback = filePathCallback
                val intent = fileChooserParams.createIntent()
                try {
                    startActivityForResult(intent, 100)
                } catch (e: Exception) {
                    this@WUploadFragment.filePathCallback = null
                    return false
                }
                return true
            }
        }
        val url = "http://${BuildConfig.host}:3100/index.html?incident_id=$uid"
        Log.d("update_file_url", "url ${url}")
        webView.loadUrl(url)

        val itc = object : InterConnect{


            override fun submitForm(it:String): String {

                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)
                val button = jsonObject.get("button").asString.trim()
                var msg = ""
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == 100) {
            val result = WebChromeClient.FileChooserParams.parseResult(resultCode, intent)
            filePathCallback?.onReceiveValue(result)
            filePathCallback = null
        }
    }

}