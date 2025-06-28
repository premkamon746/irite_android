package com.csi.irite

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
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
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.EventReport
import com.csi.irite.widget.LineItemDecoration
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.csi.irite.room.dao.EvidenceDao
import com.csi.irite.room.data.Evidence
import com.csi.irite.unuse.EvidenceItemRecyclerViewAdapter
import com.csi.irite.unuse.EvidencePrintFragment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class EvidenceBagFragment : BaseFragment() {
    private lateinit var adapter: EvidenceItemRecyclerViewAdapter
    private lateinit var evidenceDao: EvidenceDao
    private lateinit var eventReportDao: EventReportDao
    private lateinit var tableLayout: TableLayout
    //private lateinit var adapter:RecyclerView.Adapter<EvidenceItemRecyclerViewAdapter.ViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToThaiShortMonth(dateStr: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(dateStr, inputFormatter)

        val thaiShortMonths = mapOf(
            1 to "ม.ค.",
            2 to "ก.พ.",
            3 to "มี.ค.",
            4 to "เม.ย.",
            5 to "พ.ค.",
            6 to "มิ.ย.",
            7 to "ก.ค.",
            8 to "ส.ค.",
            9 to "ก.ย.",
            10 to "ต.ค.",
            11 to "พ.ย.",
            12 to "ธ.ค."
        )

        val day = date.dayOfMonth
        val month = thaiShortMonths[date.monthValue]
        val year = date.year

        return "$day $month $year"
    }

    val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            // Determine the span size based on your logic (e.g., all items have same width)
            return 1 // Adjust this value as needed
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_evidence_item_list, container, false)
        val recycleView = view.findViewById<RecyclerView>(R.id.evicence_list)

        /*val gridLayoutManager = GridLayoutManager(context, 11, GridLayoutManager.HORIZONTAL, false)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 4
            }
        }
        gridLayoutManager.spanSizeLookup = spanSizeLookup*/
        recycleView.layoutManager = LinearLayoutManager(requireContext())

        evidenceDao = db!!.evidenceDao()
        eventReportDao = db!!.eventReportDao()
        val allEvidence = evidenceDao.getByIds(uid, true)

        //tableLayout = view.findViewById<TableLayout>(R.id.tableLayout)
        //reloadTable(view,  allEvidence)

        adapter = EvidenceItemRecyclerViewAdapter(requireContext(),allEvidence)
        recycleView.adapter = adapter
        recycleView.addItemDecoration(
            LineItemDecoration(
                activity,
                LinearLayout.VERTICAL
            )
        )

        recycleView.setHasFixedSize(true);

        // Set the adapter
        /*if (recycleView is RecyclerView) {

            with(recycleView) {
            }
        }*/

        /**/adapter.setOnItemClickListener(object : EvidenceItemRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(view:View, item: Evidence) {
                showCustomDialog(view, item, "update")
            }
        })

        adapter.setOnItemDeleteClickListener(object : EvidenceItemRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(view:View, item: Evidence) {
                //showCustomDialog(view, item, "update")
                confirmDialog(uid, item)
            }
        })

        var addEvidenceButton = view.findViewById<Button>(R.id.addEvidenceButton)
        addEvidenceButton.setOnClickListener({
            val evBag = Evidence()
            showCustomDialog(view, evBag, "insert")
        })

        var printButton = view.findViewById<Button>(R.id.printButton)
        printButton.setOnClickListener({
            val report = eventReportDao.getActiveById(uid)
            //generatePdfAndPrint(allEvidence,report)
        })/**/

        var createEdBag = view.findViewById<Button>(R.id.createEdBag)
        createEdBag.setOnClickListener({
            loadFragment(EvidencePrintFragment(),uid)
        })
        return view
    }


    fun reloadTable(view:View,  allEvidence:MutableList<Evidence>){

        clearTableExceptHeader()
        // Adding a new row dynamically
        var count = 0
        for (ev in allEvidence) {
            count += 1
            val newRow = TableRow(requireContext())
            newRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            newRow.setPadding(10, 10, 10, 10)
            newRow.addView(createText(count.toString(),1f))
            newRow.addView(createText(ev.evidence.toString(),7f))
            newRow.addView(createText(ev.distance_1.toString()+"                        "+
                                           ev.distance_2.toString()+"                        "+
                                           ev.distance_3.toString()+"                        "+
                                           ev.distance_4.toString(),4f))
            //newRow.addView(createText(ev.distance_2.toString(),1f))
            //newRow.addView(createText(ev.distance_3.toString(),1f))
            //newRow.addView(createText(ev.distance_4.toString(),1f))
            newRow.addView(createText(ev.angle.toString(),2f))
            newRow.addView(createText(ev.remark.toString(),2f))
            val editButton = createButton("Edit",2f)
            //newRow.addView(editButton)

            /*editButton.setOnClickListener({
                showCustomDialog(view, ev, "update")
            })

            val deleteButton = createButton("Delete",2f)
            newRow.addView(deleteButton)

            deleteButton.setOnClickListener({
                confirmDialog(uid, ev)
            })*/
            tableLayout.addView(editButton)
            tableLayout.addView(newRow)
        }
    }

    private fun clearTableExceptHeader() {
        val childCount = tableLayout.childCount
        if (childCount > 2) {
            tableLayout.removeViews(1, childCount - 2)
        }
    }

    fun createButton(str:String,w:Float = 1.0f):Button{
        //style/Widget.AppCompat.Button.Borderless
        val styledContext: Context = ContextThemeWrapper(requireContext(), com.google.android.material.R.style.Widget_AppCompat_Button)

        val button = Button(styledContext).apply {
            text = str
            setBackgroundColor(Color.BLUE)
            setTextColor(Color.BLACK)
            textSize = 18f
            // Set layout parameters for the button
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                w
            ).apply {
                setMargins(8, 8, 8, 8)
            }
        }

        if(true) {
            val border = GradientDrawable()
            border.setColor(Color.WHITE) // Background color
            border.setStroke(2, Color.BLACK) // Border width and color
            //border.cornerRadius = 4f // Optional: corner radius

            // Apply the border to the TextView
            button.background = border
        }

        return button
    }

    fun createText(text:String,w:Float = 1.0f):TextView{
        val tv = TextView(context)
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT,
            w // Set weight to 1
        )
        tv.layoutParams = layoutParams
        tv.text = text
        tv.setTextColor(requireContext().resources.getColor(R.color.black)) // Assuming black is defined in colors.xml
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER // Left alignmen
        tv.setPadding(8, 8, 8, 8)

        if(false) {
            val border = GradientDrawable()
            border.setColor(Color.WHITE) // Background color
            border.setStroke(2, Color.BLACK) // Border width and color
            //border.cornerRadius = 4f // Optional: corner radius

            // Apply the border to the TextView
            tv.background = border
        }

        return tv
    }


    private fun showCustomDialog(view:View, evBag:Evidence, action:String = "insert") {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_evidence_bag, null)
        dialog.setContentView(view)

        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.show()
        dialog.window!!.attributes = lp

        val evidence  = view.findViewById<EditText>(R.id.evidence)
        val distance_1  = view.findViewById<AutoCompleteTextView>(R.id.distance_1)
        val distance_2  = view.findViewById<AutoCompleteTextView>(R.id.distance_2)
        val distance_3  = view.findViewById<AutoCompleteTextView>(R.id.distance_3)
        val distance_4  = view.findViewById<AutoCompleteTextView>(R.id.distance_4)
        val angle  = view.findViewById<EditText>(R.id.angle)
        val remark  = view.findViewById<EditText>(R.id.remark)

        evidence.setText(evBag.evidence)
        distance_1.setText(evBag.distance_2)
        distance_2.setText(evBag.distance_2)
        distance_3.setText(evBag.distance_3)
        angle.setText(evBag.angle)
        remark.setText(evBag.remark)

        val bt_close  = view.findViewById<ImageButton>(R.id.bt_close)
        bt_close.setOnClickListener(View.OnClickListener { v ->
            dialog.hide()
        })

        val bt_save  = view.findViewById<Button>(R.id.bt_save)
        bt_save.setOnClickListener(View.OnClickListener { v ->


            val evBagDao:EvidenceDao = db!!.evidenceDao()
            //val evBag = EvidenceBag()
            evBag.event_report_id = uid
            evBag.evidence = evidence.text.toString()
            evBag.distance_1 = distance_1.text.toString()
            evBag.distance_2 = distance_2.text.toString()
            evBag.distance_3 = distance_3.text.toString()
            evBag.distance_4 = distance_4.text.toString()
            evBag.angle = angle.text.toString()
            evBag.remark = remark.text.toString()
            evBag.updatedat = System.currentTimeMillis()

            if(action == "insert"){
                evBag.createdat = System.currentTimeMillis()
                evBagDao.insertAll(evBag)
            }else if(action == "update"){
                evBagDao.updateAll(evBag)
            }

            val allEvidence = evidenceDao.getByIds(uid, true)
            adapter.update( allEvidence)
            //reloadTable(view,  allEvidence)

            dialog.hide()
            /*evidence.setText(evBag.evidence)
            distance_1.setText(evBag.distance_1)
            distance_2.setText(evBag.distance_2)
            distance_3.setText(evBag.distance_3)
            distance_4.setText(evBag.distance_4)
            angle.setText(evBag.angle)
            remark.setText(evBag.remark)*/
        })
    }

    fun confirmDialog(uid: Long,evidenceBag:Evidence){
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Confirm remove")
        builder.setMessage("Are you sure you want to remove?")

        builder.setPositiveButton("Yes") { dialog, which ->
            // Delete code here (e.g., delete from database)
            // Dismiss the dialog
            evidenceBag.status = false
            evidenceDao?.updateAll(evidenceBag)

            val allEvidence = evidenceDao.getByIds(uid, true)
            adapter.update(allEvidence)
            //clearTableExceptHeader()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun generateQRCode(text: String, qr_size:Int = 100): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, qr_size, qr_size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return Bitmap.createBitmap(qr_size, qr_size, Bitmap.Config.RGB_565)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertLongToDateTime(epochTime: Long?): LocalDateTime {
        //return LocalDateTime.ofInstant(epochTime?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault())
        return Instant.ofEpochMilli(epochTime!!).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun cmToPixels(context: Context, cm: Float): Int {
        val metrics = context.resources.displayMetrics
        val dpi = metrics.xdpi // You can also use metrics.ydpi if you prefer
        return (cm * (dpi / 2.54f)).toInt()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun generatePdfAndPrint(evidenceBag: MutableList<Evidence>, eventReport:List<EventReport>) {
        val pdfDocument = PdfDocument()
        val pageWdith = cmToPixels(requireContext(), 5.0f);
        val pageHeight = cmToPixels(requireContext(), 2.5f);
        val pageInfo = PdfDocument.PageInfo.Builder(pageWdith, pageHeight, 1).create()

        var evReport = eventReport.get(0)
        val create_date: LocalDateTime = convertLongToDateTime(evReport.report_known_datetime)
        val oneDate =  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(create_date)
        var oneTime = DateTimeFormatter.ofPattern("HH:mm").format(create_date)

        val oneThaiDate =  convertToThaiShortMonth(oneDate)

        val report_place = evReport.report_place
        val report_officer = evReport.report_officer

        fun formatToSixDigits(number: Int): String {
            return String.format("%06d", number)
        }


        for (ev in evidenceBag) {
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = android.graphics.Paint()
            paint.color = Color.BLACK
            paint.textSize = 25f
            //paint.strokeWidth = 2f
            var line = 35f
            val space_height = 32


            val paint5 = Paint()
            paint5.color = Color.BLACK
            paint5.textSize = 16f
            canvas.drawText("คดี ทรัพย์", 400f, 30f, paint5)
            paint5.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
            canvas.drawText("DNA", 480f, 30f, paint5)

            //qr
            var qr_size = 150
            val x_pos = 25f
            var right_pos_qr = 380f
            val originalBitmap = generateQRCode("10-CSI-67-00006-023",qr_size = qr_size)
            canvas.drawBitmap(originalBitmap, right_pos_qr, x_pos + 70, paint)

            //logo
            val logo_size = 75
            var right_pos_logo = 410f
            val originalLogo: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.forensic)
            val resizedLogo = Bitmap.createScaledBitmap(originalLogo, logo_size, logo_size, true)
            canvas.drawBitmap(resizedLogo, right_pos_logo, x_pos+10f, paint)

            /*val paintRec = Paint()
            paintRec.color = Color.BLACK // Set the color (optional)
            paintRec.style = Paint.Style.STROKE // Set paint style to stroke (outline)
            paintRec.strokeWidth = 2f
            val cornerRadius = 20f // Adjust for desired corner roundness
            val rectF = RectF(5f, 5f, pageWdith.toFloat() - 3, pageHeight.toFloat() - 3)
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paintRec)*/

            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
            canvas.drawText("วัตถุพยาน", x_pos, line, paint)
            canvas.drawText("DNA", 300f, line, paint)
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

            val paint2 = Paint()
            paint2.color = Color.BLACK // Set the color (optional)
            paint2.setStrokeWidth(2f)

            //under line
            var line_weight = 330
            val save_line_space = line
            canvas.drawLine(x_pos, line + 10, x_pos + line_weight, line + 10, paint2)
            line += space_height+20
            canvas.drawText("Evid:", x_pos, line, paint)
            line += space_height
            canvas.drawText("วัน/เวลา", x_pos, line, paint)
            line += space_height
            canvas.drawText("สถานที่", x_pos, line, paint)
            line += space_height
            canvas.drawText("ผู้เก็บ", x_pos, line, paint)
            line += space_height
            canvas.drawText("พงส.", x_pos, line, paint)
            line += space_height
            canvas.drawText("FIDS:", x_pos, line, paint)



            line = save_line_space
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
            canvas.drawLine(x_pos, line + 10, x_pos + line_weight, line + 10, paint2)
            line += space_height+20
            canvas.drawText("          6711YA006A023", x_pos, line, paint)
            line += space_height
            canvas.drawText("               "+oneThaiDate+" "+oneTime, x_pos, line, paint)
            line += space_height
            canvas.drawText("              "+report_place, x_pos, line, paint)
            line += space_height
            canvas.drawText("           "+report_officer, x_pos, line, paint)
            line += space_height
            canvas.drawText("          พ.ต.ท. สมบูรณ์ ใจกว้าง", x_pos, line, paint)
            line += space_height
            canvas.drawText("           10-CSI-67-"+formatToSixDigits(ev.uid.toInt())+"-023", x_pos, line, paint)

            val paint4 = Paint()
            paint4.color = Color.BLACK
            //paint4.setColor(Color.parseColor("#939498"))
            paint4.textSize = 18f
            paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
            canvas.drawText("Item No.", 400f, 240f, paint4)

            paint4.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
            canvas.drawText("023", 475f, 240f, paint4)


            canvas.save()

            //decoration 939498
            val paint3 = Paint()
            //paint3.color = Color.BLACK
            paint3.setColor(Color.parseColor("#939498"))
            paint3.textSize = 14f
            paint3.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))

            var x_decoration = (pageHeight-10)*-1f
            var y_decoration = right_pos_qr
            canvas.rotate(-90f);
            canvas.drawText("I-RITE         I-RITE          I-RITE         I-RITE", x_decoration, y_decoration, paint3)
            paint3.color = Color.BLACK
            canvas.drawText("              CSI                CSI               CSI", x_decoration, y_decoration, paint3)




            pdfDocument.finishPage(page)
        }

        val filePath = requireContext().getExternalFilesDir(null)?.absolutePath + "/bag.pdf"
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

    private fun printContent(file: File) {
        val printManager = requireContext().getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onStart() {
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
            .setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE)
            .build()
        val printJob = printManager.print("PrintJobName", printAdapter, attrib)
        if (printJob.isFailed) {
            Toast.makeText(requireContext(), "Printing failed", Toast.LENGTH_SHORT).show()
        }
    }
}

