package com.csi.irite.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.annotation.RequiresApi
import com.csi.irite.R
import com.csi.irite.room.data.EventReport
import com.csi.irite.room.data.Evidence
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Rectangle
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class PrintFormLabelService(private val context: Context) {

    private lateinit var titleFont: Font
    private lateinit var bodyFont: Font
    private lateinit var headerTableFont: Font
    private lateinit var doc: Document
    private lateinit var fileName: String
    private lateinit var pdfFile: File
    private lateinit var warningFont: Font
    private val frontSize:Float = 20f





    fun init(){

        val width = (4 * 72).toFloat() // 4 inches
        val height = (6 * 72).toFloat() // 6 inches
        val pageSize = Rectangle(width, height)

        doc = Document(pageSize)

        fileName = "app_label_print.pdf"
        pdfFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        PdfWriter.getInstance(doc, FileOutputStream(pdfFile))
        doc.open()

        val assetManager = context.assets
        val inputStream = assetManager.open("fonts/THSarabunNew.ttf")

        val fontFile = File.createTempFile("sarabun", ".ttf", context.cacheDir)
        inputStream.use { input ->
            fontFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val baseFont = BaseFont.createFont(
            fontFile.absolutePath,
            BaseFont.IDENTITY_H,
            BaseFont.EMBEDDED
        )
        titleFont = Font(baseFont, frontSize, Font.BOLD, BaseColor.BLACK)
        headerTableFont = Font(baseFont, frontSize, Font.BOLD, BaseColor.BLACK)
        bodyFont = Font(baseFont, frontSize, Font.NORMAL, BaseColor.BLACK)
        warningFont = Font(baseFont, frontSize, Font.BOLD, BaseColor.RED)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generatePdfAndPrint(ev: EventReport, allEvidences:List<Evidence>, context: Context) {

        try {
            init()


            // Title
            //doc.add(Paragraph(" ")) // space
            var rd = convertDateToThaiArray(ev.report_known_date.toString().trim())
            var kd = convertDateToThaiArray(ev.report_known_date.toString().trim())
            createLogo()
            right("เลขรับที่/เลขรายงาน "+ev.report_number.toString().trim())
            head("แบบการตรวจเก็บและส่งมอบวัตถุพยาน")
            right("เขียนที่ "+ev.report_write.toString().trim())
            right("วันที่ "+rd[0]+" เดือน "+rd[1]+" พ.ศ. "+rd[2])
            left("รับแจ้งเหตุ จาก ท้องที่ สน/สภ. "+ev.report_place.toString().trim())
            left("ที่ "+ev.report_place.toString().trim()+" ลง "+ev.report_from_where.toString().trim()+" เมื่อวันที่ "+kd[0]+" เดือน "+kd[1]+" พ.ศ. "+kd[2])

            left("คดี "+ev.report_type.toString().trim()+" สถานที่เกิดเหตุ "+ev.report_place.toString().trim())
            left("วัน เวลา ทราบเหตุ/เกิดเหตุ เวลาประมาณ ")
            left("วัน เวลา ตรวจสถานที่เกิดเหตุ/ตรวจเก็บวัตถุพยาน "+""+" เวลาประมาณ "+"")
            left("ผู้เสียหาย/ผู้เสียชีวิต อายุประมาณ "+ev.report_place.toString().trim())
            left("ชื่อพนักงานสอบสวนเจ้าของคดี "+ev.report_officer.toString().trim())

            left("กสก                          /พฐ.จว.")
            left("จำนวน  "+allEvidences.count()+" รายการดังนี้")

            // Table with 3 col umns
            if (allEvidences.count() > 0) {
                val table = PdfPTable(3)
                table.headerRows = 1
                table.widthPercentage = 100f
                table.setWidths(floatArrayOf(1f, 3f, 2f))

                val headers = listOf("ลำดับ", "รายการ", "การตรวจพิสูจน์")
                headers.forEach {
                    val cell = PdfPCell(Phrase(it, headerTableFont))
                    cell.horizontalAlignment = Element.ALIGN_CENTER
                    cell.backgroundColor = BaseColor.WHITE
                    table.addCell(cell)
                }

                // Rows
                var i = 1
                for (evd in allEvidences) {
                    table.addCell(Phrase(i.toString(), bodyFont))
                    i = i + 1

                    // wrap text ตัวอย่าง
                    val detail = evd.evidence.toString()
                    table.addCell(Phrase(detail, bodyFont))

                    table.addCell(Phrase("", bodyFont))
                }

                doc.add(table)
            }else{
                val title = Paragraph("ยังไม่มีการบันทึกการเก็บหลักฐาน", warningFont)
                title.alignment = Element.ALIGN_CENTER
                doc.add(title)
            }

            /*val container = Paragraph()
            container.alignment = Element.ALIGN_RIGHT
            container.spacingAfter = 10f
            container.keepTogether = true // <<<< บังคับให้อยู่หน้าเดียว!*/

            right("(ลงชื่อ).........................................................ผู้รับมอบ")
            right("    (........................................................)        ")
            right("ตำแหน่ง..........................................................        ")
            right("วันที่.................................เวลา........................น.")
            right("(ลงชื่อ).........................................................ผู้มอบ")
            right("    (........................................................)        ")

            //doc.add(container)


            doc.close()

            showPrintPreview(context, pdfFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun head(s :String){
        val title = Paragraph(s, titleFont)
        title.alignment = Element.ALIGN_CENTER
        doc.add(title)
    }

    fun right(s :String){
        val title = Paragraph(s, bodyFont)
        title.alignment = Element.ALIGN_RIGHT
        doc.add(title)
    }

    fun left(s :String){
        val title = Paragraph(s, bodyFont)
        title.alignment = Element.ALIGN_LEFT
        doc.add(title)
    }

    fun right(s :String,p: Paragraph){
        val title = Paragraph(s, bodyFont)
        title.alignment = Element.ALIGN_RIGHT
        p.add(title)
    }

    fun createLogo() {
        try {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.forensic_sci)  // ชื่อไฟล์โลโก้ เช่น logo.png
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
             val imageBytes = stream.toByteArray()
             val image = Image.getInstance(imageBytes)

             // ปรับขนาดรูป (แล้วแต่ต้องการ)
             image.scaleToFit(100f, 100f) // กำหนดขนาดกว้างxสูง
             image.alignment = Element.ALIGN_CENTER // กำหนดให้อยู่ตรงกลาง

             doc.add(image)
        }catch (E:Exception){
            E.printStackTrace()
        }

    }

    fun showPrintPreview(context: Context, pdfFile: File) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = PdfDocumentAdapter(context, pdfFile)
        val jobName = "Document - ${pdfFile.name}"
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }

    protected fun convertDateToThaiArray(dateStr: String): Array<String> {
        val monthNames = arrayOf(
            "", "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
        )

        val parts = dateStr.split("/")
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid date format, must be dd/MM/yyyy")
        }

        val day = parts[0].toInt().toString() // remove leading zero
        val month = parts[1].toInt()           // 1-12
        val year = parts[2].toInt() + 543       // convert to Buddhist year

        return arrayOf(day, monthNames[month], year.toString())
    }
}