package com.csi.irite

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import com.csi.irite.room.database.AppDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Calendar
import java.util.Properties

open class BaseFragment : Fragment() {
    var uid: Long = 0
    var db: AppDatabase? = null
    protected lateinit var webView: WebView
    protected var mainActivity: MainActivity? = null
    var uploadCallback: ValueCallback<Array<Uri>>? = null
    private val REQUEST_SELECT_FILE = 1001



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    fun getURL():String{
        return BuildConfig.host
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            uid = it.getLong("uid")
            //Toast.makeText(requireContext(), "Failed to load content $uid", Toast.LENGTH_SHORT).show()

        }

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.title = ""
        }
        //og.d("WebView------------", "From line $uid")
        //Toast.makeText(requireContext(), "Failed to load content $uid", Toast.LENGTH_SHORT).show()
        db = Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        mainActivity?.hideProgressBar()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle configuration changes here if needed
    }

    fun webSetting(webView: WebView ){
        /*webView.settings.javaScriptEnabled = true

        webView.settings.mixedContentMode = webView.settings.mixedContentMode
        //WebView.setWebContentsDebuggingEnabled(true)
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true*/
        webView.settings.domStorageEnabled = true
        webView.settings.allowUniversalAccessFromFileURLs = true // Allow CORS
        WebView.setWebContentsDebuggingEnabled(true)


        webView.settings.javaScriptEnabled = true // Enable JavaScript
        webView.settings.domStorageEnabled = true // Enable DOM storage
        webView.settings.setSupportMultipleWindows(true) // Support for opening new windows
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }*/


        webView.webChromeClient = object : WebChromeClient() {
            @Deprecated("Deprecated in Java")
            override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
                Log.d("WebView", "$message -- From line $lineNumber of $sourceID")
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                uploadCallback = filePathCallback
                val intent = fileChooserParams?.createIntent()
                try {
                    if (intent != null) {
                        startActivityForResult(intent, REQUEST_SELECT_FILE)
                    }
                } catch (e: Exception) {
                    uploadCallback?.onReceiveValue(null)
                    uploadCallback = null
                    return false
                }
                return true
            }

        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Show a loading indicator here
                //progressBar.visibility = View.VISIBLE
                mainActivity?.showProgressBar()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide the loading indicator here
                //progressBar.visibility = View.GONE
                //Toast.makeText(requireContext(), url, Toast.LENGTH_SHORT).show()
                mainActivity?.hideProgressBar()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                // Handle error
                Toast.makeText(requireContext(), "Failed to load content", Toast.LENGTH_SHORT).show()
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
                Log.d("WebView", "$message -- From line $lineNumber of $sourceID")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_FILE) {
            val results = WebChromeClient.FileChooserParams.parseResult(resultCode, data)
            uploadCallback?.onReceiveValue(results)
            uploadCallback = null
        }
    }



    fun confirmDialog(){
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Confirm Delete")
        builder.setMessage("Are you sure you want to delete?")

        builder.setPositiveButton("Yes") { dialog, which ->
            // Delete code here (e.g., delete from database)
            // Dismiss the dialog
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    /*fun setLoading() {
        val weakActivity = context as MainActivity
        if (weakActivity != null){
            val lyt_progress = weakActivity.findViewById<View>(R.id.lyt_progress) as LinearLayout
            lyt_progress.visibility = View.VISIBLE
            lyt_progress.setAlpha(1.0f)
        }
    }

    fun stopLoading() {
        try {
            val weakActivity = context as MainActivity
            if (weakActivity != null){
                val lyt_progress = weakActivity.findViewById<View>(R.id.lyt_progress) as LinearLayout
                lyt_progress.visibility = View.GONE
                lyt_progress.setAlpha(1.0f)
            }
        } catch (e: Exception) {}
    }*/

    fun saveComplete(){
        Toast.makeText(context, "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show()
    }

    protected class JavaScriptInterface(private val ctx: FragmentActivity?
                                      , private val itc: InterConnect
    ) {
        @JavascriptInterface
        fun getJsonData(id: String, func: String, option:String): String {
            return itc.getJsonData(id,func,option)
        }

        @JavascriptInterface
        fun submitForm(formData: String): String  {
            // Handle form data in Kotlin
            //ctx!!.runOnUiThread {
                return itc.submitForm(formData)
            //}
        }

        @JavascriptInterface
        fun getUpdateData(): String {
            return itc.getUpdateData()
        }

    }

    interface InterConnect{
        fun submitForm(formData: String): String
        fun getUpdateData():String

        fun getJsonData(ref:String, func:String, option:String):String
    }

    protected  fun loadFragment(fragment: Fragment,uid: Long = 0){
        val args = Bundle()
        args.putLong("uid", uid) // Example parameter: key-value pair
        fragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    protected  fun loadFragmentNoStack(fragment: Fragment,uid: Long = 0){
        val args = Bundle()
        args.putLong("uid", uid) // Example parameter: key-value pair
        fragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    fun convertDateToTimestamp(dateString: String?): Long? {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
            val date = dateFormat.parse(dateString.toString())
            date?.time
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val webView = getView()?.findViewById<WebView>(R.id.webView)
        if (webView != null) {
            webView.clearCache(true)
            //Toast.makeText(requireContext(), "onDestroyView", Toast.LENGTH_LONG).show()
            webView.loadUrl("about:blank")
            webView.clearHistory()
            webView.removeAllViews()
            webView.destroy()
        }
    }

    fun getStartOfDayInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getEndOfDayInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    fun printType(value: Any?) {
        if (value == null) {
            println("Type: null")
        } else {
            println("Type: ${value::class.simpleName}")
        }
    }

    fun parseTypedValue(value: String?): Any {
        val v = value?.trim() ?: return ""

        return when {
            v.equals("true", ignoreCase = true) -> 1
            v.equals("false", ignoreCase = true) -> 0
            v.toIntOrNull() != null -> v.toInt()
            v.toDoubleOrNull() != null -> v.toDouble()
            else -> v
        }
    }

    fun autoCast(value: String): Any {
        return value.toIntOrNull()
            ?: value.toDoubleOrNull()
            ?: value // leave as string
    }


    fun buildUpdateQuery(table: String, id: Long, key: String, value: String, whereId: String? = "event_report_id"): SimpleSQLiteQuery? {
        //ignore field


        val time = System.currentTimeMillis()
        /*val booleanValue = when (value.toString().lowercase()) {
            "true" -> 1
            "false" -> 0
            else -> value
        }/**/

        val safeValue = when (booleanValue) {
            is String, is Int, is Long, is Double, is Float,
            is Short, is Byte, null -> booleanValue
            else -> booleanValue.toString()
        }*/


        val safeValue = parseTypedValue(value)

        val queryStr = "UPDATE $table SET $key = ?, updatedat = ? WHERE $whereId = ?"

        val args = arrayOf(safeValue, time, id)
        return SimpleSQLiteQuery(queryStr, args)
    }

    fun getParam(context: Context, param: String): String {
        val properties = Properties()
        val inputStream = context.assets.open("config.properties")
        properties.load(inputStream)
        return properties.getProperty(param)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return currentDate.format(formatter)
    }

}