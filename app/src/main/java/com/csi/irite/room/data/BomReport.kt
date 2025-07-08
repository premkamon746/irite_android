package com.csi.irite.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BomReport")
data class BomReport(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,  // Change from val to var
    var event_report_id: Long? = 0,
    var case_name : String? = "",
    var date : String? = "",            // YYYY-MM-DD format
    var timeApprox : String? = "",      // HH:mm format
    var notificationMethod : String? = "", // Radio group (e.g., phone, radio, document, other)
    var otherDetails1 : String? = "",
    var policeStation : String? = "",
    var location1 : String? = "",
    var recordedBy : String? = "",
    var investigator : String? = "",
    var phoneNumber : String? = "",      // 10-digit numeric string

    //form 2
    var incidentLocation: String? = "",

    /*var hasDeceased: Boolean = false,
    var deceasedName1: String? = "",
    var deceasedAge1: Int? = 0,
    var deceasedName2: String? = "",
    var deceasedAge2: Int? = 0,

    var hasInjured: Boolean = false,
    var injuredName: String? = "",
    var injuredAge: Int? = 0,

    var hasVictim: Boolean = false,
    var victimName: String? = "",
    var victimAge: Int? = 0,*/

    var victim_name: String? = "",
    var deceased_name: String? = "",
    var injured_name: String? = "",

    //form 3
    var victim_incident_date: String? = "",
    var victim_incident_time: String? = "",
    var officer_incident_date: String? = "",
    var officer_incident_time: String? = "",


    //form 4
    var investigation_date: String? = "",
    var investigation_time: String? = "",
    var additional_investigation_date: String? = "",
    var additional_investigation_time: String? = "",


    //form 6
    var characteristics_of_the_scene: String? = "",      // ถนน, สนามหญ้า, ฯลฯ
    var scene_other_specify: String? = "",              // อื่นๆ ระบุ

    var surrounding_front_facing: String? = "",         // เมื่อหันหน้าเข้า
    var surrounding_front: String? = "",                // ด้านหน้าติด
    var surrounding_left: String? = "",                // ด้านซ้ายติด
    var surrounding_right: String? = "",               // ด้านขวาติด
    var surrounding_back: String? = "",                // ด้านหลังติด

    var incident_area: String? = "",
    var has_approx_size_checkbox: Boolean = false,
    var approx_size: String? = "",

    var has_structure_checkbox: Boolean = false,
    var structure_detail: String? = "",

    var has_wall_checkbox: Boolean = false,
    var wall_detail: String? = "",

    var front_side: String? = "",
    var left_side: String? = "",
    var right_side: String? = "",
    var back_side: String? = "",
    var floor: String? = "",                  // พื้นห้อง
    var roof: String? = "",                   // หลังคา
    var object_arrangement: String? = "",      // การจัดวางสิ่งของ


    //7
    // Case Circumstances

    var incident_detail: String? = "",

    var case_circumstance: String? = "",

    var corpse_1_found: String? = "",     // "found" or "not_found"
    var corpse_1_note: String? = "",
    var corpse_1_detail: String? = "",

    var corpse_2_found: String? = "",
    var corpse_2_note: String? = "",
    var corpse_2_detail: String? = "",

    var corpse_3_found: String? = "",
    var corpse_3_note: String? = "",
    var corpse_3_detail: String? = "",

    var damage_detail: String? = "",
    var explosion_location: String? = "",

    var `package`: String? = "",                  // Radio: กล่องเหล็ก, ถังแก๊ซ, ฯลฯ
    var package_other_details: String? = "",      // If "อื่นๆ" is selected
    var explosion_method: String? = "",            // วิธีการจุดระเบิด

    var trap_detail: String? = "",         // ระบุ กับดัก / เหยียบ / สะดุด
    var wire_color: String? = "",         // สีสายไฟ
    var wire_length_cm: String? = "",      // ความยาวสายไฟ (ซม)
    var radio_brand: String? = "",         // ยี่ห้อวิทยุสื่อสาร
    var radio_model: String? = "",         // รุ่นวิทยุสื่อสาร
    var radio_color: String? = "",         // สีวิทยุสื่อสาร
    var radio_sn: String? = "",            // s/n วิทยุสื่อสาร
    var remote_control: String? = "",      // รีโมทคอนโทรล
    var timer_setting: String? = "",       // ตั้งเวลา
    var other_detail: String? = "",         // อื่นๆ
    var characteristics_of_the_scene_insite: String? = "",
    var inch: String? = "",
    var length_cm: String? = "",
    var nail_inch: String? = "",
    var others_details: String? = "",
    var explosive_components: String? = "",

    var expandable_earth_tube: Boolean = false,
    var expandable_earth_tube_details: String? = "",
    var electric_ignition_fuse: Boolean = false,
    var electric_ignition_fuse_details: String? = "",
    var electrical_tape: Boolean = false,
    var electrical_tape_details: String? = "",
    var sim_card: Boolean = false,
    var sim_card_details: String? = "",
    var ignition_circuit: Boolean = false,
    var ignition_circuit_details: String? = "",
    var battery_v: Boolean = false,
    var battery_v_details: String? = "",
    var dtmf_circuit_board: Boolean = false,
    var dtmf_circuit_board_details: String? = "",
    var circuit_board: Boolean = false,
    var circuit_board_details: String? = "",
    var circuit_package: Boolean = false,
    var circuit_package_details: String? = "",
    var clock: Boolean = false,
    var clock_details: String? = "",
    var trigger: Boolean = false,
    var trigger_details: String? = "",
    var safety_pin: Boolean = false,
    var safety_pin_details: String? = "",
    var others: Boolean = false,
    var others_details2: String? = "",
    var blood_like_stain: String? = "",
    var initial_bloodstain_test: String? = "",
    var hemastix_green_blue: Boolean = false,
    var hemastix_no_change: Boolean = false,
    var phenolphthalein_pink_change: Boolean = false,
    var phenolphthalein_no_change: Boolean = false,
    var other_evidence: String? = "",
    var evidence_handling: String? = "",
    var weapon_and_ammo_evidence: String? = "",
    var genetic_material_evidence: String? = "",
    var fingerprint_evidence: String? = "",
    var cut_mark_evidence: String? = "",
    var explosive_material_evidence: String? = "",
    var explosive_components_evidence: String? = "",
    var other_evidence_type: String? = "",
    var final_check: Boolean = false,
    var complete_evidence_collection: Boolean = false,
    var scene_image_and_hand_over: Boolean = false,

    //form 8
    var inspection_completion_date: String? = "",
    var inspection_completion_time: String? = "",
    var owner_list:String? = ""
) : BaseEntity()
