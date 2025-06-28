package com.csi.irite.room.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InspectionRecord")
data class InspectionRecord(
    @PrimaryKey(autoGenerate = true) var uid: Long = 0,
    // Checks
    var is_electric_battery_checked: Boolean = false,
    var electric_battery_fix: String? = null,
    var electric_battery_remark: String? = null,

    var is_tire_checked: Boolean = false,
    var tire_fix: String? = null,
    var tire_remark: String? = null,

    var is_lighting_checked: Boolean = false,
    var lighting_fix: String? = null,
    var lighting_remark: String? = null,

    var is_vehicle_condition_checked: Boolean = false,
    var vehicle_condition_fix: String? = null,
    var vehicle_condition_remark: String? = null,

    var is_horn_checked: Boolean = false,
    var horn_fix: String? = null,
    var horn_remark: String? = null,

    var is_brake_system_checked: Boolean = false,
    var brake_system_fix: String? = null,
    var brake_system_remark: String? = null,

    var is_wiper_checked: Boolean = false,
    var wiper_fix: String? = null,
    var wiper_remark: String? = null,

    var is_fluids_checked: Boolean = false,
    var fluids_fix: String? = null,
    var fluids_remark: String? = null,

    var is_air_conditioning_checked: Boolean = false,
    var air_conditioning_fix: String? = null,
    var air_conditioning_remark: String? = null,

    var is_test_drive_checked: Boolean = false,
    var test_drive_fix: String? = null,
    var test_drive_remark: String? = null,

    // Kits
    var has_fingerprint_kit: Boolean = false,
    var fingerprint_kit_fix: String? = null,
    var fingerprint_kit_remark: String? = null,

    var has_stationery_kit: Boolean = false,
    var stationery_kit_fix: String? = null,
    var stationery_kit_remark: String? = null,

    var has_impression_casting_kit: Boolean = false,
    var impression_casting_kit_fix: String? = null,
    var impression_casting_kit_remark: String? = null,

    var has_size_measurement_kit: Boolean = false,
    var size_measurement_kit_fix: String? = null,
    var size_measurement_kit_remark: String? = null,

    var has_dna_bloodstain_kit: Boolean = false,
    var dna_bloodstain_kit_fix: String? = null,
    var dna_bloodstain_kit_remark: String? = null,

    var has_gunpowder_kit: Boolean = false,
    var gunpowder_kit_fix: String? = null,
    var gunpowder_kit_remark: String? = null,

    var has_explosive_person_kit: Boolean = false,
    var explosive_person_kit_fix: String? = null,
    var explosive_person_kit_remark: String? = null,

    var has_explosive_object_kit: Boolean = false,
    var explosive_object_kit_fix: String? = null,
    var explosive_object_kit_remark: String? = null,

    var has_evidence_marker_kit: Boolean = false,
    var evidence_marker_kit_fix: String? = null,
    var evidence_marker_kit_remark: String? = null,

    var has_packaging_material: Boolean = false,
    var packaging_material_fix: String? = null,
    var packaging_material_remark: String? = null,

    var has_scene_protection_kit: Boolean = false,
    var scene_protection_kit_fix: String? = null,
    var scene_protection_kit_remark: String? = null,

    var has_evidence_search_kit: Boolean = false,
    var evidence_search_kit_fix: String? = null,
    var evidence_search_kit_remark: String? = null,

    var has_camera_and_lens: Boolean = false,
    var camera_and_lens_fix: String? = null,
    var camera_and_lens_remark: String? = null,

    var has_camera_battery: Boolean = false,
    var camera_battery_fix: String? = null,
    var camera_battery_remark: String? = null,

    var has_camera_accessories: Boolean = false,
    var camera_accessories_fix: String? = null,
    var camera_accessories_remark: String? = null,

    var has_memory_card: Boolean = false,
    var memory_card_fix: String? = null,
    var memory_card_remark: String? = null,

    var has_camera_test: Boolean = false,
    var camera_test_fix: String? = null,
    var camera_test_remark: String? = null,

    // Devices with Set No.
    var has_metal_detector: Boolean = false,
    var metal_detector_fix: String? = null,
    var metal_detector_remark: String? = null,
    var metal_detector_set_no: Int? = null,

    var has_poly_light: Boolean = false,
    var poly_light_fix: String? = null,
    var poly_light_remark: String? = null,
    var poly_light_set_no: Int? = null,

    var has_dust_lifter: Boolean = false,
    var dust_lifter_fix: String? = null,
    var dust_lifter_remark: String? = null,
    var dust_lifter_set_no: Int? = null,

    var has_laser_distance_meter: Boolean = false,
    var laser_distance_meter_fix: String? = null,
    var laser_distance_meter_remark: String? = null,
    var laser_distance_meter_set_no: Int? = null,

    var has_flashlight: Boolean = false,
    var flashlight_fix: String? = "",
    var flashlight_remark: String? = "",
    var flashlight_set_no: Int? = null,
    var flashlight_officer: String? = "",

    var head_remark: String? = "",
    var create_date: String? = "",
    var team: Int? = null,
    var fully: Boolean = false,
    var carlicense_no: String? = "",
    var mile: Int? = null
) : BaseEntity()
