package com.csi.irite.datainit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.csi.irite.SyncIdManager.getDeviceId
import com.csi.irite.room.data.ChecklistSave
import com.csi.irite.room.data.District
import com.csi.irite.room.database.AppDatabase
import java.time.LocalDateTime
import java.time.ZoneId

class InitCheckList(val db: AppDatabase) {

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertInit(headId:Long){
        val checklistSaveDao = db.checklistSaveDao()
        val datetime = System.currentTimeMillis()

            //val groupList: MutableList<Checklist> = mutableListOf()
            //groupList.add(ChecklistSave(0,"ระบบไฟฟ้าและแบตเตอรี่","-แบตเตอรี่รถน้ํากลั่นแบตเตอรี่\n-วิทยุสื่อสารประจํารถ",1,"1"))
            checklistSaveDao.insertAll(
                ChecklistSave(
                    0,
                    headId,
                    "ระบบไฟฟ้าและแบตเตอรี่",
                    "-แบตเตอรี่รถน้ํากลั่นแบตเตอรี่\n-วิทยุสื่อสารประจํารถ",
                    1,
                    "1",
                    "",
                    "",
                    false,

                    datetime
                )
            )
            checklistSaveDao.insertAll(
                ChecklistSave(
                    0,
                    headId,
                    "ยาง",
                    "-ไฟหน้า-ไฟท้ายไฟเบรก-ไฟเลี้ยวไฟฉุกเฉิน\n-ไฟวับวาบและสัญญาณไฟ",
                    2,
                    "1",
                    "",
                    "",
                    false,
                    
                    datetime
                )
            )
            checklistSaveDao.insertAll(
                ChecklistSave(
                    0,
                    headId,
                    "ระบบส่องสว่าง",
                    "-ไฟหน้า-ไฟท้ายไฟเบรก-ไฟเลี้ยวไฟฉุกเฉิน\n-ไฟวับวาบและสัญญาณไฟ",
                    3,
                    "1",
                    "",
                    "",
                    false,
                    
                    datetime
                )
            )
            checklistSaveDao.insertAll(
                ChecklistSave(
                    0,
                    headId,
                    "สภาพรถ",
                    "-สภาพรถ\n-กระจกมองข้างกระจกมองหลัง",
                    4,
                    "1",
                    "",
                    "",
                    false,
                    
                    datetime
                )
            )
            checklistSaveDao.insertAll(ChecklistSave(0,headId, "แตร", "-แตรรถ", 5, "1","","",false,datetime))
            checklistSaveDao.insertAll(
                ChecklistSave(
                    0,
                    headId,
                    "ระบบเบรก",
                    "ระดับปริมาณน้ํามันเบรก ระบบเบรก",
                    6,
                    "1",
                    "",
                    "",
                    false,
                    
                    datetime
                )
            )
            checklistSaveDao.insertAll(ChecklistSave(0, headId,"ใบปัดน้ําฝน", "-ใบปัดและน้ําปัดน้ําฝน", 7, "1","","",false,datetime))
            checklistSaveDao.insertAll(
                ChecklistSave(
                    0,
                    headId,
                    "ของเหลวต่างๆ",
                    "-ปริมาณน้ํามันเชื้อเพลิงหรือแก๊ส/ปริมาณน้ํามันเครื่อง\n-ระดับปริมาณน้ํามันพวงมาลัยพาวเวอร์น้ําหล่อเย็น หม้อน้ำ",
                    8,
                    "1",
                    "",
                    "",
                    false,
                    
                    datetime
                )
            )
            checklistSaveDao.insertAll(
                ChecklistSave(
                    0,
                    headId,
                    "ระบบปรับอากาศ",
                    "-เครื่องปรับอากาศภายในรถ",
                    9,
                    "1",
                    "",
                    "",
                    false,
                    
                    datetime
                )
            )
            checklistSaveDao.insertAll(ChecklistSave(0, headId,"การทดลองขับ", "", 10, "1","","",false,datetime))



            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดอุปกรณ์เก็บลายนิ้วมือ/ฝ่ามือ/ฝ่าเท้า","- ผงฝุ่นดำ 2 ขวด, ผงฝุ่นขาว 1 ขวด, ผงฝุ่นแม่เหล็ก 1 ขวด, ผงฝุ่นอลูมิเนียม 1 ขวด\n" +
                    "- แปรงขนกระรอก/ขนอูฐ 3อัน, แปรงขนกระต่าย 2 อันแปรงแม่เหล็ก 2 อัน, แปรงขนไฟเบอร์ 1 อัน\n" +
                    "- ฝ่าเท้าแว่นขยาย 1 อัน/เทปเก็บรอยนิ้วมือแฝง 1 นิ้ว 1 ม้วน 2 นิ้ว 1 ม้วน\n" +
                    "- กระดาษเก็บรอยลายนิ้วมือแฝง สีขาว 30 แผ่น สีดำ 15 แผ่น, กระดาษ A4 15 แผ่น",1,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดอุปกรณ์เครื่องเขียน","-ปากกาเคมีสีน้ําเงิน/ดํา 1 ด้าม, ปากกาลูกลื่นสีน้ําเงิน 1 ด้าม\n" +
                    "-กรรไกร 2 เล่ม, มีดคัตเตเตอร์ 1 เล่ม\n",2, "2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดหล่อรอย","- ชุดหล่อรอยเครื่องมือ เช่น Microsil 1 ชุด\n" +
                    "- ชุดหล่อรอยเท้าพร้อมปูนปาสเตอร์ 1 ชุด, กรอบอลูมิเนียม 1 อัน\n" +
                    "- กระบอกตวงพลาสติกขนาด 1 L 1 อัน, ถ้วยพลาสติกเล็ก 1 อัน\n",3,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดอุปกรณ์วัดขนาด\n","- สเกลแบบฉากขนาดใหญ่ 5 อัน, สเกลแบบฉากขนาดเล็ก 5 อัน\n" +
                    "- ขนาดสติ๊กเกอร์สเกล 5 ซม. 5 แผ่น, สติ๊กเกอร์ตัวเลข 5 แผ่น\n",4,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดตรวจเก็บDNAและทดสอบคราบโลหิต\n","- สําลีพันก้านเก็บสารพันธุกรรมแบบปลอดเชื้อ 60 ก้าน\n" +
                    "- กล่องบรรจุสําลีพันก้านเก็บสารพันธุกรรม 30 กล่อง\n" +
                    "- สมุดยินยอมการตรวจเก็บDNAและการพิมพ์มือ 1 เล่ม\n" +
                    "- Hexagon 5 ชุด, BlueStar 1 ชุด, กระบอกฉีด 1 อัน\n" +
                    "- น้ํากลั่น 10 ขวด, Forceps 5 อัน, กระดาษกรอง 1 กล่อง\n" +
                    "- ก้านสำลี 1 ห่อ, Hemastix/Phenolphthalein 1 กล่อง\n",5,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดเก็บเขม่าปืน","- 5% Nitric Acid, ซองเก็บเขม่าปืน 10 ซอง\n" +
                    "- ถุงซิป 50 ถุง, ก้านสําลี1ห่อ\n",6,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดตรวจเก็บสารระเบิดจากตัวบุคคล","- Alcohol Pads 20 แผ่น, ผ้าก็อต/ก้านสําลี 20 แผ่น\n" +
                    "- ซองเก็บวัตถุพยานพร้อมถุงซิป 20 ซอง, น้ํากลั่น 10 ขวด",7,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดตรวจเก็บสารระเบิดจากวัตถุ","- Acetone 1 ขวด, ผ้าก็อต/ก้านสําลี 20 แผ่น, ซองเก็บวัตถุพยานพร้อมถุงซิป 20 ซอง, น้ํากลั่น 20 ขวด",8,"1","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดระบุตําแหน่งวัตถุพยาน","- ป้ายหมายเลข 1-20 พร้อมลูกศร 4 อัน 1 ชุด, ป้าย A-J 1 ชุด\n" +
                    "- วงแหวนครอบวัตถุพยาน 30 อัน, สติ๊กเกอร์ตัวเลข 5 แผ่น",9,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"วัสดุหีบห่อ","- เทปปิดวัตถุพยานแบบกระดาษ 3 ม้วน และแบบพลาสติก 2 ม้วน\n" +
                    "- ถุงซิป ขนาดเล็ก,กลาง,ใหญ่อย่างละ 20 ถุง\n" +
                    "- ถุงพลาสติกเก็บวัตถุพยาน ขนาดเล็ก,กลาง,ใหญ่อย่างละ 20 ถุง\n" +
                    "- ซองกระดาษเก็บวัตถุพยาน ขนาดเล็ก,กลาง,ใหญ่อย่างละ 15 ซอง\n" +
                    "- กระปุกพลาสติก 5 ใบ, กระป๋องโลหะ 5 ใบ\n" +
                    "- ขวดแก้วพร้อมช้อนตัก 5 ชุด, กระดาษรองวัตถุพยาน 10 แผ่น\n",10,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดอุปกรณ์ป้องกันสถานที่เกิดเหตุ","-เครื่องปรับอากาศภายในรถ",11,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"การทดลองขับ","",12,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ระบบปรับอากาศ","- ชุด Tyvex 3 ชุด, ถุงมือไนไตร 1 กล่อง, ถุงมือผ้าถัก 5 คู่\n" +
                    "- หน้ากากอนามัย 1 กล่อง, หน้ากาก N-95 3 อัน, ถุงคลุมถุงเท้า 20 คู่\n" +
                    "- หมวกคลุมผม 20 ชิ้น, ถุงมือป้องกันของมีคม 5 คู่\n" +
                    "\n",13,"2","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ชุดตรวจค้นหาวัตถุพยาน\n","- ตะแกรงร่อน 1 อัน, ชุดแม่เหล็ก 1 อัน, พลั่ว 3 เล่ม\n",14,"2","","",false,datetime))



            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ตัวกล้องและเลนส์","- ตัวกล้อง, สภาพปุ่ม ON/OFF, ปุ่มชัตเตอร์, ปุ่มฟังก์ชั่น\n" +
                    "- เลนส์ช่องมองภาพจอภาพ\n" +
                    "แฟลชเสริม1อัน\n" +
                    "- ทําความสะอาด\n",1,"3","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"แบตเตอรี่\n","แบตเตอรี่ 1 ชุด, แบตเตอรี่สํารอง 1 ชุด",2,"3","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"อุปกรณ์เสริม","สายกล้องขาตั้งกล้อง, กระเป๋ากล้อง, เมมโมรี่การ์ดสํารอง",3,"3","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"เมมโมรี่การ์ด","นําภาพลงคอมพิวเตอร์แล้วลบภาพในเมมโมรี่การ์ด",4,"3","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"การทดสอบกล้อง","",5,"3","","",false,datetime))



            checklistSaveDao.insertAll(ChecklistSave(0,headId,"เครื่องตรวจโลหะ ชุดที่.....","ตัวเครื่องฉายแสงฟิลเตอร์แว่นตาสีแดง/เหลือง/ส้มและใส\n" +
                    "- แบตเตอรี่\n",1,"4","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"POLY LIGHT ชุดที่.....","- ตัวเครื่องลอกลายฝุ่นแผ่นเหล็กแผ่นพลาสติก,ฟิล์มลอกลาย\n" +
                    "- ลูกกลิ้ง, ตัวต่อสายดิน, แบตเตอรี่\n",2,"4","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"เครื่องลอกลายฝุ่น ชุดที่.....","-ตัวเครื่องวัดระยะ\n" +
                    "- แบตเตอรี่\n",3,"4","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"เครื่องวัดระยะด้วยเลเซอร์ ชุดที่.....","-ไฟฉาย\n" +
                    "- แบตเตอรี่\n",4,"4","","",false,datetime))
            checklistSaveDao.insertAll(ChecklistSave(0,headId,"ไฟฉายส่องสว่างแรงสูง ชุดที่.....","ลงชื่อ.... เจ้าหน้าที่ตรวจสถานที่เกิดเหตุ\n",5,"4","","",false,datetime))

        //checklistSaveDao.insertAll(checklist)
    }

    fun getdata(): List<ChecklistSave>{
        val checklistSaveDao = db.checklistSaveDao()
        val checklists: List<ChecklistSave> = checklistSaveDao.getAll()
        for (checklist in checklists) {
            Log.d("debug01", checklist.head.toString())
            Log.d("debug01", checklist.listname.toString())
            Log.d("debug01", checklist.sorting.toString())
            Log.d("debug01", checklist.gropping.toString())
        }
        return checklists
    }

    fun insertDistinct():Int{

        val districtDao = db.districtDao()
        if(districtDao.getAllDistricts().count() > 0) {
            return 0
        }

        val districts = listOf(
            // Yala
            District(name = "เมืองยะลา", province = "ยะลา", provinceId = 1),
            District(name = "บันนังสตา", province = "ยะลา", provinceId = 1),
            District(name = "บาเจาะ", province = "ยะลา", provinceId = 1),
            District(name = "กรงปินัง", province = "ยะลา", provinceId = 1),
            District(name = "รามัน", province = "ยะลา", provinceId = 1),
            District(name = "ยะหา", province = "ยะลา", provinceId = 1),
            District(name = "ธารโต", province = "ยะลา", provinceId = 1),
            District(name = "กาบัง", province = "ยะลา", provinceId = 1),
            // Pattani
            District(name = "เมืองปัตตานี", province = "ปัตตานี", provinceId = 2),
            District(name = "โคกโพธิ์", province = "ปัตตานี", provinceId = 2),
            District(name = "ยะหริ่ง", province = "ปัตตานี", provinceId = 2),
            District(name = "ยะรัง", province = "ปัตตานี", provinceId = 2),
            District(name = "ปะนาเระ", province = "ปัตตานี", provinceId = 2),
            District(name = "มายอ", province = "ปัตตานี", provinceId = 2),
            District(name = "ทุ่งยางแดง", province = "ปัตตานี", provinceId = 2),
            District(name = "สายบุรี", province = "ปัตตานี", provinceId = 2),
            District(name = "ไม้แก่น", province = "ปัตตานี", provinceId = 2),
            District(name = "กะพ้อ", province = "ปัตตานี", provinceId = 2),
            District(name = "หนองจิก", province = "ปัตตานี", provinceId = 2),
            District(name = "แม่ลาน", province = "ปัตตานี", provinceId = 2),
            // Narathiwat
            District(name = "เมืองนราธิวาส", province = "นราธิวาส", provinceId = 3),
            District(name = "ตากใบ", province = "นราธิวาส", provinceId = 3),
            District(name = "บาเจาะ", province = "นราธิวาส", provinceId = 3),
            District(name = "ยี่งอ", province = "นราธิวาส", provinceId = 3),
            District(name = "ระแงะ", province = "นราธิวาส", provinceId = 3),
            District(name = "รือเสาะ", province = "นราธิวาส", provinceId = 3),
            District(name = "ศรีสาคร", province = "นราธิวาส", provinceId = 3),
            District(name = "สุไหงปาดี", province = "นราธิวาส", provinceId = 3),
            District(name = "สุไหงโก-ลก", province = "นราธิวาส", provinceId = 3),
            District(name = "แว้ง", province = "นราธิวาส", provinceId = 3),
            District(name = "สุคิริน", province = "นราธิวาส", provinceId = 3),
            District(name = "จะแนะ", province = "นราธิวาส", provinceId = 3),
            District(name = "เจาะไอร้อง", province = "นราธิวาส", provinceId = 3)
        )
        districtDao.insertAll(districts)
        return 0
    }

    fun generateSyncIdWithTimestamp(context: Context): String {
        val deviceId = getDeviceId(context)
        val timestamp = System.currentTimeMillis()
        return "$deviceId-$timestamp"
    }

}