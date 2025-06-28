package com.csi.irite.unuse

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.csi.irite.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_test_menu, container, false)


        val webView: WebView = view.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.allowUniversalAccessFromFileURLs = true // Allow CORS

        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webView.loadUrl("file:///android_asset/tables-datatables-basic.html")

        val jsonData = """[
                            {
                              "id": 1,
                              "avatar": "10.png",
                              "full_name": "Korrie O'Crevy",
                              "post": "Nuclear Power Engineer",
                              "email": "kocrevy0@thetimes.co.uk",
                              "city": "Krasnosilka",
                              "start_date": "09/23/2021",
                              "salary": "$23896.35",
                              "age": "61",
                              "experience": "1 Year",
                              "status": 2
                            },
                            {
                              "id": 2,
                              "avatar": "1.png",
                              "full_name": "Bailie Coulman",
                              "post": "VP Quality Control",
                              "email": "bcoulman1@yolasite.com",
                              "city": "Hinigaran",
                              "start_date": "05/20/2021",
                              "salary": "$13633.69",
                              "age": "63",
                              "experience": "3 Years",
                              "status": 2
                            }]"""
        webView.addJavascriptInterface(JavaScriptInterface(jsonData,activity), "Android")

        return view
    }

    private class JavaScriptInterface(private val jsonData: String, private val ctx: FragmentActivity?) {
        @android.webkit.JavascriptInterface
        fun getJsonData(): String {
            return jsonData
        }

        @JavascriptInterface
        fun submitForm(formData: String) {
            // Handle form data in Kotlin
            ctx!!.runOnUiThread {
                Toast.makeText(ctx, "Form submitted: $formData", Toast.LENGTH_LONG).show()
            }
        }
    }
}