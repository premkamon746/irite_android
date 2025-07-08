package com.csi.irite

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.csi.irite.room.data.Evidence
import com.csi.irite.room.data.EvidentBag
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * A simple [Fragment] subclass.
 * Use the [TestMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EvidentFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_evident, container, false)

        var evidenceDao = db!!.evidenceDao()
        val allEvidence = evidenceDao.getByIds(uid, true)


        webView = view.findViewById(R.id.webView)
        webSetting(webView)

        webView.loadUrl("file:///android_asset/evident.html")

        var itc = object : InterConnect {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)
                val action = jsonObject.get("action").asString.trim()

                if (action == "save" || action == "update") {
                    val evident = jsonObject.get("evident").asString.trim()
                    val evident1 = jsonObject.get("evident1").asString.trim()
                    val evident2 = jsonObject.get("evident2").asString.trim()
                    val evident3 = jsonObject.get("evident3").asString.trim()
                    val evident4 = jsonObject.get("evident4").asString.trim()
                    val angle = jsonObject.get("angle").asString.trim()
                    val remark = jsonObject.get("remark").asString.trim()

                    var evBag = Evidence()
                    if (action == "update") {
                        evBag = evidenceDao.loadAllById(uid)
                        evBag.updatedat = System.currentTimeMillis()
                    }

                    evBag.event_report_id = uid
                    evBag.evidence = evident
                    evBag.distance_1 = evident1
                    evBag.distance_2 = evident2
                    evBag.distance_3 = evident3
                    evBag.distance_4 = evident4
                    evBag.angle = angle
                    evBag.remark = remark

                    if (action == "save") {
                        evBag.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
                        evBag.createdat = System.currentTimeMillis()
                        evBag.updatedat = System.currentTimeMillis()
                        evBag.uid = System.currentTimeMillis()
                        evidenceDao.insertAll(evBag)
                    } else if (action == "update") {
                        evBag.updatedat = System.currentTimeMillis()
                        evidenceDao.updateAll(evBag)
                    }
                } else if (action == "delete") {
                    val evid = jsonObject.get("uid").asLong
                    var updatedat = System.currentTimeMillis()
                    evidenceDao.updateStatus(evid, false, updatedat)
                }else if (action == "clone") {
                    val selectedCheckboxes = jsonObject.get("clone").asString.trim()
                    val evd = db?.evidenceDao()
                    val evdBag = db?.evidentBagDao()

                    if(selectedCheckboxes.length > 0) {
                        var selArr = selectedCheckboxes.trim().split(",")
                        val longArray: LongArray = selArr.map { it.toLong() }.toLongArray()
                        //val selList: List<String> = selArr.toList()
                        var evds = evd?.getSomeChecked(longArray)
                        //last seq no

                        if (evds != null) {
                            for (ev in evds) {
                                var evident_id = evdBag?.getByEvidentIds(ev.uid)
                                if (evident_id == ev.uid) continue
                                var seq_no = evdBag?.getLastSeqNO(uid)
                                seq_no?.plus(1)
                                var evb = EvidentBag(
                                    evident_id = ev.uid,
                                    seq_no = seq_no,
                                    event_report_id = ev.event_report_id,
                                    evident = ev.evidence
                                )
                                evb.refkey =  SyncIdManager.generateSyncIdWithTimestamp(requireContext())
                                val timeGen =  System.currentTimeMillis()
                                evb.uid = timeGen
                                evb.createdat = timeGen
                                evb.updatedat = timeGen
                                evdBag?.insertAll(evb)
                            }
                        }
                    }
                    loadFragment(EvidentBagFragment(),uid)
                }

                Toast.makeText(requireContext(), "อัพเดทข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_LONG).show()
                return ""
            }

            override fun getUpdateData():String {
                val allEvidence = evidenceDao.getByIds(uid, true)
                val gson = Gson()
                val jsonData = gson.toJson(allEvidence)
                return jsonData
            }

            override fun getJsonData(ref: String,func: String, option:String): String {
                val gson = Gson()
                val jsonData = gson.toJson(allEvidence)
                return jsonData
            }

        }

        webView.addJavascriptInterface(JavaScriptInterface(activity, itc), "Android")
        return view
    }

}