package com.csi.irite

import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import yuku.ambilwarna.AmbilWarnaDialog
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 * Use the [DrawFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class DrawFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    protected lateinit var drawingView: DrawingView
    protected lateinit var bgBitmap: Bitmap
    private var currentColor = Color.RED
    private var currentSize = 8f
    private var defBrushSize = 3
    protected lateinit var btnSave: Button
    private var onBackPressedCallback: OnBackPressedCallback? = null

    private lateinit var drawingMenu: LinearLayout
    private lateinit var fabMenu: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_draw_map, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showSaveDialog()
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showSaveDialog()
                }
            }
        )
    }

    fun createCanvas(v:View, bgBitmap:Bitmap){
        drawingView = v.findViewById(R.id.drawingView)
        //drawingView.setEraserMode(true)
        btnSave = v.findViewById(R.id.btnSave)



        val brushSizes = listOf(5f, 10f, 15f, 20f, 35f, 50f, 78f, 80f) // Define 5 levels

        val btnErase: Button = v.findViewById(R.id.btnErase) // Erase button
        val btnDraw: Button = v.findViewById(R.id.btnDraw) // Switch back to drawing
        val btnColor: Button = v.findViewById(R.id.btnColor)
        val btnSign: Button = v.findViewById(R.id.btnSign)
        val seekBarSize: SeekBar = v.findViewById(R.id.seekBarSize)


        drawingView = v.findViewById(R.id.drawingView)
        drawingMenu = v.findViewById(R.id.menuContainer)
        fabMenu = v.findViewById(R.id.fabMenu)

        fabMenu.setOnClickListener {
            toggleMenuVisibility()
        }

        //val canvas = Canvas(bgBitmap)
        //val paint = Paint()
        //paint.color = Color.WHITE
        //canvas.drawRect(0f, 0f, screenWidth.toFloat(), screenHeight.toFloat(), paint)
        drawingView.setBackgroundImage(bgBitmap)
        drawingView.setBrushSize(brushSizes[defBrushSize])


        btnErase.setOnClickListener {
            drawingView.setEraserMode(true)
            drawingView.setBrushSize(brushSizes[defBrushSize]*4f)
        }
        btnSign.setOnClickListener {
            val dialog = SignFragment()
            dialog.show(childFragmentManager, "CustomDialog")
        }

        btnDraw.setOnClickListener {
            drawingView.setEraserMode(false)
            drawingView.setBrushColor(currentColor)
            drawingView.setBrushSize(brushSizes[defBrushSize])
        }



        btnColor.setOnClickListener {
            showColorPicker()
        }

        seekBarSize.max = brushSizes.lastIndex
        seekBarSize.progress = defBrushSize
        seekBarSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentSize = brushSizes[progress]
                drawingView.setBrushSize(currentSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val level = ((seekBar.progress.toFloat() / seekBar.max.toFloat()) * (brushSizes.lastIndex - 1)).roundToInt() + 1
                defBrushSize = level
            }
        })
    }


    fun loadBitmapKeepRatio(resourceId: Int, targetWidth: Int, context: Context): Bitmap {
        // 1. Load original bitmap from resources
        val originalBitmap = BitmapFactory.decodeResource(context.resources, resourceId)

        // 2. Calculate the aspect ratio
        val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()

        // 3. Calculate target height based on target width
        val targetHeight = (targetWidth / aspectRatio).toInt()

        // 4. Create scaled bitmap
        return Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, true)
    }

    private fun toggleMenuVisibility() {
        if (drawingMenu.visibility == View.VISIBLE) {
            drawingMenu.visibility = View.GONE
        } else {
            drawingMenu.visibility = View.VISIBLE
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun onPause() {
        super.onPause()
        // Reset to allow rotation for other fragments
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


    private fun showColorPicker() {
        val colorPicker = AmbilWarnaDialog(
            requireContext(),
            currentColor,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    currentColor = color
                    drawingView.setBrushColor(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog?) {}
            })
        colorPicker.show()
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun showSaveDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("ต้องการบันทึกรูปภาพหรือไม่?")
            .setMessage("คุณต้องการบันทึกรูปภาพหรือไม่?")
            .setPositiveButton("บันทึก") { dialog, _ ->
                saveDrawing()
                // หลังจาก Save เสร็จ
                findNavController().popBackStack() // หรือไปหน้าอื่น
            }
            .setNegativeButton("Don't Save") { dialog, _ ->
                findNavController().popBackStack()
            }
            .setNeutralButton("Cancel", null) // อยู่ต่อ
            .show()
    }

    private fun saveDrawing() {
        // TODO: บันทึกข้อมูลลง Room หรือทำอะไรก็ได้
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback?.remove()
        onBackPressedCallback = null
    }

}