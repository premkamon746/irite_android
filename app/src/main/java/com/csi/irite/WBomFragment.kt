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
import com.csi.irite.room.dao.BomReportDao
import com.csi.irite.room.data.BomReport
import com.csi.irite.room.data.EventReport
import com.csi.irite.unuse.DrawMapFragment
import com.csi.irite.utils.PrintFormService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject


class WBomFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_incident, container, false)

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.title = "ระเบิด"
        }

        val bomReportDao: BomReportDao = db!!.bomReportDao()
        val existing = bomReportDao.getBomReportByEventId(uid)

        val eventReportDao: EventReportDao = this.db!!.eventReportDao()
        val eventReports: EventReport = eventReportDao.loadAllByIds(uid)

        if (existing == null) {
            val bomReport = BomReport(
                event_report_id = uid,
                notificationMethod = eventReports.report_channel.toString(),
                investigator = eventReports.report_officer.toString(),
                phoneNumber = eventReports.report_officer_contact_no.toString(),
                incidentLocation = eventReports.report_place.toString(),
                victim_incident_date = eventReports.report_known_date.toString(),
                victim_incident_time = eventReports.report_known_time,
            )
            bomReport.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
            bomReport.createdat = System.currentTimeMillis()
            bomReport.updatedat = System.currentTimeMillis()
            bomReport.uid = System.currentTimeMillis()
            bomReportDao.insertBomReport(bomReport)
        }

        //val allEvent = eventReportDao.getAll()


        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        webView.loadUrl("file:///android_asset/w-bom.html")
        var interConUid = uid

        val itc = object : InterConnect{

            @RequiresApi(Build.VERSION_CODES.O)
            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)

                val bomReportDao: BomReportDao = db!!.bomReportDao()

                Log.d("WebView----+", jsonObject.toString())
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
                        val query = buildUpdateQuery("BomReport",
                            id = interConUid,
                            key = key,
                            value = v
                        )
                        if (query != null) {
                            try {
                                bomReportDao.updateField(query)
                            }catch (e:Exception){}
                        }
                    }

                    Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                }else if(button == "update_list"){
                    jsonObject.remove("update_list")
                    val field = jsonObject.get("field").asString.trim()
                    val query = buildUpdateQuery("BomReport",
                        id = interConUid,
                        key = field,
                        value = jsonObject["data"].toString()
                    )
                    if (query != null) {
                        try {
                            bomReportDao.updateField(query)
                        }catch (e:Exception){
                            Log.d("WebView",e.toString())
                        }
                    }
                }else if(button == "single"){

                    val query = buildUpdateQuery("BomReport", id = interConUid, key = jsonObject.get("fieldName").toString(), value = jsonObject.get("fieldValue").asString)
                    if (query != null) {
                        bomReportDao.updateField(query)
                    }
                    //Log.d("WebView",jsonObject.toString())
                    Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                }  else if (button == "delete") {
                    //setLoading()
                    val evid = jsonObject.get("uid").asLong
                    var updatedat = System.currentTimeMillis()
                    eventReportDao.updateStatus(evid, false, updatedat)
                }else if (button == "print"){
                    val uid = jsonObject.get("uid").asString.toLong()
                    var print = PrintFormService(requireContext())

                    var evrs = db!!.eventReportDao()
                    var eventReport = evrs.loadAllByIds(uid)

                    var evb = db!!.evidenceDao()
                    var evident = evb.findEventId(uid.toLong())

                    //print.generatePdfAndPrint(eventReport, evident, requireContext())
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
                    val bomReportDao: BomReportDao = db!!.bomReportDao()
                    val bomReport = bomReportDao.getBomReportByEventId(interConUid)
                    returnData = gson.toJson(bomReport)
                }else if(func == "getDataTable"){
                    val allEvent = eventReportDao.getAllActiveStatus()
                    returnData = gson.toJson(allEvent)
                    //stopLoading()
                }else if(func == "getByID"){
                    val event = eventReportDao.loadAllByIds(ref.toLong())
                    returnData = gson.toJson(event)
                }else if (func == "assets"){
                    var event_report_id = option
                    loadFragment(AssetFormFragment(),event_report_id.toLong())
                }else if (func == "map"){
                    var event_report_id = option
                    loadFragment(DrawMapFragment(),event_report_id.toLong())
                }
                //Log.d("WebView----", returnData)
                return returnData
            }
        }

        webView.addJavascriptInterface(JavaScriptInterface(activity ,itc ), "Android")
        return view
    }



}