import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.csi.irite.room.dao.AssetReportDao
import com.csi.irite.room.dao.BomReportDao
import com.csi.irite.room.dao.CheckListHeadDao
import com.csi.irite.room.dao.ChecklistSaveDao
import com.csi.irite.room.dao.DistrictDao
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.dao.EvidenceDao
import com.csi.irite.room.dao.FireReportDao
import com.csi.irite.room.dao.HistoryDao
import com.csi.irite.room.dao.LifeReportDao
import com.csi.irite.room.data.BaseEntity
import com.csi.irite.room.data.EventReport
import com.csi.irite.sync.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.mapsforge.map.model.com.csi.irite.room.dao.EvidentBagDao
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class SyncRepository(
    private val assetReportDao: AssetReportDao,
    private val bomReportDao: BomReportDao,
    private val checkListHeadDao: CheckListHeadDao,

    private val checklistSaveDao: ChecklistSaveDao,
    private val eventReportDao: EventReportDao,

    private val evidenceDao: EvidenceDao,
    private val evidentBagDao: EvidentBagDao,
    private val fireReportDao: FireReportDao,

    private val lifeReportDao: LifeReportDao,
    private val districtDao: DistrictDao,
    private val historyDao: HistoryDao,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestamp(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        return formatter.format(Instant.ofEpochMilli(timestamp))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun syncAll() {
        event()
        asset()
        bom()
        cheklistHead()
        ChecklistSave()
        district()
        evidence()
        evidentBag()
        fireReport()
        history()
        LifeReport()
    }

    suspend fun event() {
        upload("eventUpload", eventReportDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadEventReport(it)
        }

        downloadAndSync(
            tag = "eventDownload",
            apiCall = { RetrofitInstance.api.downloadEventReport() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },   // This is from local Room data
            findLocalByRefKey = { refkey -> eventReportDao.findByRefKey(refkey) },
            insert = { eventReportDao.insertAll(it) },
            update = {eventReportDao.updateAll(it)}
        )
    }

    suspend fun asset(){
        upload("assetUpload", assetReportDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadAssetReport(it)
        }
        downloadAndSync(
            tag = "assetDownload",
            apiCall = { RetrofitInstance.api.downloadAssetReport() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> assetReportDao.findByRefKey(refkey) },
            insert = { assetReportDao.insertAll(it) },
            update = { assetReportDao.updateAll(it) }
        )
    }

    suspend fun bom(){
        upload("bomUpload", bomReportDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadBomReport(it)
        }
        downloadAndSync(
            tag = "bomDownload",
            apiCall = { RetrofitInstance.api.downloadBomReport() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> bomReportDao.findByRefKey(refkey) },
            insert = { bomReportDao.insertAll(it) },
            update = { bomReportDao.updateAll(it) }
        )
    }

    suspend fun cheklistHead(){
        upload("checkListHeadUpload", checkListHeadDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadCheckListHead(it)
        }
        downloadAndSync(
            tag = "checkListHeadDownload",
            apiCall = { RetrofitInstance.api.downloadCheckListHead() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> checkListHeadDao.findByRefKey(refkey) },
            insert = { checkListHeadDao.insertAll(it) },
            update = { checkListHeadDao.updateAll(it) }
        )
    }

    suspend fun ChecklistSave(){
        upload("checklistSaveUpload", checklistSaveDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadChecklistSave(it)
        }
        downloadAndSync(
            tag = "checklistSaveDownload",
            apiCall = { RetrofitInstance.api.downloadChecklistSave() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> checklistSaveDao.findByRefKey(refkey) },
            insert = { checklistSaveDao.insertAll(it) },
            update = { checklistSaveDao.updateAll(it) }
        )
    }

    suspend fun district(){
        upload("districtUpload", districtDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadDistrict(it)
        }
        downloadAndSync(
            tag = "districtDownload",
            apiCall = { RetrofitInstance.api.downloadDistrict() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> districtDao.findByRefKey(refkey) },
            insert = { districtDao.insertAll(it) },
            update = { districtDao.updateAll(it) }
        )
    }

    suspend fun evidence(){
        upload("evidenceUpload", evidenceDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadEvidence(it)
        }
        downloadAndSync(
            tag = "evidenceDownload",
            apiCall = { RetrofitInstance.api.downloadEvidence() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> evidenceDao.findByRefKey(refkey) },
            insert = { evidenceDao.insertAll(it) },
            update = { evidenceDao.updateAll(it) }
        )
    }

    suspend fun evidentBag(){
        upload("evidentBagUpload", evidentBagDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadEvidentBag(it)
        }
        downloadAndSync(
            tag = "evidentBagDownload",
            apiCall = { RetrofitInstance.api.downloadEvidentBag() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> evidentBagDao.findByRefKey(refkey) },
            insert = { evidentBagDao.insertAll(it) },
            update = { evidentBagDao.updateAll(it) }
        )
    }

    suspend fun fireReport(){
        upload("fireReportUpload", fireReportDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadFireReport(it)
        }
        downloadAndSync(
            tag = "fireReportDownload",
            apiCall = { RetrofitInstance.api.downloadFireReport() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> fireReportDao.findByRefKey(refkey) },
            insert = { fireReportDao.insertAll(it) },
            update = { fireReportDao.updateAll(it) }
        )
    }

    /*suspend fun KeepEvidence(){
        upload("keepEvidenceUpload", keepEvidenceDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadKeepEvidence(it)
        }
        downloadAndSync(
            tag = "keepEvidenceDownload",
            apiCall = { RetrofitInstance.api.downloadKeepEvidence() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> keepEvidenceDao.findByRefKey(refkey) },
            insert = { keepEvidenceDao.insertAll(it) },
            update = { keepEvidenceDao.updateAll(it) }
        )
    }*/

    suspend fun LifeReport(){
        upload("lifeReportUpload", lifeReportDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadLifeReport(it)
        }
        downloadAndSync(
            tag = "lifeReportDownload",
            apiCall = { RetrofitInstance.api.downloadLifeReport() },
            getRefKey = { it.refkey },
            getUpdatedAt = { it.updatedat ?: 0L },
            findLocalByRefKey = { refkey -> lifeReportDao.findByRefKey(refkey) },
            insert = { lifeReportDao.insertAll(it) },
            update = { lifeReportDao.updateAll(it) }
        )
    }

    suspend fun history(){
        upload("HistoryUpload", historyDao.getUnsyncedData()) {
            RetrofitInstance.api.uploadHistory(it)
        }
    }

    private suspend fun <T> upload(tag: String, data: List<T>, apiCall: suspend (List<T>) -> Response<Unit>) {
        if (data.isEmpty()) return

        try {
            val response = apiCall(data)
            if (response.isSuccessful) {
                Log.d("syncLog", "$tag sync successful")
            } else {
                Log.e("syncLog", "$tag HTTP error: ${response.code()} ${response.message()}")
            }
        } catch (e: SocketTimeoutException) {
            Log.e("syncLog", "$tag timeout: ${e.message}")
        } catch (e: IOException) {
            Log.e("syncLog", "$tag network error: ${e.message}")
        } catch (e: Exception) {
            Log.e("syncLog", "$tag unexpected error: ${e.message}")
        }
    }

    suspend fun <T> downloadAndSync(
        tag: String,
        apiCall: suspend () -> Response<List<T>>,
        getRefKey: (T) -> String?,
        getUpdatedAt: (T) -> Long,
        findLocalByRefKey: suspend (String) -> T?,
        insert: suspend (T) -> Unit,
        update: suspend (T) -> Unit
    ) {
        Log.d("syncLogDownload", "Calling $tag")

        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val remoteData = response.body() ?: emptyList()
                Log.d("syncLogDownload", "$tag received ${remoteData.size} items")

                download(
                    tag = tag,
                    remoteData = remoteData,
                    getRefKey = getRefKey,
                    getUpdatedAt = getUpdatedAt,
                    findLocalByRefKey = findLocalByRefKey,
                    insert = insert,
                    update = update
                )
            } else {
                Log.e("syncLogDownload", "$tag failed: HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("syncLogDownload", "$tag error: ${e.message}", e)
        }
    }

    suspend fun <T> download(
        tag: String,
        remoteData: List<T>,
        getRefKey: (T) -> String?,             // Now supports all data classes
        getUpdatedAt: (T) -> Long?,
        findLocalByRefKey: suspend (String) -> T?,
        insert: suspend (T) -> Unit,
        update: suspend (T) -> Unit
    ) {
        val gson = Gson()
        if (remoteData.isEmpty()) return

        for (item in remoteData) {
            try {

                val refKey = getRefKey(item) ?: continue
                val local = findLocalByRefKey(refKey)
                if (local == null) {
                    Log.d(tag, "$tag inserted: $refKey"+" "+item.toString())
                    insert(item)
                } else {
                    val localUpdatedAt = getUpdatedAt(local) ?: 0L
                    val remoteUpdatedAt = getUpdatedAt(item) ?: 0L

                    if (remoteUpdatedAt > localUpdatedAt) {
                        Log.d(tag, "$tag updated with remote: $refKey"+" "+item.toString())
                        update(item)
                    } else {
                        Log.d(tag, "$tag skipped: $refKey")
                    }
                }
            } catch (e: Exception) {
                Log.e("syncLog", "$tag error for item: ${e.message}")
            }
        }
    }

}
