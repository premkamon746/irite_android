package com.csi.irite

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.csi.irite.room.dao.DistrictDao
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.EventReport
import com.csi.irite.sync.RetrofitInstance
import com.csi.irite.unuse.DrawMapFragment
import com.csi.irite.utils.PrintFormService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.io.File


class IncidentFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_incident, container, false)


        val eventReportDao: EventReportDao = db!!.eventReportDao()
        val allEvent = eventReportDao.getAll()
        /*val jsonData = """ [{ "id": 1, "name": "System Architect", "date": "Edinburgh", "test": "xxxxx" },
                            { "id": 3, "name": "System Architect3", "date": "Edinburgh4", "test": "xxxxx2" }]"""*/
        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        webView.loadUrl("file:///android_asset/incident.html")

        var currentId = uid
        val itc = object : InterConnect{

            @RequiresApi(Build.VERSION_CODES.O)
            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)
                //Log.d("WebView----+", jsonObject.toString())
                val button = jsonObject.get("button").asString.trim().trim()
                if(button == "add-evident"){
                    val uid = jsonObject.get("uid").asString.trim().toLong()
                    loadFragment(EvidentFragment(), uid)
                }else if(button == "upload"){
                    val uid = jsonObject.get("uid").asString.trim().toLong()
                    loadFragment(WUploadFragment(), uid)
                }else if(button == "incidentForm"){
                    //for keep value would not update
                    var eventReport = EventReport()
                    if(jsonObject.get("uid").asString.trim() != "") {
                        val uid = jsonObject.get("uid").asLong
                        eventReport = eventReportDao.loadAllByIds(uid)

                    }else{
                        eventReport.status = true
                        eventReport.createdat = System.currentTimeMillis()
                        eventReport.updatedat = System.currentTimeMillis()
                        eventReport.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
                    }

                    eventReport.head = jsonObject.get("head").asString.trim()
                    eventReport.report_number = jsonObject.get("report_number").asString.trim()
                    eventReport.report_write = jsonObject.get("report_write").asString.trim()

                    //val create_datetime = convertDateToTimestamp(jsonObject.get("create_datetime").asString.trim())
                    //eventReport.create_datetime = create_datetime
                    eventReport.create_date = jsonObject.get("create_date").asString.trim()
                    eventReport.create_time = jsonObject.get("create_time").asString.trim()

                    eventReport.report_from_where = jsonObject.get("report_from_where").asString.trim()
                    eventReport.report_channel = jsonObject.get("report_channel").asString.trim()
                    eventReport.report_channel_other = jsonObject.get("report_channel_other").asString.trim()

                    eventReport.report_type = jsonObject.get("report_type").asString.trim()
                    eventReport.report_type_other = jsonObject.get("report_type_other").asString.trim()
                    eventReport.report_place = jsonObject.get("report_place").asString.trim()
                    eventReport.report_province = jsonObject.get("report_province").asString.trim()
                    eventReport.report_distinct = jsonObject.get("report_distinct").asString.trim()

                    eventReport.report_known_date = jsonObject.get("report_known_date").asString.trim()
                    eventReport.report_known_time = jsonObject.get("report_known_time").asString.trim()

                    eventReport.report_preinfo = jsonObject.get("report_preinfo").asString.trim()

                    eventReport.report_officer = jsonObject.get("report_officer").asString.trim()
                    eventReport.report_officer_contact_no = jsonObject.get("report_officer_contact_no").asString.trim()
                    eventReport.report_sufferer = jsonObject.get("report_sufferer").asString.trim()
                    eventReport.report_sufferer_contact_no = jsonObject.get("report_sufferer_contact_no").asString.trim()

                    if(jsonObject.get("uid").asString.trim() == "") {

                        lifecycleScope.launch {
                            val running = getRunningNumber(eventReport.report_type!!)
                            eventReport.report_number = running
                            eventReport.updatedat = System.currentTimeMillis()
                            eventReportDao.insertAll(eventReport)
                        }

                    }else{

                        eventReport.uid = jsonObject.get("uid").asLong
                        eventReport.updatedat = System.currentTimeMillis()
                        if (eventReport.report_number.toString() == "null") {
                            lifecycleScope.launch {
                                val running = getRunningNumber(eventReport.report_type!!)
                                Log.d("_Login"," running :$running")
                                eventReport.report_number = running
                                eventReport.updatedat = System.currentTimeMillis()
                                eventReportDao.updateAll(eventReport)
                            }
                        }else{
                            eventReportDao.updateAll(eventReport)
                        }

                    }
                    Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                } else if (button == "delete") {
                    //setLoading()
                    val evid = jsonObject.get("uid").asLong
                    val updatedat = System.currentTimeMillis()
                    eventReportDao.updateStatus(evid, false, updatedat)
                }else if (button == "print"){
                    val uid = jsonObject.get("uid").asString.trim().toLong()
                    var print = PrintFormService(requireContext())

                    var eventReport = eventReportDao.loadAllByIds(uid)
                    //Log.d("xxxxxxxxxxxxxxxxxxxxxx",eventReport.toString())

                    var evb = db!!.evidentBagDao()
                    var evidentBag = evb.findEventId(uid)
                    //Log.d("xxxxxxxxxxxxxxxxxxxxxx",evident.count().toString())

                    print.generatePdfAndPrint(eventReport, evidentBag,requireContext())
                }
                return ""
            }

            override fun getUpdateData():String {
                return ""
            }

            override fun getJsonData(ref: String, func:String,option:String): String {
                var returnData = ""
                var event_report_id = option
                val gson = GsonBuilder()
                    .serializeNulls() // Include null fields
                    .setPrettyPrinting() // Optional: for pretty printing
                    .create()
                if(func == "getDistinct") {
                    val districtDao: DistrictDao = db!!.districtDao()
                    val dists = districtDao.getlDistrictsByProvince(ref)
                    returnData = gson.toJson(dists)
                }else if(func == "getDataTable"){
                    val allEvent = eventReportDao.getAllActiveStatus()
                    returnData = gson.toJson(allEvent)
                    //stopLoading()
                }else if(func == "getByID"){
                    val event = eventReportDao.loadAllByIds(ref.toLong())
                    returnData = gson.toJson(event)
                }else if (func == "loadReport"){
                    var eventReport = eventReportDao.loadAllByIds(event_report_id.toLong())
                    var report_type = eventReport.report_type.toString()
                    if(report_type == "ทรัพย์"){
                        loadFragment(WAssetsFragment(),event_report_id.toLong())
                    }else if(report_type == "ระเบิด"){
                        loadFragment(WBomFragment(),event_report_id.toLong())
                    }else if(report_type == "เพลิงไหม้"){
                        loadFragment(WFireFragment(),event_report_id.toLong())
                    }else{
                        loadFragment(WLifeFragment(),event_report_id.toLong())
                    }
                }else if (func == "map"){
                    loadFragment(DrawMapFragment(),event_report_id.toLong())
                }else if (func == "evidence"){
                    loadFragment(DrawEvidenceFragment(),event_report_id.toLong())
                }else if (func == "location"){
                    loadFragment(MapsforgeFragment(),event_report_id.toLong())
                }else if (func == "download_report"){
                    var eventReport = eventReportDao.loadAllByIds(event_report_id.toLong())
                    var report_type = eventReport.report_type.toString()
                    if(report_type == "ทรัพย์"){
                        getpdf("http://103.76.181.219:3400/pdf/assets-report")
                    }else if(report_type == "ระเบิด"){
                        getpdf("http://103.76.181.219:3400/pdf/bomb-report")
                    }else if(report_type == "เพลิงไหม้"){
                        getpdf("http://103.76.181.219:3400/pdf/fire-report")
                    }else{
                        getpdf("http://103.76.181.219:3400/pdf/lift-report")
                    }
                }
                Log.d("WebView----", returnData)
                return returnData
            }
        }

        webView.addJavascriptInterface(JavaScriptInterface(activity ,itc ), "Android")
        return view
    }

    fun getpdf(url:String){
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Report")
            .setDescription("Downloading PDF")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "report.pdf")

        val dm = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "report.pdf")
        val uri = FileProvider.getUriForFile(requireContext(), "com.csi.irite.fileprovider", file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        requireContext().startActivity(intent)
    }

    suspend fun getRunningNumber(report_type:String) :String{
        var group = ""
        if(report_type  == "ทรัพย์"){
            group = "ท"
        }else if(report_type  == "ระเบิด"){
            group = "ร"
        }else if(report_type  == "เพลิงไหม้"){
            group = "w"
        }else{
            group = "ช"
        }

        if (isNetworkAvailable(requireContext())) {
            val response = RetrofitInstance.api.getSyncRunningNumber(group)
            val body = response.body()
            val number = body?.running_number  // ← Specific field
            return number.toString()
        }else{
            return ""
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For Android 10 (API level 29) and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            // For lower versions of Android
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}