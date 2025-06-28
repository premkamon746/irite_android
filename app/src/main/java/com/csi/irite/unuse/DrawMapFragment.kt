package com.csi.irite.unuse

import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.csi.irite.DrawFragment
import com.csi.irite.room.dao.EventReportDao

/**
 * A simple [Fragment] subclass.
 * Use the [DrawMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrawMapFragment : DrawFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val eventReportDao: EventReportDao = db!!.eventReportDao()

        var bgByteArray: ByteArray = eventReportDao.getImageMapById(uid)

        if (bgByteArray != null) {
            bgBitmap = byteArrayToBitmap(bgByteArray)
        }else{
            bgBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        }

        createCanvas(view, bgBitmap)


        btnSave.setOnClickListener {
            val resultBitmap = drawingView.saveComposedImage()
            var updatedat = System.currentTimeMillis()
            eventReportDao.updateDrawMapById(uid, bitmapToByteArray(resultBitmap), updatedat)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}