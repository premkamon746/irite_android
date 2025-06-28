package com.csi.irite

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.csi.irite.BaseFragment
import com.csi.irite.R
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.Evidence
import com.csi.irite.room.data.EvidentBag
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import org.mapsforge.map.model.com.csi.irite.room.dao.EvidentBagDao
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.google.gson.JsonParser

/**
 * A simple [Fragment] subclass.
 * Use the [TestMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EvidentBagFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_evident_bag, container, false)

        var evidenceBagDao = db!!.evidentBagDao()


        webView = view.findViewById(R.id.webView)
        webSetting(webView)

        webView.loadUrl("file:///android_asset/evident-bag.html")

        var itc = object : InterConnect {

            override fun submitForm(it:String): String {
                val gson = Gson()
                val jsonObject: JsonObject = gson.fromJson(it, JsonObject::class.java)
                val action = jsonObject.get("action").asString.trim()
                return ""
            }

            override fun getUpdateData():String {
                var eb = db?.evidentBagDao()
                val ebs = eb?.getByIds(uid)
                val gson = Gson()
                val jsonData = gson.toJson(ebs)
                Log.d("WebView----",jsonData)
                return jsonData
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getJsonData(ref: String, func: String, option:String): String {
                Log.d("debug","$ref x $func x $option")
                var jsonData = ""
                val gson = Gson()
                var eb = db?.evidentBagDao()
                val updatedat = System.currentTimeMillis()
                var status = true
                if(option == "0"){
                    status = false
                }
                if(func == "plastic"){
                    eb?.updatePlastic(ref.toLong(), status, updatedat)
                }else if(func == "paper"){
                    eb?.updatePaper(ref.toLong(), status, updatedat)
                }else if(func == "return_pks"){
                    Log.d("debug","$ref x $option x $updatedat")
                    eb?.updatePsk(ref.toLong(), option, updatedat)
                }else if(func == "get-data"){
                    var eb = db?.evidentBagDao()
                    val ebs = eb?.getByIds(uid)
                    val gson = Gson()
                    jsonData = gson.toJson(ebs)
                }else if(func == "print"){
                    val stringArray = Gson().fromJson(option, Array<String>::class.java)
                    val intArray = stringArray.map { it.toInt() }.toIntArray()

                    val selected = evidenceBagDao.loadAllByIds(intArray)
                    var eventReportDao = db!!.eventReportDao()
                    val report = eventReportDao.getActiveById(uid)
                    generatePdfAndPrint(selected,report)
                }
                return jsonData
            }

        }

        webView.addJavascriptInterface(JavaScriptInterface(activity, itc), "Android")
        return view
    }

    fun cmToPixels(context: Context, cm: Float): Int {
        val metrics = context.resources.displayMetrics
        val dpi = metrics.xdpi // You can also use metrics.ydpi if you prefer
        return (cm * (dpi / 2.54f)).toInt()
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertLongToDateTime(epochTime: Long?): LocalDateTime {
        //return LocalDateTime.ofInstant(epochTime?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault())
        return Instant.ofEpochMilli(epochTime!!).atZone(ZoneId.systemDefault()).toLocalDateTime()
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
    private fun generatePdfAndPrint(evidenceBag: List<EvidentBag>, eventReport:List<EventReport>) {
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

            val output = try {
                val jsonElement = JsonParser.parseString(ev.return_pks)
                if (jsonElement.isJsonArray) {
                    val arr = Gson().fromJson(ev.return_pks, Array<String>::class.java)
                    val joined = arr.joinToString(",")
                    Log.d("DEBUG", "return_pks is JSON array: $joined")
                    joined
                } else {
                    Log.d("DEBUG", "return_pks is plain string (not JSON array): $ev.return_pks")
                    ev.return_pks
                }
            } catch (e: Exception) {
                Log.d("DEBUG", "return_pks is plain string (JSON parse failed): $ev.return_pks")
                ev.return_pks
            }

            val paint5 = Paint()
            paint5.color = Color.BLACK
            paint5.textSize = 16f
            paint5.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // set before measuring
            Log.d("",ev.return_pks)
            val return_pks = Gson().fromJson(ev.return_pks, Array<String>::class.java).joinToString(",")

            val rightMargin = 360f
            val textWidth = paint5.measureText(return_pks)
            val x = rightMargin - textWidth // align right

            canvas.drawText(return_pks, x, 33f, paint5)

            //qr
            var qr_size = 150
            val x_pos = 25f
            var right_pos_qr = 380f


            var rno = evReport.report_number.toString()
            var qrno = "1"
            try {
                qrno = rno.substring(
                    6,
                    8
                ) + "CSI-" + rno.substring(rno.length - 2) + formatToSixDigits(ev.uid.toInt()) + "-" + rno.substring(
                    1,
                    3
                )
            }catch (e: Exception) {}
            val originalBitmap = generateQRCode(qrno,qr_size = qr_size)
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
            //canvas.drawText(ev.return_pks, 300f, line, paint)
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
            canvas.drawText("               "+rno, x_pos, line, paint)
            line += space_height
            canvas.drawText("               "+oneThaiDate+" "+oneTime, x_pos, line, paint)
            line += space_height
            canvas.drawText("              "+report_place, x_pos, line, paint)
            line += space_height
            canvas.drawText("           "+report_officer, x_pos, line, paint)
            line += space_height
            canvas.drawText("          "+report_officer, x_pos, line, paint)
            line += space_height
            canvas.drawText("           "+rno, x_pos, line, paint)

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