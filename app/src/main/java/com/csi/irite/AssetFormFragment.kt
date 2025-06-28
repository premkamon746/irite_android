package com.csi.irite


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.toLowerCase
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.room.ColumnInfo
import com.csi.irite.helper.DateTimeHelper
import com.csi.irite.helper.PanelViewHelper
import com.csi.irite.room.dao.AssetFormDao
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.AssetForm
import com.csi.irite.room.data.EventReport
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.model.com.csi.irite.helper.MapHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AssetFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssetFormFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    var nested_scroll_view: NestedScrollView? = null
    protected lateinit var map: MapView
    protected lateinit var mhp: MapHelper
    protected lateinit var mapCarView: View
    protected var event_report_id: Long = 0

        protected val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value == true }
        if (granted) {
            // Permission granted, get location
            mhp.getLocation(mapCarView)
        } else {
            // Permission denied, handle the case
            //Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            event_report_id = it.getLong("uid")
            //Toast.makeText(requireContext(), "Failed to load content $uid", Toast.LENGTH_SHORT).show()

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidGraphicFactory.createInstance(requireActivity().application)
        val view =  inflater.inflate(R.layout.fragment_asset_form, container, false)
        nested_scroll_view = view.findViewById<View>(R.id.nested_scroll_view) as NestedScrollView

        //bt_toggle_input!!.setOnClickListener { toggleSectionInput(bt_toggle_input!!) }

        val pv = PanelViewHelper(requireContext(),nested_scroll_view!!)
        val linearLayoutTop = pv.createNestestChild()

        val cardview = pv.createPanel(R.layout.form_asset_event1,"1. การรับแจ้งเหตุ")
        val cardview2 = pv.createPanel(R.layout.form_asset_event_place2,"2.สถานที่เกิดเหตุ")
        val cardview3 = pv.createPanel(R.layout.form_common_date3,"3. วันเวลาที่ทราบเหตุ/เกิดเหตุ")

        val cardview4 = pv.createPanel(R.layout.form_common_place4,"4. วันเวลาที่ตรวจเหตุ")
        val cardview5 = pv.createPanel(R.layout.form_fire_check5,"5.ผู้ตรวจสถานที่เกิดเหตุ")
        val cardview6 = pv.createPanel(R.layout.form_asset_event_place_prop6,"6. ลักษณะสถานที่เกิดเหตุ")

        val cardview7 = pv.createPanel(R.layout.form_asset_result7,"7.ผลการตรวจสถานที่เกิดเหตุ")
        val cardview8 = pv.createPanel(R.layout.form_asset_sign8,"8.การส่งมอบคืนสถานที่เกิดเหตุ")
        val cardview9 = pv.createPanel(R.layout.location_maps,"แผนที่โดยสังเขป")

        linearLayoutTop.addView(cardview)
        linearLayoutTop.addView(cardview2)
        linearLayoutTop.addView(cardview3)

        linearLayoutTop.addView(cardview4)
        linearLayoutTop.addView(cardview5)
        linearLayoutTop.addView(cardview6)

        linearLayoutTop.addView(cardview7)
        linearLayoutTop.addView(cardview8)
        linearLayoutTop.addView(cardview9)

        mapCarView = cardview9
        map = cardview9.findViewById(R.id.map)
        mhp = MapHelper(requireContext(), map)
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            // Permission already granted, get location
            Log.d("xxxxxxxxxxxx","getlocation")
            mhp.getLocation(mapCarView)
        }/**/

        var assetData = false
        var assetForms : List<AssetForm>? = null

        try {
            initConponent(view)

            val eventReportDao: EventReportDao = this.db!!.eventReportDao()
            val eventReports:List<EventReport> = eventReportDao.getById(uid)

            if (eventReports.count() > 0) {
                var eventReport = eventReports.get(0)
                try {
                    retriveForm(pv, view, eventReport)
                } catch (e: Exception) {
                    Log.d("xxxxxx1",e.message.toString())
                    val stackTrace = e.stackTraceToString()
                    Log.d("xxxxxx2", stackTrace)
                }


                /*if (assetForms.count() > 0){
                    try {
                        assetData = true // if asset has data
                        Log.d("xxxxxxxxxxxx",assetForms.count().toString())
                        Log.d("xxxxxxxxxxxx",assetForms.get(0).uid.toString() )
                        Log.d("xxxxxxxxxxxx",assetForms.get(0).event_report_id.toString())
                        Log.d("xxxxxxxxxxxx","phone : "+assetForms.get(0).phenno)
                        retriveAssetForm(pv, view,assetForms.get(0))
                    } catch (e: Exception) {
                        Log.d("xxxxxx1",e.message.toString())
                        val stackTrace = e.stackTraceToString()
                        Log.d("xxxxxx2", stackTrace)
                    }

                }*/
            }

            val saveButton: Button = view.findViewById(R.id.saveButton)
            saveButton.setOnClickListener(View.OnClickListener { v ->
                if(!assetData){
                    var assetForm : AssetForm = AssetForm()
                    save(pv, view, assetForm)
                }else {
                    save(pv, view, assetForms!!.get(0))
                }
                saveComplete()


            })

            /*val updateLoc: Button = view.findViewById(R.id.bt_update_loc)
            updateLoc.setOnClickListener(View.OnClickListener { v ->
                val latEdit = view.findViewById<EditText>(R.id.latitude)
                val longEdit = view.findViewById<EditText>(R.id.longitude)
                var latlng:LatLong = LatLong(latEdit.toDouble(), longEdit.toDouble())
                updateMarker(latlng)
            })*/

            val expandButton: Button = view.findViewById(R.id.expandButton)
            expandButton.setOnClickListener(View.OnClickListener { v ->
                if(expandButton.text.toString().trim().toLowerCase() == "expand all"){
                    expandButton.setText("CLOSE ALL")
                    pv.expandAll()
                }else{
                    expandButton.setText("EXPAND ALL")
                    pv.closeAll()
                }

            })
        } catch (e: Exception) {
            // Must be safe
            Log.d("xxxxxx1",e.message.toString())
            val stackTrace = e.stackTraceToString()
            Log.d("xxxxxx2", stackTrace)
        }

        try {
            uicontrol(view)
        } catch (e: Exception) {
            // Must be safe
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun retriveForm(pv:PanelViewHelper, view:View, eventReport:EventReport){

        val radioGroup: RadioGroup = view.findViewById(R.id.group_channel)
        pv.retreveRadio(view,radioGroup, eventReport.report_channel.toString().trim())

        val create_date: LocalDateTime = pv.convertLongToDateTime(eventReport.create_datetime)
        //Log.d("xxxxxxxxxxx", "tttt "+create_date.toString())
        val oneDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(create_date)
        var oneTime = DateTimeFormatter.ofPattern("HH:mm").format(create_date)

        val bt_event_date: AutoCompleteTextView = view.findViewById(R.id.bt_event_date)
        val bt_event_time: AutoCompleteTextView = view.findViewById(R.id.bt_event_time)
        val officer: EditText = view.findViewById(R.id.officer)
        val phone_no: EditText = view.findViewById(R.id.phone_no)

        bt_event_date.setText(oneDate)
        bt_event_time.setText(oneTime)
        officer.setText(eventReport.report_officer)
        phone_no.setText(eventReport.report_officer_contact_no)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save(pv:PanelViewHelper, view:View, assetForm : AssetForm){
        var update = false
        if (assetForm.event_report_id!! > 0){
            update = true
        }

        //1
        var asset_type = pv.getRadioValue(view, R.id.asset_type)
        assetForm.asset_type = asset_type

        //2
        val event_place: EditText = view.findViewById(R.id.event_place)
        val name: EditText = view.findViewById(R.id.name)
        val age: EditText = view.findViewById(R.id.age)

        //3
        var bt_event_date_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_date_know)
        var bt_event_time_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_time_know)
        var bt_event_time_officer_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_time_officer_know)
        var bt_event_date_officer_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_date_officer_know)
        //known_datetime //officer_known_datetime

        //4
        var bt_event_place_date: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_date)
        var bt_event_place_time: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_time)
        var bt_event_place_date_more: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_date_more)
        var bt_event_place_time_more: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_time_more)
        //check_datetime check_more_datetime


        //6
        //val keep_place: RadioGroup = view.findViewById(R.id.keep_place)
        var keep_place = pv.getRadioValue(view, R.id.keep_place)

        var floor_no: EditText = view.findViewById(R.id.no_of_floor)
        var wall = pv.getRadioValue(view, R.id.wall)

        var front: EditText = view.findViewById(R.id.front)
        var left: EditText = view.findViewById(R.id.left)
        var right: EditText = view.findViewById(R.id.right)
        var back: EditText = view.findViewById(R.id.back)

        //7
        var behavior_case: EditText = view.findViewById(R.id.behavior_case)
        var criminal_access = pv.getRadioValue(view, R.id.criminal_access)
        var vestige = pv.getRadioValue(view, R.id.vestige)
        var vestige_other = pv.getRadioValue(view, R.id.vestige_other)
        var criminal_tools = pv.getRadioValue(view, R.id.criminal_tools)
        var criminal_tools_other: EditText = view.findViewById(R.id.criminal_tools_other)
        var criminal_size: EditText = view.findViewById(R.id.criminal_size)
        var criminal_no: EditText = view.findViewById(R.id.criminal_no)

        var weapon = pv.getRadioValue(view, R.id.weapon)
        var weapon_other: EditText = view.findViewById(R.id.weapon_other)

        var weapon_tools = pv.getRadioValue(view, R.id.weapon_tools)
        var weapon_tools_other: EditText = view.findViewById(R.id.weapon_tools_other)

        var attack = pv.getRadioValue(view, R.id.attack)
        var man_effect = pv.getRadioValue(view, R.id.man_effect)

        var around: EditText = view.findViewById(R.id.around)
        var asset_pirate: EditText = view.findViewById(R.id.asset_pirate)
        var evedent_object: EditText = view.findViewById(R.id.evedent_object)

        var blood = pv.getRadioValue(view, R.id.blood)
        var hem_change = pv.getRadioValue(view, R.id.hem_change)
        var phenno = pv.getRadioValue(view, R.id.phenno)

        var other_evident: EditText = view.findViewById(R.id.other_evident)

        var fingle_print_evident: EditText = view.findViewById(R.id.fingle_print_evident)
        var genetic_evident: EditText = view.findViewById(R.id.genetic_evident)
        var cut_evident: EditText = view.findViewById(R.id.cut_evident)
        var other_evident_type: EditText = view.findViewById(R.id.other_evident_type)

        var last_check: CheckBox = view.findViewById(R.id.last_check)
        var last_evidence_complete: CheckBox = view.findViewById(R.id.last_evidence_complete)
        var camera_event: CheckBox = view.findViewById(R.id.camera_event)


        assetForm.event_place = event_place.text.toString()
        assetForm.man = pv.getRadioValue(view, R.id.man)
        assetForm.name = name.text.toString()
        assetForm.event_report_id = event_report_id
        try{ assetForm.age = age.text.toString().toInt()}catch (e: Exception){}

        var dt1  = bt_event_date_know.text.toString()+" "+bt_event_time_know.text.toString()
        var dt2  = bt_event_time_officer_know.text.toString()+" "+bt_event_date_officer_know.text.toString()

        try{ assetForm.known_datetime = pv.convertDateTimeToLong(dt1) } catch (e: Exception){}
        try{ assetForm.officer_known_datetime = pv.convertDateTimeToLong(dt2) } catch (e: Exception){}

        var dt3  = bt_event_place_date.text.toString()+" "+ bt_event_place_time.text.toString()
        var dt4  = bt_event_place_date_more.text.toString()+" "+ bt_event_place_time_more.text.toString()

        try{ assetForm.check_datetime = pv.convertDateTimeToLong(dt3) }catch (e: Exception){}
        try{ assetForm.check_more_datetime = pv.convertDateTimeToLong(dt4) }catch (e: Exception){}

        try{ assetForm.floor_no = floor_no.text.toString().toFloat() }catch (e: Exception){}

        var out_charector = pv.getRadioValue(view, R.id.out_charector)
        assetForm.out_charector = out_charector

        assetForm.keep_place = keep_place
        assetForm.wall = wall
        assetForm.front = front.text.toString()
        assetForm.left = left.text.toString()
        assetForm.right = right.text.toString()
        assetForm.back = back.text.toString()

        //7
        assetForm.behavior_case = behavior_case.text.toString()
        assetForm.criminal_access = criminal_access
        assetForm.vestige = vestige
        assetForm.vestige_other = vestige_other
        assetForm.criminal_tools = criminal_tools
        assetForm.criminal_tools_other = criminal_tools_other.text.toString()
        assetForm.criminal_size = criminal_size.text.toString()
        assetForm.criminal_no = criminal_no.text.toString()
        assetForm.weapon = weapon
        assetForm.weapon_other = weapon_other.text.toString()
        assetForm.weapon_tools = weapon_tools
        assetForm.weapon_tools_other = weapon_tools_other.text.toString()
        assetForm.attack = attack
        assetForm.man_effect = man_effect
        assetForm.around = around.text.toString()
        assetForm.asset_pirate = asset_pirate.text.toString()
        assetForm.evedent_object = evedent_object.text.toString()
        assetForm.blood = blood
        assetForm.hem_change = hem_change
        assetForm.phenno = phenno
        assetForm.other_evident = other_evident.text.toString()
        assetForm.fingle_print_evident = fingle_print_evident.text.toString()
        assetForm.genetic_evident = genetic_evident.text.toString()
        assetForm.cut_evident = cut_evident.text.toString()
        assetForm.other_evident_type = other_evident_type.text.toString()
        assetForm.last_check = last_check.isChecked
        assetForm.last_evidence_complete = last_evidence_complete.isChecked
        assetForm.camera_event = camera_event.isChecked

        val gson = Gson()
        val jsonString = gson.toJson(assetForm)


        if(update == true){
            Log.d("xxxxxxxxxxx", "update")
        }else{
            Log.d("xxxxxxxxxxx", "insert")
        }


        //known_datetime //officer_known_datetime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun retriveAssetForm(pv:PanelViewHelper, view:View, assetForm: AssetForm){

        //1

        val asset_type: RadioGroup = view.findViewById(R.id.asset_type)
        pv.retreveRadio(view, asset_type, assetForm.asset_type.toString())


        //2
        val event_place: EditText = view.findViewById(R.id.event_place)
        event_place.setText(assetForm.event_place.toString())

        val man: RadioGroup = view.findViewById(R.id.man)
        pv.retreveRadio(view, man, assetForm.man.toString())

        val name: EditText = view.findViewById(R.id.name)
        name.setText(assetForm.name.toString())

        val age: EditText = view.findViewById(R.id.age)
        age.setText(assetForm.age.toString())

        //3
        var bt_event_date_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_date_know)
        var bt_event_time_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_time_know)
        val known_datetime:LocalDateTime = pv.convertLongToDateTime(assetForm.known_datetime)
        val knowDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(known_datetime)
        var knowTime = DateTimeFormatter.ofPattern("HH:mm").format(known_datetime)
        bt_event_date_know.setText(knowDate)
        bt_event_time_know.setText(knowTime)

        var bt_event_time_officer_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_date_officer_know)
        var bt_event_date_officer_know: AutoCompleteTextView = view.findViewById(R.id.bt_event_time_officer_know)
        val officer_know_datetime:LocalDateTime = pv.convertLongToDateTime(assetForm.officer_known_datetime)
        val officerDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(officer_know_datetime)
        var officerTime = DateTimeFormatter.ofPattern("HH:mm").format(officer_know_datetime)
        bt_event_time_officer_know.setText(officerDate)
        bt_event_date_officer_know.setText(officerTime)

        //4
        var bt_event_place_date: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_date)
        var bt_event_place_time: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_time)
        val check_datetime:LocalDateTime = pv.convertLongToDateTime(assetForm.check_datetime)
        val checkDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(check_datetime)
        var checkTime = DateTimeFormatter.ofPattern("HH:mm").format(check_datetime)
        bt_event_place_date.setText(checkDate)
        bt_event_place_time.setText(checkTime)


        var bt_event_place_date_more: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_date_more)
        var bt_event_place_time_more: AutoCompleteTextView = view.findViewById(R.id.bt_event_place_time_more)
        val check_more_datetime:LocalDateTime = pv.convertLongToDateTime(assetForm.check_more_datetime)
        val moreDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(check_more_datetime)
        var moreTime = DateTimeFormatter.ofPattern("HH:mm").format(check_more_datetime)
        bt_event_place_date_more.setText(moreDate)
        bt_event_place_time_more.setText(moreTime)

        //check_datetime check_more_datetime


        //6
        val keepPlace: RadioGroup = view.findViewById(R.id.keep_place)
        pv.retreveRadio(view, keepPlace, assetForm.keep_place.toString())

        var floor_no: EditText = view.findViewById(R.id.no_of_floor)
        floor_no.setText(assetForm.floor_no.toString())

        var out_charector: RadioGroup = view.findViewById(R.id.out_charector)
        pv.retreveRadio(view, out_charector, assetForm.out_charector.toString())

        var wall: RadioGroup = view.findViewById(R.id.wall)
        pv.retreveRadio(view, wall, assetForm.wall.toString())

        var front: EditText = view.findViewById(R.id.front)
        front.setText(assetForm.front.toString())

        var left: EditText = view.findViewById(R.id.left)
        left.setText(assetForm.left.toString())

        var right: EditText = view.findViewById(R.id.right)
        right.setText(assetForm.right.toString())

        var back: EditText = view.findViewById(R.id.back)
        back.setText(assetForm.back.toString())

        //7
        var behavior_case: EditText = view.findViewById(R.id.behavior_case)
        behavior_case.setText(assetForm.behavior_case.toString())

        var criminal_access: RadioGroup = view.findViewById(R.id.criminal_access)
        pv.retreveRadio(view, criminal_access, assetForm.criminal_access.toString())
        var vestige: RadioGroup = view.findViewById(R.id.vestige)
        pv.retreveRadio(view, vestige, assetForm.vestige.toString())
        var vestige_other: RadioGroup = view.findViewById(R.id.vestige_other)
        pv.retreveRadio(view, vestige_other, assetForm.vestige_other.toString())
        var criminal_tools: RadioGroup = view.findViewById(R.id.criminal_tools)
        pv.retreveRadio(view, criminal_tools, assetForm.criminal_tools.toString())

        var criminal_tools_other: EditText = view.findViewById(R.id.criminal_tools_other)
        criminal_tools_other.setText(assetForm.criminal_tools_other.toString())

        var criminal_size: EditText = view.findViewById(R.id.criminal_size)
        criminal_size.setText(assetForm.criminal_size.toString())

        var criminal_no: EditText = view.findViewById(R.id.criminal_no)
        criminal_no.setText(assetForm.criminal_no.toString())

        var weapon: RadioGroup = view.findViewById(R.id.weapon)
        pv.retreveRadio(view, weapon, assetForm.weapon.toString())

        var weapon_other: EditText = view.findViewById(R.id.weapon_other)
        weapon_other.setText(assetForm.weapon_other.toString())

        var weapon_tools: RadioGroup = view.findViewById(R.id.weapon_tools)
        pv.retreveRadio(view, weapon_tools, assetForm.weapon_tools.toString())

        var weapon_tools_other: EditText = view.findViewById(R.id.weapon_tools_other)
        weapon_tools_other.setText(assetForm.weapon_tools_other.toString())

        var attack: RadioGroup = view.findViewById(R.id.weapon_tools)
        pv.retreveRadio(view, attack, assetForm.attack.toString())

        var man_effect: RadioGroup = view.findViewById(R.id.man_effect)
        pv.retreveRadio(view, man_effect, assetForm.man_effect.toString())

        var around: EditText = view.findViewById(R.id.around)
        around.setText(assetForm.around.toString())
        var asset_pirate: EditText = view.findViewById(R.id.asset_pirate)
        asset_pirate.setText(assetForm.asset_pirate.toString())
        var evedent_object: EditText = view.findViewById(R.id.evedent_object)
        evedent_object.setText(assetForm.evedent_object.toString())


        var blood: RadioGroup = view.findViewById(R.id.blood)
        pv.retreveRadio(view, blood, assetForm.blood.toString())
        var hem_change: RadioGroup = view.findViewById(R.id.hem_change)
        pv.retreveRadio(view, hem_change, assetForm.hem_change.toString())
        var phenno: RadioGroup = view.findViewById(R.id.phenno)
        pv.retreveRadio(view, phenno, assetForm.phenno.toString())


        var other_evident: EditText = view.findViewById(R.id.other_evident)
        other_evident.setText(assetForm.other_evident.toString())

        var fingle_print_evident: EditText = view.findViewById(R.id.fingle_print_evident)
        fingle_print_evident.setText(assetForm.fingle_print_evident.toString())
        var genetic_evident: EditText = view.findViewById(R.id.genetic_evident)
        genetic_evident.setText(assetForm.genetic_evident.toString())
        var cut_evident: EditText = view.findViewById(R.id.cut_evident)
        cut_evident.setText(assetForm.cut_evident.toString())
        var other_evident_type: EditText = view.findViewById(R.id.other_evident_type)
        other_evident_type.setText(assetForm.other_evident_type.toString())

        var last_check: CheckBox = view.findViewById(R.id.last_check)
        last_check.isChecked = assetForm.last_check == true
        var last_evidence_complete: CheckBox = view.findViewById(R.id.last_evidence_complete)
        last_evidence_complete.isChecked = assetForm.last_evidence_complete == true
        var camera_event: CheckBox = view.findViewById(R.id.camera_event)
        camera_event.isChecked = assetForm.camera_event== true
    }

    fun initConponent(view: View){

        var dtHper = DateTimeHelper()
        dtHper.controlRadio(view)
        dtHper.defaultToday(view,R.id.bt_event_date)
        dtHper.setCurrentTime(view, R.id.bt_event_time)

        dtHper.eventSelectDate(view, R.id.bt_event_date, childFragmentManager)
        dtHper.eventSelectTime(view, R.id.bt_event_time,requireContext())


        //------------------------date in form 3
        dtHper.defaultToday(view,R.id.bt_event_date_know)
        dtHper.eventSelectDate(view, R.id.bt_event_date_know,childFragmentManager)
        dtHper.eventSelectTime(view, R.id.bt_event_time_know,requireContext())

        dtHper.defaultToday(view,R.id.bt_event_date_officer_know)
        dtHper.eventSelectDate(view, R.id.bt_event_date_know,childFragmentManager)
        dtHper.eventSelectTime(view, R.id.bt_event_time_officer_know,requireContext())

        //------------------------date in form 4
        dtHper.defaultToday(view,R.id.bt_event_place_date)
        dtHper.eventSelectDate(view, R.id.bt_event_place_date,childFragmentManager)
        dtHper.eventSelectTime(view, R.id.bt_event_place_time,requireContext())

        dtHper.defaultToday(view,R.id.bt_event_place_date_more)
        dtHper.eventSelectDate(view, R.id.bt_event_place_date_more,childFragmentManager)
        dtHper.eventSelectTime(view,R.id.bt_event_place_time_more,requireContext())
    }

    fun uicontrol(view: View){
        val raiohave: RadioButton = view.findViewById(R.id.from_asset_prop_place_have)
        val raionothave: RadioButton = view.findViewById(R.id.from_asset_prop_place_nothave)

        raiohave.setOnClickListener {
            raionothave.isChecked = false
            raiohave.isChecked = true
        }

        raionothave.setOnClickListener {
            raiohave.isChecked = false
            raionothave.isChecked = true
        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AssetForm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AssetFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
