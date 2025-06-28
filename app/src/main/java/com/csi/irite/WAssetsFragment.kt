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
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.dao.AssetReportDao
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.AssetReport
import com.csi.irite.room.data.EventReport
import com.csi.irite.unuse.DrawMapFragment
import com.csi.irite.utils.PrintFormService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.json.JSONArray


class WAssetsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_incident, container, false)

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.title = "ทรัพย์"
        }

        val assetReportDao: AssetReportDao = db!!.assetReportDao()
        val existing = assetReportDao.getAssetReportByEventId(uid)
        val eventReportDao: EventReportDao = this.db!!.eventReportDao()
        val eventReports: EventReport = eventReportDao.loadAllByIds(uid)

        if (existing == null) {
            var assetReport = AssetReport(
                event_report_id = uid,
                report_method = eventReports.report_channel.toString(),
                investigator = eventReports.report_officer.toString(),
                investigator_phone = eventReports.report_officer_contact_no.toString(),
                incident_location = eventReports.report_place.toString(),
                victim_incident_date = eventReports.report_known_date.toString(),
                victim_incident_time = eventReports.report_known_time,
            )

            assetReport.updatedat = System.currentTimeMillis()
            assetReport.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
            assetReport.createdat = System.currentTimeMillis()
            assetReport.updatedat = System.currentTimeMillis()
            assetReportDao.insertIncidentReport(assetReport)
        }

        //Log.d(uid)


        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        webView.loadUrl("file:///android_asset/w-asset.html")
        var interConUid = uid

        val itc = object : InterConnect{

            @RequiresApi(Build.VERSION_CODES.O)
            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)

                val assetReportDao: AssetReportDao = db!!.assetReportDao()

                Log.d("WebView","---------------------------")
                Log.d("WebView",jsonObject.toString())
                Log.d("WebView","---------------------------")
                //assetReportDao.getAssetReportByEventId(uid)

                //Log.d("WebView----+", jsonObject.toString())
                val button = jsonObject.get("button").asString.trim()
                if(button == "add-evident"){
                    val uid = jsonObject.get("uid").asString.toLong()
                }else if(button == "multiple"){
                    jsonObject.remove("button")

                    jsonObject.entrySet().forEach { (key, value) ->
                        //val value = jsonObject.get(key)
                        val query = buildUpdateQuery("AssetReport",
                            id = interConUid,
                            key = key,
                            value = value
                        )


                        Log.d("json_export", "field  id:"+interConUid+" ")
                        assetReportDao.updateField(query)
                        Log.d("json_export", "field "+query.sql)
                        Log.d("json_export", jsonObject["data"].toString())

                    }

                    var ass = assetReportDao.getAssetReportByEventId(interConUid)
                    var gg = gson.toJson(ass)
                    Log.d("WebView","++++++++++")
                    Log.d("WebView",gg.toString())
                    Log.d("WebView","++++++++++")


                    Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                }else if(button == "single"){
                    val query = buildUpdateQuery("AssetReport", id = interConUid, key = jsonObject.get("fieldName").toString(), value = jsonObject.get("fieldValue").toString())
                    assetReportDao.updateField(query)
                    Log.d("WebView",jsonObject.toString())
                    Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                }  else if (button == "delete") {
                    //setLoading()
                    val evid = jsonObject.get("uid").asLong
                    val updatedat = System.currentTimeMillis()
                    eventReportDao.updateStatus(evid, false, updatedat)
                }else if (button == "print"){
                    val uid = jsonObject.get("uid").asString.toLong()
                    var print = PrintFormService(requireContext())

                    var evrs = db!!.eventReportDao()
                    var eventReport = evrs.loadAllByIds(uid)

                    var evb = db!!.evidenceDao()
                    var evident = evb.findEventId(uid.toLong())

                    //print.generatePdfAndPrint(eventReport, evident,requireContext())
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
                    val assetReportDao: AssetReportDao = db!!.assetReportDao()
                    val assetReport = assetReportDao.getAssetReportByEventId(interConUid)
                    returnData = gson.toJson(assetReport)
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