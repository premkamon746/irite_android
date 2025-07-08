package com.csi.irite

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.csi.irite.datainit.InitCheckList
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.ChecklistSave
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.InspectionRecord
import com.google.gson.Gson
import com.google.gson.JsonObject

class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home2, container, false)

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.title = ""
        }

        webView = view.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webSetting(webView)
        webView.loadUrl("file:///android_asset/app-calendar.html")
        val chlst = db?.let { InitCheckList(it) }


        val eventReportDao: EventReportDao = this.db!!.eventReportDao()

        val chSave = db?.checklistSaveDao()

        var checklistSaveDao: List<ChecklistSave>? = chSave?.getAll()
        //val gson = Gson()
        //var jsonData = gson.toJson(checklistSaveDao)
        //Log.d("check list save", jsonData)

        var itc = object : InterConnect{

            @SuppressLint("SuspiciousIndentation")
            @RequiresApi(Build.VERSION_CODES.O)
            val inspection = db?.inspectionRecordDao()

            @SuppressLint("SuspiciousIndentation")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)

                var dataReturn = ""
                val button = jsonObject.get("button").asString.trim()
                if(button == "multiple"){ // if(button == "add-checklist")
                    jsonObject.remove("button")
                    val team = jsonObject.get("team").asString.trim()
                    val create_date = jsonObject.get("create_date").asString.trim()
                    //Log.d("add-checklist","team: $team ,create_date: $create_date")
                    var clID = inspection?.getIdIfExists(create_date, team)
                    if( clID == null ){
                        val ispRrd = InspectionRecord()
                        ispRrd.createdat = System.currentTimeMillis()
                        ispRrd.team = team.toInt()
                        ispRrd.create_date = create_date
                        ispRrd.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
                        ispRrd.uid = System.currentTimeMillis()
                        inspection?.insertAll(ispRrd)
                        clID = inspection?.getIdIfExists(create_date, team)
                    }

                    jsonObject.entrySet().forEach { (key, value) ->
                        val v = value.asString
                        val query = buildUpdateQuery("InspectionRecord",
                                id = clID!!,
                                key = key,
                                value = v,
                                whereId = "uid"
                            )

                        if (query != null) {
                            var updateResult = inspection?.updateField(query)
                            //Log.d("json", "-------------"+ updateResult.toString())
                        }
                    }

                    //val checklist = inspection?.getById(clID)
                    //dataReturn = gson.toJson(checklist)


                    Toast.makeText(requireContext(), "บันทึกเรียบร้อย", Toast.LENGTH_LONG).show()
                    return clID.toString()
                }
                return dataReturn
            }

            override fun getUpdateData():String {
                return ""
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getJsonData(ref: String, func:String, option:String): String {
                var jsonData = ""
                val gson = Gson()


                Log.d("WebView----", ref+" "+option)
                if(func == "addEvent"){
                    loadFragment(IncidentFragment(),uid)
                }else if(func == "retrieveChecklist"){
                    val uid = option.toLong()
                    val checklist = inspection?.getById(uid)
                    jsonData = gson.toJson(checklist)
                }else if(func == "getEvent"){
                    val eventReports: List<EventReport> = eventReportDao.getAll()
                    jsonData = gson.toJson(eventReports)
                }else if(func == "getChecklist"){
                    /*val type = object : TypeToken<Map<String, Any>>() {}.type
                    val jsonMap: Map<String, Any> = gson.fromJson(option, type)
                    var checklist = inspection?.getDateTeam(jsonMap["current_date"].toString(),
                        jsonMap["team"].toString()
                    )
                    jsonData = gson.toJson(checklist)*/
                    val checklist = inspection?.getAllChecklist()
                    jsonData = gson.toJson(checklist)
                }else if (func == "loadReport"){
                    var uid = option.toLong()
                    var eventReport = eventReportDao.loadAllByIds(uid)
                    var report_type = eventReport.report_type.toString()
                    if(report_type == "ทรัพย์"){
                        loadFragment(WAssetsFragment(),eventReport.uid)
                    }else if(report_type == "ระเบิด"){
                        loadFragment(WBomFragment(),eventReport.uid)
                    }else if(report_type == "เพลิงไหม้"){
                        loadFragment(WFireFragment(),eventReport.uid)
                    }else{
                        loadFragment(WLifeFragment(),eventReport.uid)
                    }
                }
                return jsonData
            }

        }
        webView.addJavascriptInterface(JavaScriptInterface(activity,itc), "Android")

        return view
    }

}