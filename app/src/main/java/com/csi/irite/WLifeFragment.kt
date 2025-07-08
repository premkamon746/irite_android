package com.csi.irite

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.dao.LifeReportDao
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.LifeReport
import com.csi.irite.unuse.DrawMapFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject


class WLifeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_incident, container, false)

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.title = "ชีวิต"
        }

        val lifeReportDao: LifeReportDao = db!!.lifeReportDao()
        val existing = lifeReportDao.getLifeReportByEventId(uid)

        val eventReportDao: EventReportDao = this.db!!.eventReportDao()
        val eventReports: EventReport = eventReportDao.loadAllByIds(uid)

        if (existing == null) {
            val lifeReport = LifeReport(
            event_report_id = uid,
            notification_method = eventReports.report_channel.toString(),
            investigator = eventReports.report_officer.toString(),
            phone_number = eventReports.report_officer_contact_no.toString(),
            incident_location = eventReports.report_place.toString(),
            date = eventReports.report_known_date.toString(),
            time_approx = eventReports.report_known_time,
            police_station = eventReports.report_from_where,
            other_details1 = eventReports.report_preinfo
            )
            lifeReport.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
            lifeReport.createdat = System.currentTimeMillis()
            lifeReport.updatedat = System.currentTimeMillis()
            lifeReport.uid = System.currentTimeMillis()
            lifeReportDao.insertLifeReport(lifeReport)
        }
        //val allEvent = eventReportDao.getAll()

        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        webView.loadUrl("file:///android_asset/w-life.html")
        var interConUid = uid

        val itc = object : InterConnect{

            @RequiresApi(Build.VERSION_CODES.O)
            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)

                val lifeReportDao: LifeReportDao = db!!.lifeReportDao()

                //Log.d("WebView----+", jsonObject.toString())
                val button = jsonObject.get("button").asString.trim()
                if(button == "add-evident"){
                    val uid = jsonObject.get("uid").asString.toLong()
                }else if(button == "multiple"){
                    jsonObject.remove("button")
                    jsonObject.entrySet().forEach { (key, value) ->
                        if (key == "owner_list") return@forEach
                        var v = ""
                        try {
                            if(value.isJsonArray){
                                v = value.toString()
                            }else{
                                v = value.asString
                            }
                        } catch (e: Exception) {
                            v = value.toString()
                        }

                        val query = buildUpdateQuery("LifeReport",
                            id = interConUid,
                            key = key,
                            value = v
                        )
                        if (query != null) {
                            try {
                                lifeReportDao.updateField(query)
                            } catch (e: Exception) {
                            }
                        }
                    }

                }else if(button == "update_list"){
                    jsonObject.remove("update_list")
                    val field = jsonObject.get("field").asString.trim()
                    val query = buildUpdateQuery("LifeReport",
                        id = interConUid,
                        key = field,
                        value = jsonObject["data"].toString()
                    )
                    if (query != null) {
                        try {
                            lifeReportDao.updateField(query)
                        }catch (e:Exception){}
                    }
                }else if(button == "single"){
                    Log.d("WebView","multi+++++++++++++++++++++++++")
                    val query = buildUpdateQuery("LifeReport", id = interConUid, key = jsonObject.get("fieldName").toString(), value = jsonObject.get("fieldValue").asString)
                    if (query != null) {
                        lifeReportDao.updateField(query)
                    }
                    Log.d("WebView",jsonObject.toString())
                    Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                }  else if (button == "delete") {
                    //setLoading()
                    val evid = jsonObject.get("uid").asLong
                    val updatedat = System.currentTimeMillis()
                    eventReportDao.updateStatus(evid, false, updatedat)
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
                if(func == "getFormData") {
                    val lifeReportDao: LifeReportDao = db!!.lifeReportDao()
                    val lifeReport = lifeReportDao.getLifeReportByEventId(interConUid)
                    returnData = gson.toJson(lifeReport)
                }else if(func == "getDataTable"){
                    val allEvent = eventReportDao.getAllActiveStatus()
                    returnData = gson.toJson(allEvent)
                    //stopLoading()
                }else if(func == "getByID"){
                    val event = eventReportDao.loadAllByIds(ref.toLong())
                    returnData = gson.toJson(event)
                }else if (func == "assets"){
                    var event_report_id = option
                    //Log.d("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",event_report_id)
                    loadFragment(AssetFormFragment(),event_report_id.toLong())
                }else if (func == "map"){
                    var event_report_id = option
                    loadFragment(DrawMapFragment(),event_report_id.toLong())
                }
                Log.d("WebView----", returnData)
                return returnData
            }
        }

        webView.addJavascriptInterface(JavaScriptInterface(activity ,itc ), "Android")
        return view
    }


}