package com.csi.irite.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose


@Entity(tableName = "AssetForm")
data class AssetForm(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
) : BaseEntity()  {
    @ColumnInfo(name = "event_report_id")
    var event_report_id: Long? = 0

    //1
    @ColumnInfo(name = "asset_type")
    var asset_type: String? = ""
    @ColumnInfo(name = "create_datetime")
    var create_datetime: Long? = 0
    @ColumnInfo(name = "where")
    var where: String? = ""
    @ColumnInfo(name = "down")
    var down: String? = ""

    //2
    @ColumnInfo(name = "event_place")
    var event_place: String? = ""
    @ColumnInfo(name = "man")
    var man: String? = ""
    @ColumnInfo(name = "name")
    var name: String? = ""
    @ColumnInfo(name = "age")
    var age: Int? = 0

    //3
    @ColumnInfo(name = "known_datetime")
    var known_datetime: Long? = 0
    @ColumnInfo(name = "officer_known_datetime")
    var officer_known_datetime: Long? = 0

    //4
    @ColumnInfo(name = "check_datetime")
    var check_datetime: Long? = 0
    @ColumnInfo(name = "check_more_datetime")
    var check_more_datetime: Long? = 0

    //5
    @ColumnInfo(name = "officer_check")
    var officer_check: String? = ""

    //6
    @ColumnInfo(name = "keep_place")
    var keep_place: String? = ""
    @ColumnInfo(name = "out_charector")
    var out_charector: String? = ""
    @ColumnInfo(name = "floor_no")
    var floor_no: Float? = 0f
    @ColumnInfo(name = "front")
    var front: String? = ""
    @ColumnInfo(name = "left")
    var left: String? = ""
    @ColumnInfo(name = "right")
    var right: String? = ""
    @ColumnInfo(name = "back")
    var back: String? = ""
    @ColumnInfo(name = "wall")
    var wall: String? = ""

    @ColumnInfo(name = "look_inside")
    var look_inside: String? = ""
    @ColumnInfo(name = "in_charector")
    var in_charector: String? = ""
    @ColumnInfo(name = "around_place")
    var around_place: String? = ""

    //7
    @ColumnInfo(name = "behavior_case")
    var behavior_case: String? = ""
    @ColumnInfo(name = "criminal_size")
    var criminal_size: String? = ""
    @ColumnInfo(name = "criminal_access")
    var criminal_access: String? = ""
    @ColumnInfo(name = "vestige")
    var vestige: String? = ""
    @ColumnInfo(name = "vestige_other")
    var vestige_other: String? = ""

    @ColumnInfo(name = "criminal_tools")
    var criminal_tools: String? = ""
    @ColumnInfo(name = "criminal_tools_other")
    var criminal_tools_other: String? = ""
    @ColumnInfo(name = "vestige_size")
    var vestige_size: String? = ""
    @ColumnInfo(name = "criminal_no")
    var criminal_no: String? = ""
    @ColumnInfo(name = "weapon")
    var weapon: String? = ""
    @ColumnInfo(name = "weapon_other")
    var weapon_other: String? = ""

    @ColumnInfo(name = "criminal_attack")
    var criminal_attack: String? = ""
    @ColumnInfo(name = "weapon_tools")
    var weapon_tools: String? = ""
    @ColumnInfo(name = "weapon_tools_other")
    var weapon_tools_other: String? = ""
    @ColumnInfo(name = "attack")
    var attack: String? = ""
    @ColumnInfo(name = "attack_other")
    var attack_other: String? = ""

    @ColumnInfo(name = "man_effect")
    var man_effect: String? = ""

    @ColumnInfo(name = "around")
    var around: String? = ""
    @ColumnInfo(name = "asset_pirate")
    var asset_pirate: String? = ""
    @ColumnInfo(name = "evedent_object")
    var evedent_object: String? = ""
    @ColumnInfo(name = "blood")
    var blood: String? = ""
    @ColumnInfo(name = "blood_change")
    var blood_change: String? = ""

    @ColumnInfo(name = "test_blood")
    var test_blood: String? = ""

    @ColumnInfo(name = "other_evident")
    var other_evident: String? = ""
    @ColumnInfo(name = "fingle_print_evident")
    var fingle_print_evident: String? = ""

    @ColumnInfo(name = "hem_change")
    var hem_change: String? = ""
    @ColumnInfo(name = "phenno")
    var phenno: String? = ""
    @ColumnInfo(name = "genetic_evident")
    var genetic_evident: String? = ""
    @ColumnInfo(name = "cut_evident")
    var cut_evident: String? = ""
    @ColumnInfo(name = "other_evident_type")
    var other_evident_type: String? = ""

    @ColumnInfo(name = "last_check")
    var last_check: Boolean? = false
    @ColumnInfo(name = "last_evidence_complete")
    var last_evidence_complete: Boolean? = false
    @ColumnInfo(name = "camera_event")
    var camera_event: Boolean? = false

}