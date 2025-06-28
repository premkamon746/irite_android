package com.csi.irite.unuse

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.csi.irite.AssetFormFragment
import com.csi.irite.EvidenceBagFragment
import com.csi.irite.R
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.database.AppDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class MyEventReportRecyclerViewAdapter(
    private val values: List<EventReport>, val fm:FragmentManager)
    : RecyclerView.Adapter<MyEventReportRecyclerViewAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    var context: Context? = null
    var db: AppDatabase? = null

    var line:Float = 125f
    var vspace = 23f
    var normal_x = 67f

    lateinit var canvas:Canvas
    lateinit var paint: Paint

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_evr_item, parent, false)

        db = Room.databaseBuilder(
            context!!.applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        return ViewHolder(itemView)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.item_number.text = item.head
        holder.create_datetime.text = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm",
            Locale("th", "TH")
        ).format(convertLongToDateTime(item.create_datetime))
        holder.report_write.text = item.report_write
        holder.report_channel.text = item.report_channel

        val report_type = if (item.report_type != "") item.report_type else "ระบุเหตุที่รับแจ้ง!!"
        holder.report_type.text = report_type
        holder.event_report_id.text = item.uid.toString()


        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position, item)
        }/**/

        holder.formBtn.setOnClickListener {
            val type = item.report_type
            when (type) {
                "ทรัพย์" -> loadFragment(AssetFormFragment(),item.uid)
                "ชีวิต" -> loadFragment(LifeFormFragment(),item.uid)
                "ระเบิด" -> loadFragment(BomFormFragment(),item.uid)
                "เพลิงไหม้" -> loadFragment(FireFormFragment(),item.uid)
            }/**/
        }/**/

        holder.evidenceButton.setOnClickListener (View.OnClickListener { view ->
            loadFragment(EvidenceBagFragment(), item.uid)
        })/**/

        holder.transferButton.setOnClickListener (View.OnClickListener { view ->
            //loadFragment(TransferEvidenceFragment(), item.uid)
            //generatePdfAndPrint(item)
        })/**/


    }

    private  fun loadFragment(fragment: Fragment, uid: Long = 0){
        val args = Bundle()
        args.putLong("uid", uid) // Example parameter: key-value pair
        fragment.arguments = args

        val fragmentManager = fm
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.nav_host_fragment,fragment)
        transaction.commit()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertLongToDateTime(epochTime: Long?): LocalDateTime {
        return LocalDateTime.ofInstant(epochTime?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault())
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val event_report_id : TextView = itemView.findViewById(R.id.event_report_id)
        val item_number: TextView = itemView.findViewById(R.id.item_number)
        val create_datetime: TextView = itemView.findViewById(R.id.create_datetime)
        val report_write: TextView = itemView.findViewById(R.id.report_write)
        val report_channel: TextView = itemView.findViewById(R.id.report_channel)
        val report_type: TextView = itemView.findViewById(R.id.report_type)
        val formBtn : Button = itemView.findViewById(R.id.formButton)
        val evidenceButton : Button = itemView.findViewById(R.id.evidenceButton)
        val transferButton : Button = itemView.findViewById(R.id.transferButton)

        override fun toString(): String {
            return super.toString() + " '" + item_number.text + "'"
        }


    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: EventReport)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generatePdfAndPrint(ev: EventReport) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var  page = pdfDocument.startPage(pageInfo)

        canvas = page.canvas
        paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 12f
        paint.strokeWidth = 2f

        //draw logo
        val originalLogo: Bitmap = BitmapFactory.decodeResource(context!!.resources,
            R.drawable.forensic_sci_square
        )
        val resizedBitmap: Bitmap = Bitmap.createScaledBitmap(originalLogo, 120, 120, true)
        canvas.drawBitmap(resizedBitmap, 250f, 20f, paint)


        val create_date = Instant.ofEpochMilli(ev.report_known_datetime!!).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val day =  DateTimeFormatter.ofPattern("dd").format(create_date)
        val month =  DateTimeFormatter.ofPattern("MMM").format(create_date)
        val year =  DateTimeFormatter.ofPattern("yyyy").format(create_date)

        val oneDate =  DateTimeFormatter.ofPattern("dd MMM yyyy").format(create_date)
        var oneTime = DateTimeFormatter.ofPattern("HH:mm").format(create_date)



        canvas.drawText("เลขรับที่/เลขรายงาน "+ev.report_number, 375f, line, paint)
        line+=vspace+20
        canvas.drawText("แบบการตรวจเก็บและส่งมอบวัตถุพยาน", 213f, line, paint)
        line+=vspace
        canvas.drawText("เขียนที่ "+ev.report_write, 375f, line, paint)
        line+=vspace
        canvas.drawText("วันที่ "+day+"             เดือน"+month+"                พ.ศ."+year+543, 312f, line, paint)
        line = 188f+vspace
        canvas.drawText("รับแจ้งเหตุ จาก ท้องที่ สน/สภ. "+ev.report_place, normal_x, line, paint)
        line+=vspace
        canvas.drawText("ที่                                              ลง                          เมื่อวันที่ "+day+"         เดือน  "+month+"                พ.ศ. "+year+543, normal_x, line, paint)
        line+=vspace
        canvas.drawText("คดี "+ev.report_type+"                                          สถานที่เกิดเหตุ "+ev.report_place, normal_x, line, paint)
        line+=vspace
        canvas.drawText("", normal_x, line, paint)


        line+=vspace
        canvas.drawText("วัน เวลา ทราบเหตุ/เกิดเหตุ "+oneDate+"                                   เวลาประมาณ "+oneTime, normal_x, line, paint)
        line+=vspace
        canvas.drawText("วัน เวลา ตรวจสถานที่เกิดเหตุ/ตรวจเก็บวัตถุพยาน "+oneDate+" เวลาประมาณ "+oneTime, normal_x, line, paint)
        line+=vspace
        canvas.drawText("ผู้เสียหาย/ผู้เสียชีวิต                                   อายุประมาณ ", normal_x, line, paint)
        line+=vspace
        canvas.drawText("ชื่อพนักงานสอบสวนเจ้าของคดี "+ev.report_officer, normal_x, line, paint)
        line+=vspace
        canvas.drawText("กสก                          /พฐ.จว.", normal_x, line, paint)
        line+=vspace
        canvas.drawText("จำนวน  รายการดังนี้", normal_x, line, paint)

        line+=vspace
        // Define the table structure

        var startX = normal_x
        var startY = line
        var cellWidth = 167f
        var cellHeight = 23f
        var numCols = 3
        var righ_col = 310f

        line+=vspace-7
        canvas.drawText("  ลำดับ                                                         รายการ                                         การตรวจพิสูจน์", normal_x+10, line, paint)

        val evidenceDao = db!!.evidenceDao()
        val allEvidence = evidenceDao.getByIds(ev.uid, true)

        // Draw the table
        var i = 0
        for (allev in allEvidence){
            line+=vspace
            canvas.drawLine(startX, startY + i * cellHeight, startX + numCols * cellWidth, startY + i * cellHeight, paint)
            canvas.drawText(i.toString(), 80f, line, paint)
            canvas.drawText(allev.evidence.toString(), 150f, line, paint)
            canvas.drawText("", 200f, line, paint)
            i++
            //page = newPage(pdfDocument, page, pageInfo, line, true, canvas, paint, i)
        }
        canvas.drawLine(startX, startY + i * cellHeight, startX + numCols * cellWidth, startY + i * cellHeight, paint)
        i++
        canvas.drawLine(startX, startY + i * cellHeight, startX + numCols * cellWidth, startY + i * cellHeight, paint)

        canvas.drawLine(startX , startY, startX , startY + i * cellHeight, paint)
        canvas.drawLine(startX+70 , startY, startX+70 , startY + i * cellHeight, paint)
        canvas.drawLine(startX+350 , startY, startX+350 , startY + i * cellHeight, paint)
        canvas.drawLine(startX+500 , startY, startX+500 , startY + i * cellHeight, paint)
        //canvas.drawLine(startX+700 , startY, startX+700 , startY + numRows * cellHeight, paint)

        line+=vspace
        //page = newPage(pdfDocument, page, pageInfo, line, false, canvas, paint)
        canvas.drawText("วัตถุพยานลำดับที่"+    "ได้ส่งมอบให้กับพนักงานสอบสวนเจ้าของคลีเพื่อดำเนินการต่อไป", normal_x, line, paint)
        line+=vspace
        //page = newPage(pdfDocument, page, pageInfo, line, false, canvas, paint)
        canvas.drawText("(ลงชื่อ).........................................................ผู้รับมอบ", righ_col, line, paint)
        line+=vspace
        //page = newPage(pdfDocument, page, pageInfo, line, false, canvas, paint)
        canvas.drawText("(..............................................................)", righ_col+30, line, paint)
        line+=vspace
        //page = newPage(pdfDocument, page, pageInfo, line, false, canvas, paint)
        canvas.drawText("ตำแหน่ง..........................................................", righ_col, line, paint)
        line+=vspace
        //page = newPage(pdfDocument, page, pageInfo, line, false, canvas, paint)
        canvas.drawText("วันที่.................................เวลา........................น.", righ_col, line, paint)
        line+=vspace
        //page = newPage(pdfDocument, page, pageInfo, line, false, canvas, paint)
        canvas.drawText("(ลงชื่อ).........................................................ผู้มอบ", righ_col, line, paint)
        line+=vspace
        //page = newPage(pdfDocument, page, pageInfo, line, false, canvas, paint)
        canvas.drawText("(..............................................................)", righ_col+30, line, paint)

        pdfDocument.finishPage(page)

        val filePath = context?.getExternalFilesDir(null)?.absolutePath + "/report.pdf"
        val file = File(filePath)
        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()
            fileOutputStream.close()
            printContent(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun newPage(pdfDocument:PdfDocument, page:PdfDocument.Page, pageInfo:PdfDocument.PageInfo, numrow:Float, tbHeader:Boolean, canvas: Canvas, paint:android.graphics.Paint,i:Int = 0): PdfDocument.Page{

        /*if(tbHeader){
            //build table header
            line+=vspace-7
            canvas.drawText("  ลำดับ                                                                    รายการ                                         การตรวจพิสูจน์", normal_x+10, line, paint)

            canvas.drawLine(startX , startY, startX , startY + i * cellHeight, paint)
            canvas.drawLine(startX+70 , startY, startX+70 , startY + i * cellHeight, paint)
            canvas.drawLine(startX+350 , startY, startX+350 , startY + i * cellHeight, paint)
            canvas.drawLine(startX+500 , startY, startX+500 , startY + i * cellHeight, paint)
        }*/

        if(numrow > 500) {
            line = 125f
            vspace = 23f
            normal_x = 67f

            pdfDocument.finishPage(page)
            var pageNew = pdfDocument.startPage(pageInfo)

            return pageNew
        }
        return page
    }

    private fun printContent(file: File) {
        val printManager = context?.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onStart() {
                PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).build()
            }

            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback,
                extras: Bundle?
            ) {

                if (cancellationSignal?.isCanceled == true) {
                    callback.onLayoutCancelled()
                    return
                }

                val info = PrintDocumentInfo.Builder(file.name)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()

                callback.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<PageRange>,
                destination: ParcelFileDescriptor,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback
            ) {
                var out: FileOutputStream? = null
                var input: FileInputStream? = null
                try {
                    input = FileInputStream(file)
                    out = FileOutputStream(destination.fileDescriptor)
                    input.copyTo(out)
                    callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback.onWriteFailed(e.message)
                } finally {
                    try {
                        input?.close()
                        out?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFinish() {}
        }

        val attrib = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT)
            .build()

        val printJob = printManager.print("PrintJobName", printAdapter, attrib)
        if (printJob.isFailed) {
            Toast.makeText(context, "Printing failed", Toast.LENGTH_SHORT).show()
        }
    }
}