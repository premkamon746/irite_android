package com.csi.irite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.dao.FireReportDao
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.FireReport
import com.csi.irite.unuse.DrawMapFragment
import com.csi.irite.utils.PrintFormService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject


class WFireFragment : BaseFragment() {

    private var existing: FireReport? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_incident, container, false)

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.title = "เพลงไหม้"
        }

        val fireReportDao: FireReportDao = db!!.fireReportDao()
        existing = fireReportDao.getFireReportByEventId(uid)

        val eventReportDao: EventReportDao = this.db!!.eventReportDao()
        val eventReports: EventReport = eventReportDao.loadAllByIds(uid)

        if (existing == null) {
            val fireReport = FireReport(
                event_report_id = uid,
                police_station = eventReports.report_from_where.toString(),
                notification_other_details = eventReports.report_channel_other.toString(),
                notification_method = eventReports.report_channel.toString(),
                investigator_name = eventReports.report_officer.toString(),
                phone_number = eventReports.report_officer_contact_no.toString(),
                incident_location = eventReports.report_place.toString(),
                incident_date = eventReports.report_known_date.toString(),
                approximate_time = eventReports.report_known_time,
            )
            fireReport.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
            fireReport.createdat = System.currentTimeMillis()
            fireReport.updatedat = System.currentTimeMillis()
            fireReportDao.insertFireReport(fireReport)
        }

        //Log.d(uid)

        //val allEvent = eventReportDao.getAll()


        webView = view.findViewById(R.id.webView)
        webSetting(webView)
        webView.loadUrl("file:///android_asset/w-fire.html")

        var interConUid = uid

        val itc = object : InterConnect{

            override fun submitForm(it:String): String {

                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)
                //Log.d("RawQuery", "json: $it")
                //Log.d("WebView----+", jsonObject.toString())
                val button = jsonObject.get("button").asString.trim()
                if(button == "add-evident"){
                    val uid = jsonObject.get("uid").asString.toLong()
                }else if(button == "multiple"){
                    jsonObject.remove("button")
                    jsonObject.entrySet().forEach { (key, value) ->
                        //Log.d("RawQuery", "key: $key , value: $value")
                        val query = buildUpdateQuery("FireReport",
                            id = interConUid,
                            key = key,
                            value = value.toString()
                        )
                        fireReportDao.updateField(query)
                        Log.d("WebView----+", "key: $key , value: $value")
                    }

                    //Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                }else if(button == "update_list"){
                    jsonObject.remove("update_list")
                    val field = jsonObject.get("field").asString.trim()
                    val query = buildUpdateQuery("FireReport",
                        id = interConUid,
                        key = field,
                        value = jsonObject["data"].toString()
                    )
                    Log.d("json_export", "field "+field+" id:"+interConUid+" ")
                    fireReportDao.updateField(query)
                    Log.d("json_export", "field "+query.sql.toString())
                    Log.d("json_export", jsonObject["data"].toString())
                }else if(button == "single"){
                    val query = buildUpdateQuery("FireReport", id = interConUid, key = jsonObject.get("fieldName").toString(), value = jsonObject.get("fieldValue").toString())
                    val rowsUpdated = fireReportDao.updateField(query)
                    Log.d("RawQuery", "SQL = ${rowsUpdated}")
                    //Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                }else if (button == "delete") {
                    val evid = jsonObject.get("uid").asLong
                    val updatedat = System.currentTimeMillis()
                    eventReportDao.updateStatus(evid, false,updatedat)
                }else if (button == "print"){
                    val uid = jsonObject.get("uid").asString.toLong()
                    var print = PrintFormService(requireContext())

                    var evrs = db!!.eventReportDao()
                    var eventReport = evrs.loadAllByIds(uid)

                    var evb = db!!.evidenceDao()
                    var evident = evb.findEventId(uid.toLong())

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
                    val fireReportDao: FireReportDao = db!!.fireReportDao()
                    val fireReport = fireReportDao.getFireReportByEventId(interConUid)
                    returnData = gson.toJson(fireReport)
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
                return returnData
            }
        }

        webView.addJavascriptInterface(JavaScriptInterface(activity ,itc ), "Android")
        return view
    }

}