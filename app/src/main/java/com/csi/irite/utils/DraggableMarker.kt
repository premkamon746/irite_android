package org.mapsforge.map.model.com.csi.irite.utils

import android.util.Log
import org.mapsforge.core.graphics.Bitmap
import android.view.MotionEvent
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.model.Point
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.overlay.Marker

class DraggableMarker(latLong: LatLong, bitmap: Bitmap, horizontalOffset: Int, verticalOffset: Int) :
    Marker(latLong, bitmap, horizontalOffset, verticalOffset) {

    private var isDragging = false
    private var previousX = 0f
    private var previousY = 0f

    /*init {
        // Enable dragging when the marker is long-pressed
        setLongPress(true)
    }

    override fun onLongPress(event: MotionEvent, mapView: MapView): Boolean {
        // Check if the long press event is within the bounds of the marker
        if (contains(Point(event.x.toDouble(), event.y.toDouble()), Point(mapView.mapViewProjection.toPixels(latLong).x, mapView.mapViewProjection.toPixels(latLong).y))) {
            isDragging = true
            previousX = event.x
            previousY = event.y
            return true
        }
        return super.onLongPress(event, mapView)
    }*/

    fun handleTouchEvent(event: MotionEvent, mapView: MapView): Boolean {
        if (isDragging) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    // Calculate the change in position
                    val deltaX = event.x - previousX
                    val deltaY = event.y - previousY

                    // Update the marker's position
                    latLong = mapView.mapViewProjection.fromPixels(
                        mapView.mapViewProjection.toPixels(latLong).x + deltaX,
                        mapView.mapViewProjection.toPixels(latLong).y + deltaY
                    )

                    // Invalidate the map view to redraw the marker at the new position
                    mapView.invalidate()

                    // Update previous touch coordinates
                    previousX = event.x
                    previousY = event.y

                    return true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isDragging = false
                    return true
                }
            }
        }
        return false
    }
}