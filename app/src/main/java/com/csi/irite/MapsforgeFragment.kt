package com.csi.irite


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.cache.TileCache
import org.mapsforge.map.layer.overlay.Marker
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import org.mapsforge.core.graphics.Bitmap as MapsForgeBitmap
import android.widget.Toast
import com.csi.irite.room.dao.EventReportDao


class MapsforgeFragment : BaseFragment() {

    private lateinit var mapView: MapView
    private var currentMarker: Marker? = null
    private var startX: Float = 0f
    private var startY: Float = 0f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1001

    private lateinit var etLat: EditText
    private lateinit var etLng: EditText
    private lateinit var btnSet: Button
    private lateinit var btnSave: Button
    private lateinit var currentLocation: Button

    protected val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value == true }
        if (granted) {
            // Permission granted, get location
            Log.d("cccccccccccccccccccc","granted location:")
            getDeviceLocation()
        } else {
            Log.d("cccccccccccccccccccc","no granted location:")
            // Permission denied, handle the case
            //Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view =  inflater.inflate(R.layout.fragment_maps, container, false)
        mapView = view.findViewById<MapView>(R.id.mapView)
        etLat = view.findViewById(R.id.etLatitude)
        etLng = view.findViewById(R.id.etLongitude)
        btnSet = view.findViewById(R.id.btnSetMarker)
        btnSave = view.findViewById(R.id.btnSaveLocation)
        currentLocation = view.findViewById(R.id.currentLocation)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        AndroidGraphicFactory.createInstance(requireActivity().application)
        //var mhp = MapHelper(requireContext(), map)
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            // Permission already granted, get location
            getDeviceLocation()
        }

        //mapView = MapView(requireContext())
        mapView.setClickable(true)
        mapView.setBuiltInZoomControls(true)
        mapView.getMapScaleBar().setVisible(true)

        mapView.model.mapViewPosition.setZoomLevel((16).toByte())


        val tileCache: TileCache = AndroidUtil.createTileCache(
            requireContext(),
            "mapcache",
            mapView.model.displayModel.tileSize,
            1f,
            mapView.model.frameBufferModel.overdrawFactor
        )

        //val mapFile = File(requireContext().getExternalFilesDir(null), "yourmap.map")



        val assetManager: AssetManager = requireContext().assets
        val inputStream = assetManager.open("thailand.map")
        val filePath = getFilePathForResource(inputStream)
        inputStream.close()

        //if (tempMapFile.exists()) {
            val tileRendererLayer = TileRendererLayer(
                tileCache,
                MapFile(filePath),
                mapView.getModel().mapViewPosition,
                AndroidGraphicFactory.INSTANCE
            )
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT)
            mapView.layerManager.layers.add(tileRendererLayer)
        //}

        // เริ่มหาตำแหน่ง GPS ปัจจุบัน
        getDeviceLocation()

        var isMarkerBeingDragged = false
        // listener กดในแผนที่ -> ย้าย Marker
        mapView.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    // Detect if the touch started on an existing marker
                    startX = event.x
                    startY = event.y

                    val geoPoint = mapView.mapViewProjection.fromPixels(startX.toDouble(), startY.toDouble())
                    currentMarker?.let {
                        if (isMarkerTouchingMarker(geoPoint, it)) {
                            isMarkerBeingDragged = true
                        }
                    }
                }
                android.view.MotionEvent.ACTION_MOVE -> {
                    if (isMarkerBeingDragged) {
                        val geoPoint = mapView.mapViewProjection.fromPixels(event.x.toDouble(), event.y.toDouble())
                        addMarkerAt(geoPoint)
                    }
                }
                android.view.MotionEvent.ACTION_UP -> {
                    isMarkerBeingDragged = false
                }
            }
            false
        }

        currentLocation.setOnClickListener {
            getDeviceLocation()
        }

        btnSet.setOnClickListener {
            try {
                val lat = etLat.text.toString().toDouble()
                val lng = etLng.text.toString().toDouble()
                val pos = LatLong(lat, lng)
                addMarkerAt(pos)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Invalid coordinates", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            currentMarker?.latLong?.let { pos ->

                val eventReportDao: EventReportDao = db!!.eventReportDao()
                var updatedat = System.currentTimeMillis()
                eventReportDao.updateLocation(pos.latitude, pos.longitude, updatedat,uid)

                Toast.makeText(requireContext(), "Saved: ${pos.latitude}, ${pos.longitude}", Toast.LENGTH_SHORT).show()
                // TODO: Save to DB or file
            }
        }

        return view
    }

    private fun isMarkerTouchingMarker(geoPoint: LatLong, marker: Marker): Boolean {
        // Compare the touch point with the marker's position (this could be adjusted for proximity)
        val distance = Math.sqrt(
            Math.pow(geoPoint.latitude - marker.latLong.latitude, 2.0) +
                    Math.pow(geoPoint.longitude - marker.latLong.longitude, 2.0)
        )
        return distance < 0.0001 // Adjust threshold based on your use case
    }

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


    private fun getDeviceLocation() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geoPoint = LatLong(location.latitude, location.longitude)
                addMarkerAt(geoPoint)
                etLat.setText(location.latitude.toString())
                etLng.setText(location.longitude.toString())
            }else{
                var thailandLatLong = LatLong(13.7563, 100.5018)
                addMarkerAt(thailandLatLong)
            }
        }
    }

    private fun moveMarker(newLatLong: LatLong) {
        currentMarker?.let {
            it.latLong = newLatLong
            mapView.invalidate()
            // Log ตำแหน่งใหม่
            println("New Location: ${newLatLong.latitude}, ${newLatLong.longitude}")
            // TODO: เตรียมบันทึก newLatLong ไป Room ได้เลย
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.destroyAll()
    }

    private fun setDefaultLocationMarker() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val latLong = LatLong(location.latitude, location.longitude)
            addMarkerAt(latLong)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun addMarkerAt(latLong:LatLong){

        val eventReportDao: EventReportDao = db!!.eventReportDao()
        val eventReport = eventReportDao.loadAllByIds(uid)

        Log.d("current_locaiton_lat", eventReport.lat.toString())
        Log.d("current_locaiton_lat", eventReport.lng.toString())

        if (latLong != null && currentMarker == null) {
            val drawable = resources.getDrawable(
                android.R.drawable.ic_menu_mylocation,
                requireContext().theme
            ).mutate()

            drawable.setTint(Color.RED)
            val bitmap = AndroidGraphicFactory.convertToBitmap(drawable)

            currentMarker = Marker(latLong, bitmap, 0, -50)
            mapView.layerManager.layers.add(currentMarker)
            mapView.setCenter(latLong)
            mapView.model.mapViewPosition.zoomLevel = 15.toByte()
        }else{
            mapView.setCenter(latLong)
            moveMarker(latLong)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setDefaultLocationMarker()
        }
    }
}