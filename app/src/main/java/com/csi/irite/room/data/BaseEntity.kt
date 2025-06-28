package com.csi.irite.room.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseEntity {
    var createdat: Long? = null
    var updatedat: Long? = null
    var issynced: Boolean = false
    var refkey: String? = null
}