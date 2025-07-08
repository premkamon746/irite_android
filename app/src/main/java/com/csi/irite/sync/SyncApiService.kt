package com.csi.irite.sync

import com.csi.irite.room.data.AssetReport
import com.csi.irite.room.data.BomReport
import com.csi.irite.room.data.CheckListHead
import com.csi.irite.room.data.ChecklistSave
import com.csi.irite.room.data.District
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.Evidence
import com.csi.irite.room.data.EvidentBag
import com.csi.irite.room.data.FireReport
import com.csi.irite.room.data.History
import com.csi.irite.room.data.KeepEvidence
import com.csi.irite.room.data.LifeReport
import com.csi.irite.room.data.RunningNumberResponse
import com.csi.irite.room.data.TranferEvidence
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SyncApiService {
    @POST("syncdata/eventreport")
    suspend fun uploadEventReport(@Body forms: List<EventReport>): Response<Unit>
    @GET("syncdata/eventreport")
    suspend fun downloadEventReport(): Response<List<EventReport>>

    @POST("syncdata/assetreport")
    suspend fun uploadAssetReport(@Body forms: List<AssetReport>): Response<Unit>
    @GET("syncdata/assetreport")
    suspend fun downloadAssetReport(): Response<List<AssetReport>>

    // bomreport
    @POST("syncdata/bomreport")
    suspend fun uploadBomReport(@Body forms: List<BomReport>): Response<Unit>
    @GET("syncdata/bomreport")
    suspend fun downloadBomReport(): Response<List<BomReport>>

    // CheckListHead
    @POST("syncdata/checklisthead")
    suspend fun uploadCheckListHead(@Body forms: List<CheckListHead>): Response<Unit>
    @GET("syncdata/checklisthead")
    suspend fun downloadCheckListHead(): Response<List<CheckListHead>>

    // ChecklistSave
    @POST("syncdata/checklistsave")
    suspend fun uploadChecklistSave(@Body forms: List<ChecklistSave>): Response<Unit>
    @GET("syncdata/checklistsave")
    suspend fun downloadChecklistSave(): Response<List<ChecklistSave>>

    // District
    @POST("syncdata/district")
    suspend fun uploadDistrict(@Body forms: List<District>): Response<Unit>
    @GET("syncdata/district")
    suspend fun downloadDistrict(): Response<List<District>>

    // Evidence
    @POST("syncdata/evidence")
    suspend fun uploadEvidence(@Body forms: List<Evidence>): Response<Unit>
    @GET("syncdata/evidence")
    suspend fun downloadEvidence(): Response<List<Evidence>>

    // EvidentBag
    @POST("syncdata/evidentbag")
    suspend fun uploadEvidentBag(@Body forms: List<EvidentBag>): Response<Unit>
    @GET("syncdata/evidentbag")
    suspend fun downloadEvidentBag(): Response<List<EvidentBag>>

    // FireReport
    @POST("syncdata/firereport")
    suspend fun uploadFireReport(@Body forms: List<FireReport>): Response<Unit>
    @GET("syncdata/firereport")
    suspend fun downloadFireReport(): Response<List<FireReport>>

    // KeepEvidence
    @POST("syncdata/keepevidence")
    suspend fun uploadKeepEvidence(@Body forms: List<KeepEvidence>): Response<Unit>
    @GET("syncdata/keepevidence")
    suspend fun downloadKeepEvidence(): Response<List<KeepEvidence>>

    // LifeReport
    @POST("syncdata/lifereport")
    suspend fun uploadLifeReport(@Body forms: List<LifeReport>): Response<Unit>
    @GET("syncdata/lifereport")
    suspend fun downloadLifeReport(): Response<List<LifeReport>>

    // TranferEvidence
    @POST("syncdata/tranferevidence")
    suspend fun uploadTranferEvidence(@Body forms: List<TranferEvidence>): Response<Unit>
    @GET("syncdata/tranferevidence")
    suspend fun downloadTranferEvidence(): Response<List<TranferEvidence>>

    // history only upload on download
    @POST("syncdata/hisotry")
    suspend fun uploadHistory(@Body forms: List<History>): Response<Unit>

    @GET("syncdata/unique/{param}")
    suspend fun getSyncRunningNumber(@Path("param") param: String): Response<RunningNumberResponse>
}