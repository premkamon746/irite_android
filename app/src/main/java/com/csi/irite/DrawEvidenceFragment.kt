package com.csi.irite

import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.csi.irite.room.dao.EventReportDao

/**
 * A simple [Fragment] subclass.
 * Use the [DrawEvidenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrawEvidenceFragment : DrawFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val eventReportDao: EventReportDao = db!!.eventReportDao()

        var bgByteArray: ByteArray = eventReportDao.getImageEvidenceById(uid)

        if (bgByteArray != null) {
            bgBitmap = byteArrayToBitmap(bgByteArray)
        }else{
            bgBitmap = loadBitmapKeepRatio(R.drawable.autosy, screenWidth, requireContext())
        }

        createCanvas(view, bgBitmap)


        btnSave.setOnClickListener {
            val resultBitmap = drawingView.saveComposedImage()
            val updatedat = System.currentTimeMillis()
            eventReportDao.updateDrawEvidenceById(uid, bitmapToByteArray(resultBitmap), updatedat)
        }
    }

   override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}