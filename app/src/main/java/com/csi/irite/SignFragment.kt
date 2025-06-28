package com.csi.irite

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment

class SignFragment : DialogFragment() {

    private lateinit var drawingView: DrawingView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_sign, null)

        drawingView = view.findViewById(R.id.drawingView)
        val bgBitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
        bgBitmap.eraseColor(Color.WHITE)
        drawingView.setBackgroundImage(bgBitmap)

        val btnClear: Button = view.findViewById(R.id.btnClear)
        btnClear.setOnClickListener {
            drawingView.clearCanvas()
        }

        val btnClose = view.findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
}