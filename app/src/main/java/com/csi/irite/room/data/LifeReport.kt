package com.csi.irite.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LifeReport")
data class LifeReport(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    var event_report_id: Long? = 0,
    var case_name : String? = "",
    var date : String? = "",            // YYYY-MM-DD format
    var time_approx : String? = "",      // HH:mm format
    var notification_method: String? = "", // Radio group (e.g., phone, radio, document, other)
    var other_details1: String? = "",
    var police_station: String? = "",
    var location1: String? = "",
    var recorded_by: String? = "",
    var investigator: String? = "",
    var phone_number: String? = "",

    //form 2
    var incident_location: String? = "",
    var owner: String? = "",
    var victim: String? = "",
    var related_other: String? = "",
    var victim_age: Int? = 0,

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
    var location: String? = "",
    var location_other_text: String? = "",
    var light:  String? = "",
    var light_other_text:  String? = "",
    var temperature: String? = "",
    var temperature_other_text: String? = "",
    var characteristics_of_the_scene: String? = "",
    var characteristics_of_the_scene_other_text: String? = "",
    var surrounding_front_view: String? = "",
    var surrounding_front: String? = "",
    var surrounding_left: String? = "",
    var surrounding_right: String? = "",
    var surrounding_back: String? = "",
    var incident_location2: String? = "",
    var smell: String? = "",
    var smell_other_text: String? = "",
    var characteristics_of_the_scene_insite: String? = "",
    var characteristics_of_the_scene_insite_other_tex: String? = "",

    var surrounding_front_out_view: String? = "",
    var surrounding_front_out: String? = "",
    var surrounding_left_out: String? = "",
    var surrounding_right_out: String? = "",
    var surrounding_back_out: String? = "",
    var inside_prop: String? = "",
    var case_location: String? = "",
    var place_object: String? = "",
    var roof: String? = "",
    var floor: String? = "",
    var back_side: String? = "",
    var right_side: String? = "",
    var left_side: String? = "",
    var front_side: String? = "",
    var instruct_place: String? = "",

    //7
    // Case Circumstances
    var case_details: String? = "",
    var entry_exit: String? = "",
    var fight_trace: String? = "",
    var fight_trace_details: String? = "",

    var find: String? = "",
    var find_details: String? = "",

    var die: String? = "",
    var die_details: String? = "",
    var location_of_body: String? = "",
    var condition_of_body: String? = "",

    var clothing: Boolean = false,
    var clothing_details: String? = "",

    var pants: Boolean = false,
    var pants_details: String? = "",

    var shoes_socks: Boolean = false,
    var shoes_socksDetails: String? = "",

    var belt: Boolean = false,
    var belt_details: String? = "",

    var tattoo_scar: Boolean = false,
    var tattoo_scarDetails: String? = "",

    var other: Boolean = false,
    var other_details: String? = "",

    var preliminary_wound_condition: String? = "",
    var wound_count: Int? = 0,                      // number input
    var wound_description: String? = "",

    var blood_like_stain: String? = "",
    var blood_test_kit_used: String? = "",

    var hemastix_color_change_green_blue: Boolean = false,
    var hemastix_no_color_change: Boolean = false,

    var phenolphthalein_color_change_pink: Boolean = false,
    var phenolphthalein_no_color_change: Boolean = false,

    var other_evidence: String? = "",

    var firearm_and_ammunition_evidence: String? = "",
    var genetic_evidence: String? = "",
    var latent_print_evidence: String? = "",
    var other_types_of_evidence: String? = "",

    var final_inspection: Boolean = false,
    var evidence_collection_complete: Boolean = false,
    var scene_photo_and_handover: Boolean = false,

    var dresses: String? = "",
    var hemastix: String? = "",
    var final_inspectioned: String? = "",

    //form
    var inspection_completion_date: String? = "",
    var inspection_completion_time: String? = "",
    var owner_list:String? = ""
) : BaseEntity()