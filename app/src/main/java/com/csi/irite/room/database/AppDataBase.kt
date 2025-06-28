package com.csi.irite.room.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.csi.irite.converter.DateConverter
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.dao.AssetReportDao
import com.csi.irite.room.dao.BomReportDao
import com.csi.irite.room.dao.ChecklistSaveDao
import com.csi.irite.room.dao.DistrictDao
import com.csi.irite.room.dao.EvidenceDao
import com.csi.irite.room.data.District
import com.csi.irite.room.data.Evidence
import com.csi.irite.room.dao.CheckListHeadDao
import com.csi.irite.room.dao.FireReportDao
import com.csi.irite.room.dao.HistoryDao
import com.csi.irite.room.dao.InspectionRecordDao
import com.csi.irite.room.dao.LifeReportDao
import com.csi.irite.room.dao.UserDao
import com.csi.irite.room.data.AssetReport
import com.csi.irite.room.data.BomReport
import com.csi.irite.room.data.CheckListHead
import com.csi.irite.room.data.ChecklistSave
import com.csi.irite.room.data.EvidentBag
import com.csi.irite.room.data.FireReport
import com.csi.irite.room.data.History
import com.csi.irite.room.data.InspectionRecord
import com.csi.irite.room.data.LifeReport
import com.csi.irite.room.data.User
import org.mapsforge.map.model.com.csi.irite.room.dao.EvidentBagDao

@Database(entities = [EventReport::class, AssetReport::class, LifeReport::class,Evidence::class, District::class,
                        ChecklistSave::class, CheckListHead::class, EvidentBag::class, BomReport::class, FireReport::class,
                        User::class, History::class, InspectionRecord::class], version = 111)

@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventReportDao(): EventReportDao
    //abstract fun assetFormDao(): AssetFormDao
    abstract fun evidenceDao(): EvidenceDao

    abstract fun districtDao(): DistrictDao
    abstract fun checkListHeadDao(): CheckListHeadDao
    abstract fun checklistSaveDao(): ChecklistSaveDao
    abstract fun evidentBagDao(): EvidentBagDao
    abstract fun assetReportDao(): AssetReportDao
    abstract fun lifeReportDao(): LifeReportDao
    abstract fun bomReportDao(): BomReportDao
    abstract fun fireReportDao(): FireReportDao
    abstract fun userDao(): UserDao
    abstract fun historyDao(): HistoryDao
    abstract fun inspectionRecordDao(): InspectionRecordDao

    /*@Migrations(from = 1, to = 2)
    abstract class Migration1To2 : Migration {
        override fun migrate(database: SupportSQLiteOpenHelper.SupportSQLiteDatabase) {
            // Implement logic to insert initial data
            val initialUsers = listOf(
                User(1, "John Doe"),
                User(2, "Jane Doe")
            )

            initialUsers.forEach { user ->
                database.insert(User::class.java, OnConflictStrategy.IGNORE, user)
            }
        }
    }*/
}