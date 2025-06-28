package com.csi.irite.utils

import android.content.Context
import android.os.Bundle
import android.print.PrintDocumentAdapter
import android.print.PrintAttributes
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintDocumentAdapter.WriteResultCallback
import android.print.PrintDocumentAdapter.LayoutResultCallback
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PdfDocumentAdapter(private val context: Context, private val file: File) : PrintDocumentAdapter() {

    private var fileDescriptor: ParcelFileDescriptor? = null

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        layoutResultCallback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        if (cancellationSignal?.isCanceled == true) {
            layoutResultCallback?.onLayoutCancelled()
            return
        }

        val builder = PrintDocumentInfo.Builder(file.name)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)

        layoutResultCallback?.onLayoutFinished(builder.build(), true)
    }

    override fun onWrite(
        pages: Array<PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {
        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

            val inputStream = FileInputStream(fileDescriptor!!.fileDescriptor)
            val outputStream = FileOutputStream(destination.fileDescriptor)

            inputStream.copyTo(outputStream)

            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))

        } catch (e: Exception) {
            callback.onWriteFailed(e.message)
        }
    }
}
