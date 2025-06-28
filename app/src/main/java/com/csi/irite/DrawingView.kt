package com.csi.irite

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var backgroundBitmap: Bitmap? = null
    private var drawingBitmap: Bitmap? = null
    private var drawingCanvas: Canvas? = null
    private val currentPath = Path()
    private var isEraserMode = false

    private val drawingPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun setBackgroundImage(bitmap: Bitmap) {
        backgroundBitmap = bitmap
        if (width > 0 && height > 0) {
            backgroundBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        drawingBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawingCanvas = Canvas(drawingBitmap!!)
        backgroundBitmap?.let {
            backgroundBitmap = Bitmap.createScaledBitmap(it, w, h, true)
        }
    }

    override fun onDraw(canvas: Canvas) {
        backgroundBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        drawingBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        canvas.drawPath(currentPath, drawingPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> currentPath.moveTo(event.x, event.y)
            MotionEvent.ACTION_MOVE -> currentPath.lineTo(event.x, event.y)
            MotionEvent.ACTION_UP -> {
                currentPath.lineTo(event.x, event.y)
                drawingCanvas?.drawPath(currentPath, drawingPaint)
                currentPath.reset()
            }
        }
        invalidate()
        return true
    }

    fun setEraserMode(isEraser: Boolean) {
        isEraserMode = isEraser
        if (isEraserMode) {
            drawingPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        } else {
            drawingPaint.xfermode = null
        }
    }

    fun setBrushColor(color: Int) {
        if (!isEraserMode) {
            drawingPaint.color = color
        }
    }

    fun setBrushSize(size: Float) {
        drawingPaint.strokeWidth = size
    }

    fun clearCanvas() {
        drawingCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        invalidate()
    }

    fun saveComposedImage(file: File) {
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        backgroundBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        drawingBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        canvas.drawPath(currentPath, drawingPaint)

        try {
            FileOutputStream(file).use { out ->
                resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveComposedImage():Bitmap {
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        backgroundBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        drawingBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
        canvas.drawPath(currentPath, drawingPaint)
        return resultBitmap
    }
}