package com.csi.irite.helper

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.room.Room
import com.csi.irite.AssetFormFragment
import com.csi.irite.unuse.BomFormFragment
import com.csi.irite.unuse.FireFormFragment
import com.csi.irite.unuse.LifeFormFragment
import com.csi.irite.R
import com.csi.irite.unuse.ReportEventFormFragment
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.database.AppDatabase
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class EventReportHelper(val context: Context, val childFm: FragmentManager, val parentFm: FragmentManager) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createForm(view:View, eventReport: EventReport = EventReport(), layoutID:Int = 0){
        val mainLayout = view.findViewById<LinearLayout>(layoutID)
        val fromReport = UiViewHelper(context,view,childFm)

        val param1 = listOf(
            listOf("ปจว.ข้อ", eventReport.head.toString(), "edit", "head"),
            listOf("เลขรับ/เลขรายงาน", eventReport.report_number.toString(), "edit", "report_number")
        )
        val lin1 = fromReport.createFreeFlexSection(param1)
        mainLayout.addView(lin1)


        var localDate = LocalDateTime.now()

        if(eventReport.uid > 0){
            localDate = convertLongToDateTime(eventReport.create_datetime)
        }
        val today = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
        val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("th", "TH"))
        val yearWith543Added = (localDate.format(DateTimeFormatter.ofPattern("yyyy")).toInt() + 543).toString()
        val formattedTime = localDate.format(DateTimeFormatter.ofPattern("HH:mm"))

        val param2 = listOf(
            listOf("1. เขียนที่", "กสก ศพฐ 10", "edit", "report_write"),
            listOf("วันที่", today, "date", "oneDate"),
            listOf("เดือน", month, "edit", "oneMonth"),
            listOf("พ.ศ.", yearWith543Added, "edit","oneYear"),
            listOf("เวลา", formattedTime, "time","oneTime")
        )
        val lin2 = fromReport.createFreeFlexSection(param2)
        mainLayout.addView(lin2)

        val param3 = listOf(
            listOf("2. รับแจ้งจาก สน./สภ.", eventReport.report_from_where.toString(),"report_from_where")
        )
        val lin3 = fromReport.createFreeSection(param3)
        mainLayout.addView(lin3)



        val radioOption = listOf(
            "ทางหนังสือ",
            "ทางโทรศัพท์",
            "วิทยุสื่อสาร",
            "อื่นๆ",
        )

        val lin4 = fromReport.crateRadio(radioOption, "report_channel",eventReport.report_channel.toString(), false ,"", true, "report_channel_other")
        mainLayout.addView(lin4)

        //-------------------------------------------เหตุที่รับแจ้ง---------------------------------------------
        val lin5 = fromReport.createTextView("3. เหตุที่รับแจ้ง")
        mainLayout.addView(lin5)

        val radioOption2 = listOf(
            "ทรัพย์",
            "ชีวิต",
            "ระเบิด",
            "เพลิงไหม้",
            "จราจร",
            "ตรวจเก็บวัตถุพยาน",
            "อื่นๆ",
        )

        val lin6 = fromReport.crateRadio(radioOption2, "report_type",eventReport.report_type.toString(), false ,"", true,"report_type_other",eventReport.report_type_other.toString())
        mainLayout.addView(lin6)
        //----------------------------------------------------------------------------------------

        //------------------------------------------สถานที่เกิดเหตุ----------------------------------------------
        val param7 = listOf(
            listOf("4. สถานที่เกิดเหตุ",eventReport.report_place.toString(),"report_place")
        )
        val lin7 = fromReport.createFreeSection(param7)
        mainLayout.addView(lin7)
        //----------------------------------------------------------------------------------------


        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------



        var localDateKnow = LocalDateTime.now()
        if(eventReport.uid > 0){
            localDateKnow = convertLongToDateTime(eventReport.report_known_datetime)
        }

        val todayKnow = localDateKnow.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
        val timeKnow = localDateKnow.format(DateTimeFormatter.ofPattern("HH:mm"))

        val param8 = listOf(
            listOf("5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ", todayKnow, "date","dateEvent"),
            listOf("เวลาประมาณ", timeKnow, "time","timeEvent"),
        )
        val lin8 = fromReport.createFreeFlexSection(param8)
        mainLayout.addView(lin8)
        //----------------------------------------------------------------------------------------

        //------------------------------------------ข้อมูลเบื้องต้น----------------------------------------------
        val param9 = listOf(
            listOf("6. ข้อมูลเบื้องต้น",eventReport.report_preinfo.toString(),"report_preinfo")
        )
        val lin9 = fromReport.createFreeSection(param9)
        mainLayout.addView(lin9)
        //----------------------------------------------------------------------------------------

        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------
        val param10 = listOf(
            listOf("7. พนักงานสอบสวน", eventReport.report_sufferer.toString(), "edit","report_officer"),
            listOf("เบอร์โทรศัพท์ติดต่อ", eventReport.report_officer_contact_no.toString(), "edit","report_officer_contact_no"),
        )
        val lin10 = fromReport.createFreeFlexSection(param10)
        mainLayout.addView(lin10)
        //----------------------------------------------------------------------------------------

        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------
        val param11 = listOf(
            listOf("8. ผู้เสียหาย", eventReport.report_sufferer.toString(), "edit","report_sufferer"),
            listOf("เบอร์โทรศัพท์ติดต่อ", eventReport.report_sufferer_contact_no.toString(), "edit","report_sufferer_contact_no")
        )
        val lin11 = fromReport.createFreeFlexSection(param11)
        mainLayout.addView(lin11)/**/

        /*val paramX = listOf(
            listOf("", eventReport.report_sufferer.toString(), "edit","report_sufferer"),
            listOf("", eventReport.report_sufferer_contact_no.toString(), "edit","report_sufferer_contact_no")
        )
        val linX = fromReport.createFreeFlexSection(paramX)
        mainLayout.addView(linX)*/
        //----------------------------------------------------------------------------------------

        val save = fromReport.createSaveButton("บันทึก","saveReportEvent")
        mainLayout.addView(save)

        val btn = view.findViewWithTag<View>("saveReportEvent")


        btn.setOnClickListener(View.OnClickListener { v ->
            //if(eventReport.uid > 0)
            val eventReport = buildFormData(view,eventReport)
            retrieveForm(eventReport)
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun retrieveForm(eventReport: EventReport, editFlag:Boolean = false){

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_report_event, null)
        dialog.setContentView(view)
        dialog.setCancelable(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT


        val mainLayout = view.findViewById<LinearLayout>(R.id.retrieveReportEvent)
        val fromReport = UiViewHelper(context,view,childFm)

        val param1 = listOf(
            listOf("", "", "view", ""),
            listOf("", "", "view", ""),
            listOf("ปจว.ข้อ", eventReport.head.toString(), "text_bold", ""),
            listOf("เลขรับ/เลขรายงาน", eventReport.report_number.toString(), "text_bold", "")
        )
        val lin1 = fromReport.createFreeFlexSectionView(param1)
        mainLayout.addView(lin1)

        val create_date: LocalDateTime = convertLongToDateTime(eventReport.create_datetime)

        val oneDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(create_date)
        var oneMonth = create_date.month.getDisplayName(TextStyle.FULL, Locale("th", "TH"))
        val oneYear =  DateTimeFormatter.ofPattern("yyyy").format(create_date)
        var oneTime = DateTimeFormatter.ofPattern("HH:mm").format(create_date)

        val param2 = listOf(
            listOf("1. เขียนที่", eventReport.report_write.toString(), "text_bold", "oneWrite"),
            listOf("วันที่", oneDate, "text_bold", "oneDate"),
            listOf("เดือน", oneMonth, "text_bold", "oneMonth"),
            listOf("พ.ศ.", oneYear, "text_bold","oneYear"),
            listOf("เวลา", oneTime, "text_bold","oneTime")
        )
        val lin2 = fromReport.createFreeFlexSectionView(param2)
        mainLayout.addView(lin2)

        val param3 = listOf(
            listOf("2. รับแจ้งจาก สน./สภ.", eventReport.report_from_where.toString(),"text_bold","")
        )
        val lin3 = fromReport.createFreeFlexSectionView(param3)
        mainLayout.addView(lin3)


        val channel = if(eventReport.report_channel_other.toString() != "") eventReport.report_channel_other.toString()  else eventReport.report_channel.toString()
        val param4 = listOf(
            listOf("รับแจ้งทาง", channel,"text_bold","")
        )
        val lin4 = fromReport.createFreeFlexSectionView(param4)
        mainLayout.addView(lin4)

        /*val reportChannel = oview.findViewWithTag<RadioGroup>("report_channel")
        fromReport.retrieveRadio(eventReport.reportChannel,view,oview,"reportTypeRetrieve1")*/

        val type = if(eventReport.report_type_other.toString() != "") eventReport.report_type_other.toString()  else eventReport.report_type.toString()
        val param6 = listOf(
            listOf("3. เหตุที่รับแจ้ง", type,"text_bold","")
        )
        val lin6 = fromReport.createFreeFlexSectionView(param6)
        mainLayout.addView(lin6)


        //------------------------------------------สถานที่เกิดเหตุ----------------------------------------------
        val param7 = listOf(
            listOf("4. สถานที่เกิดเหตุ", eventReport.report_place.toString(),"text_bold","")
        )

        val lin7 = fromReport.createFreeFlexSectionView(param7)
        mainLayout.addView(lin7)
        //----------------------------------------------------------------------------------------


        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------

        /*var today = oview.findViewWithTag<AutoCompleteTextView>("dateEvent")
        var formattedTime = oview.findViewWithTag<AutoCompleteTextView>("timeEvent")

        val param8 = listOf(
            listOf("5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ", today.text.toString(), "text_bold",""),
            listOf("เวลาประมาณ", formattedTime.text.toString(), "text_bold",""),
        )
        val lin8 = fromReport.createFreeFlexSectionView(param8)
        mainLayout.addView(lin8)*/
        //----------------------------------------------------------------------------------------

        //------------------------------------------ข้อมูลเบื้องต้น----------------------------------------------
        val param9 = listOf(
            listOf("6. ข้อมูลเบื้องต้น", eventReport.report_preinfo.toString(), "text_bold","")
        )
        val lin9 = fromReport.createFreeFlexSectionView(param9)
        mainLayout.addView(lin9)
        //----------------------------------------------------------------------------------------

        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------
        val param10 = listOf(
            listOf("7. พนักงานสอบสวน", eventReport.report_officer.toString(), "text_bold",""),
            listOf("เบอร์โทรศัพท์ติดต่อ", eventReport.report_officer_contact_no.toString(), "text_bold","")
        )
        val lin10 = fromReport.createFreeFlexSectionView(param10)
        mainLayout.addView(lin10)
        //----------------------------------------------------------------------------------------

        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------
        val param11 = listOf(
            listOf("8. ผู้เสียหาย", eventReport.report_sufferer.toString(), "text_bold",""),
            listOf("เบอร์โทรศัพท์ติดต่อ", eventReport.report_sufferer_contact_no.toString(), "text_bold",""),
        )
        val lin11 = fromReport.createFreeFlexSectionView(param11)
        mainLayout.addView(lin11)

        val bt_save = view.findViewById<Button>(R.id.bt_save)

        if(editFlag == true){
            //Create button link to form
            val param12 = listOf(
                listOf("", "ฟอร์มเก็บหลักฐาน"+type, "button","button_form")
            )
            val lin12 = fromReport.createFreeFlexSectionView(param12)
            mainLayout.addView(lin12)

            val colEvidence  = view.findViewWithTag<Button>("button_form")
            colEvidence.setOnClickListener(View.OnClickListener { v ->
                when (type) {
                    "ทรัพย์" -> loadFragment(AssetFormFragment(),eventReport.uid)
                    "ชีวิต" -> loadFragment(LifeFormFragment(),eventReport.uid)
                    "ระเบิด" -> loadFragment(BomFormFragment(),eventReport.uid)
                    "เพลิงไหม้" -> loadFragment(FireFormFragment(),eventReport.uid)
                    /*"จราจร" ->
                    "ตรวจเก็บวัตถุพยาน" ->
                    "อื่นๆ" ->*/
                }
                dialog.hide()
            })

            //loadFragment(AssetFormFragment())
            //loadFragment(FireFormFragment())
            //loadFragment(BomFormFragment())
            //loadFragment(LifeFormFragment())
            //loadFragment(ReportEventFormFragment())
            //loadFragment(CreateMenuFragment())
            //loadFragment(EventReportListFragment())

            //edit report form
            bt_save.text = "แก้ไข"
            bt_save.setOnClickListener(View.OnClickListener { v ->

                //createForm(view, eventReport)
                loadFragment(ReportEventFormFragment(),eventReport.uid)
                dialog.hide()
            })

        }else{
            bt_save.text = "บันทึก"
            bt_save.setOnClickListener(View.OnClickListener { v ->
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                val eventReportDao: EventReportDao = db.eventReportDao()

                if(eventReport.uid > 0 ){
                    //eventReportDao.updateAll(eventReport)
                }else{
                    val uid = eventReportDao.insertAll(eventReport)
                    when (eventReport.report_type) {
                        "ทรัพย์" -> {
                            /*val assetFormDao: AssetFormDao = db.assetFormDao()
                            val assetForm = AssetForm()
                            assetForm.event_report_id = uid
                            assetFormDao.insertAll(assetForm)*/
                        }
                        "" -> println("Value is 2")
                        else -> println("Value is something else")
                    }

                    /*val evidenceBagDao: EvidenceBagDao = db.evidenceBagDao()
                    val evidenceBag = EvidenceBag()
                    evidenceBag.event_report_id = uid
                    evidenceBagDao.insertAll(evidenceBag)*/
                }

                dialog.hide()
            })
        }

        val bt_close  = view.findViewById<ImageButton>(R.id.bt_close)
        bt_close.setOnClickListener(View.OnClickListener { v ->
            dialog.hide()
        })

        dialog.show()
        dialog.window!!.attributes = lp
    }

    private  fun loadFragment(fragment: Fragment,uid: Long = 0){

        val args = Bundle()
        args.putLong("uid", uid) // Example parameter: key-value pair
        fragment.arguments = args

        val fragmentManager = parentFm
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.nav_host_fragment,fragment)
        transaction.commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buildFormData(oview: View, eventReport: EventReport = EventReport()): EventReport {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_report_event, null)
        val fromReport = UiViewHelper(context,view,childFm)

        var head = oview.findViewWithTag<TextView>("head")
        var report_number = oview.findViewWithTag<TextView>("report_number")

        var report_write = oview.findViewWithTag<TextView>("report_write")
        var oneDate = oview.findViewWithTag<TextView>("oneDate")
        var oneTime = oview.findViewWithTag<TextView>("oneTime")
        var dateStr = oneDate.text.toString()+" "+oneTime.text.toString()

        var report_from_where = oview.findViewWithTag<TextView>("report_from_where")
        val report_channel_other = oview.findViewWithTag<EditText>("report_channel_other")

        val report_channel_group = oview.findViewWithTag<RadioGroup>("report_channel")
        val report_channel = fromReport.retrieveRadio(report_channel_group,oview)

        //-------------------------------------------เหตุที่รับแจ้ง---------------------------------------------
        val report_type_other = oview.findViewWithTag<EditText>("report_type_other")
        val report_type_group = oview.findViewWithTag<RadioGroup>("report_type")
        val report_type = fromReport.retrieveRadio(report_type_group,oview)
        //----------------------------------------------------------------------------------------
        var report_place = oview.findViewWithTag<TextView>("report_place")
        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------

        var today = oview.findViewWithTag<AutoCompleteTextView>("dateEvent")
        var formattedTime = oview.findViewWithTag<AutoCompleteTextView>("timeEvent")
        val report_known_datetime = today.text.toString()+" "+formattedTime.text.toString()
        //----------------------------------------------------------------------------------------

        //------------------------------------------ข้อมูลเบื้องต้น----------------------------------------------
        var report_preinfo = oview.findViewWithTag<EditText>("report_preinfo")
        //----------------------------------------------------------------------------------------

        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------
        var report_officer = oview.findViewWithTag<EditText>("report_officer")
        var report_officer_contact_no = oview.findViewWithTag<EditText>("report_officer_contact_no")
        //----------------------------------------------------------------------------------------

        //------------------------------------------5. วัน เวลา ผู้เสียหายทราบเหตุ/เกิดเหตุ/พงส.ทราบเหตุ----------------------------------------------
        var report_sufferer = oview.findViewWithTag<EditText>("report_sufferer")
        var report_sufferer_contact_no = oview.findViewWithTag<EditText>("report_sufferer_contact_no")

        //val eventReport = EventReport()
        eventReport.head = head.getText().toString()
        eventReport.report_number = report_number.getText().toString()
        eventReport.report_write = report_write.getText().toString()
        eventReport.create_datetime = convertDateTimeToLong(dateStr)
        eventReport.report_from_where = report_from_where.getText().toString()
        eventReport.report_channel = report_channel
        eventReport.report_channel_other = report_channel_other.getText().toString()

        eventReport.report_type = report_type
        eventReport.report_type_other = report_type_other.getText().toString()
        eventReport.report_place = report_place.getText().toString()

        eventReport.report_known_datetime = convertDateTimeToLong(report_known_datetime)
        eventReport.report_preinfo = report_preinfo.getText().toString()

        eventReport.report_officer = report_officer.getText().toString()
        eventReport.report_officer_contact_no = report_officer_contact_no.getText().toString()
        eventReport.report_sufferer = report_sufferer.getText().toString()
        eventReport.report_sufferer_contact_no = report_sufferer_contact_no.getText().toString()
        eventReport.status = true

        return eventReport
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTimeToLong(dateTimeString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        return dateTime.toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertLongToDateTime(epochTime: Long?): LocalDateTime {
        return LocalDateTime.ofInstant(epochTime?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault())
    }

}