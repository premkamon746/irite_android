package com.csi.irite.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FireReport")
data class FireReport(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    var event_report_id: Long? = 0,
    var case_type: String? = "",
    var notification_method: String? = "",
    var notification_other_details: String? = "",
    var police_station: String? = "",
    var investigator_name: String? = "",
    var investigator_rank: String? = "",
    var phone_number: String? = "",

    var incident_location: String? = "",
    var is_victim: Boolean = false,
    /*var victim_name: String? = "",
    var victim_age: Int? = 0,
    var victim_phone_number: String? = "",*/

    var victim_name: String? = "",

    var is_deceased: Boolean = false,
    var deceased_name: String? = "",
    var deceased_age: Int? = 0,

    var incident_date: String? = "",
    var approximate_time: String? = "",

    var investigation_date: String? = "",
    var investigation_time: String? = "",
    var additional_investigation_date: String? = "",
    var additional_investigation_time: String? = "",

    var has_savings: String? = "",
    var has_savings_other_details: String? = "",

    var exterior_type: String? = "",
    var exterior_type_other_details: String? = "",

    var surrounding_fence: String? = "",
    var facing_direction: String? = "",
    var front_adjacent: String? = "",
    var left_adjacent: String? = "",
    var right_adjacent: String? = "",
    var back_adjacent: String? = "",
    var interior_description: String? = "",
    var incident_location_description: String? = "",
    var approx_size: String? = "",
    var facing_incident_direction: String? = "",
    var front_wall: String? = "",
    var left_wall: String? = "",
    var right_wall: String? = "",
    var back_wall: String? = "",

    var room_floor: String? = "",
    var roof_ceiling: String? = "",
    var front_wall_left_to_right: String? = "",
    var left_wall_front_to_back: String? = "",
    var right_wall_front_to_back: String? = "",
    var left_wall_left_to_right: String? = "",

    var fire_insurance: String? = "",
    var fire_insurance_details: String? = "",
    var no_fire_insurance: String? = "",
    var no_fire_insurance_details: String? = "",
    var fire_time: String? = "",
    var extinguisher: String? = "",
    var extinguisher_details: String? = "",
    var damage_condition_area: String? = "",

    var front_leather_cover: String? = "",
    var left_leather_cover: String? = "",
    var right_leather_cover: String? = "",
    var back_leather_cover: String? = "",
    var floor: String? = "",
    var ceiling: String? = "",
    var front_wall_objects: String? = "",
    var left_front_wall_objects: String? = "",
    var right_front_wall_objects: String? = "",
    var back_front_wall_objects: String? = "",
    var floor_objects: String? = "",
    var fire_area_before: String? = "",
    var damage_area: String? = "",
    var damage_description: String? = "",
    var fuel: Boolean = false,
    var fuel_oil: Boolean = false,
    var other_evidence_description: String? = "",
    var heat_source: String? = "",
    var cigarette: Boolean = false,
    var incense_candle: Boolean = false,
    var unknown_object_with_fire: Boolean = false,
    var other_fire_object: Boolean = false,
    var other_fire_object_description: String? = "",

    var start_fire_location: String? = "",
    var start_fire_location_found: Boolean = false,
    var start_fire_location_not_found: Boolean = false,
    var start_fire_location_description: String? = "",

    var detected_evidence: String? = "",
    var fuel_source_description: String? = "",
    var nofire_insurance_details: String? = "",

    /*var fuel_source: Boolean = false,
    var fuel_source_description: String? = "",
    var chemical_substances: Boolean = false,
    var other_evidence: Boolean = false,*/

    var heat_source2: Boolean = false,
    var heat_source_description: String? = "",
    var preliminary_opinion: String? = "",

    var fire_sources: String? = "",
    /*var preliminary_opinion: Boolean = false,
    var preliminary_opinion_yes: Boolean = false,
    var preliminary_opinion_no: Boolean = false,*/
    var preliminary_opinion_description: String? = "",
    var behav_case: String? = "",

    var final_inspection: String? = "",
    var inspection_date: String? = "",
    var inspection_time: String? = "",
    var owner_list:String? = ""
):BaseEntity()
