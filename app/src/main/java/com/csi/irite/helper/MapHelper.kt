package org.mapsforge.map.model.com.csi.irite.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.view.View
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.csi.irite.unuse.MapsFragment
import com.csi.irite.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.mapsforge.map.android.view.MapView
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidBitmap
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.model.com.csi.irite.utils.DraggableMarker
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MapHelper(var context: Context,var map: MapView) {

    companion object{
        var BANGKOK = LatLong(13.7563, 100.5018)
    }

    public fun getLocation(view: View) {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                BANGKOK = LatLong(latitude, longitude)
                val latEdit = view.findViewById<EditText>(R.id.latitude)
                val longEdit = view.findViewById<EditText>(R.id.longitude)
                latEdit.setText(latitude.toString())
                longEdit.setText(longitude.toString())
                openMap()
                // Use latitude and longitude for your purposes
                //Toast.makeText(this, "Location: ($latitude, $longitude)", Toast.LENGTH_SHORT).show()
            } else {
                //Toast.makeText(this, "Location unavailable", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            // Handle location retrieval failure
            //Toast.makeText(this, "Error getting location: $e", Toast.LENGTH_SHORT).show()
        }/**/
    }

    fun openMap(){
        map.mapScaleBar.isVisible = true
        map.setBuiltInZoomControls(true)
        val cache = AndroidUtil.createTileCache(
            context,
            "mycache",
            map.model.displayModel.tileSize,
            1f,
            map.model.frameBufferModel.overdrawFactor
        )

        //val inputStream = resources.openRawResource(R.raw.thailand)
        /*val resourceId = R.raw.thailand
        val inputStream = context.resources.openRawResource(resourceId)
        inputStream.toString()
        if (inputStream != null) {
            val filePath = getFilePathForResource(inputStream)
            inputStream.close()
            val mapStore = MapFile(filePath)

            val renderLayer = TileRendererLayer(
                cache,
                mapStore,
                map.model.mapViewPosition,
                AndroidGraphicFactory.INSTANCE
            )

            renderLayer.setXmlRenderTheme(
                InternalRenderTheme.DEFAULT
            )

            map.layerManager.layers.add(renderLayer)

            val markerBitmap: Bitmap = AndroidBitmap(
                BitmapFactory.decodeResource(
                    context.resources, R.drawable.mark_red
                )
            )
            val draggableMarker = DraggableMarker(BANGKOK, markerBitmap, 0, 0)

            map.layerManager.layers.add(draggableMarker)

            map.setOnTouchListener { _, event ->
                draggableMarker.handleTouchEvent(event, map)
            }/**/

            map.setCenter(MapsFragment.BANGKOK)
            map.setZoomLevel(15)
            // Use the input stream
        } else {
            Log.d("xxxxxxx","inputStream = null")
        }*/

    }

    fun updateMarker(local:LatLong){

        val markerBitmap: Bitmap = AndroidBitmap(
            BitmapFactory.decodeResource(
                context.resources, R.drawable.mark_red
            )
        )

        val draggableMarker = DraggableMarker(local, markerBitmap, 0, 0)

        map.layerManager.layers.add(draggableMarker)

        map.setOnTouchListener { _, event ->
            draggableMarker.handleTouchEvent(event, map)
        }/**/

        map.setCenter(MapsFragment.BANGKOK)
        map.setZoomLevel(15)
    }

    /*
     * Settings related methods.
     */
    /*protected fun createSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.edit().clear()
        PreferenceManager.setDefaultValues(requireContext(), R.xml.preferences, true)
        //sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }*/

    fun getFilePathForResource(inputStream: InputStream): String {
        val tempFile = File.createTempFile("temp", null)
        tempFile.deleteOnExit()

        FileOutputStream(tempFile).use { output ->
            inputStream.use { input ->
                input.copyTo(output)
            }
        }

        return tempFile.absolutePath
    }
}