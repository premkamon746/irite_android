package com.csi.irite.unuse

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.csi.irite.BaseFragment
import com.csi.irite.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.view.MapView
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


open class MapsFragment : BaseFragment() {

    companion object{
        var BANGKOK = LatLong(13.7563, 100.5018)
    }
    //private lateinit var b: ActivityMainBinding
    protected lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value == true }
        if (granted) {
            // Permission granted, get location
            getLocation()
        } else {
            // Permission denied, handle the case
            //Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AndroidGraphicFactory.createInstance(requireActivity().application)
        val view =  inflater.inflate(R.layout.fragment_maps, container, false)
        map = view.findViewById(R.id.mapView)
        //var mhp = MapHelper(requireContext(), map)
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            // Permission already granted, get location
            getLocation()
        }
        return view
    }

    protected fun getLocation() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
        /*map.mapScaleBar.isVisible = true
        map.setBuiltInZoomControls(true)
        val cache = AndroidUtil.createTileCache(
            requireContext(),
            "mycache",
            map.model.displayModel.tileSize,
            1f,
            map.model.frameBufferModel.overdrawFactor
        )

        //val inputStream = resources.openRawResource(R.raw.thailand)
        val resourceId = R.raw.thailand
        val inputStream = resources.openRawResource(resourceId)
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
                    resources, R.drawable.mark_red
                )
            )
            val draggableMarker = DraggableMarker(BANGKOK, markerBitmap, 0, 0)

            map.layerManager.layers.add(draggableMarker)

            map.setOnTouchListener { _, event ->
                draggableMarker.handleTouchEvent(event, map)
            }/**/

            map.setCenter(BANGKOK)
            map.setZoomLevel(15)
            // Use the input stream
        } else {
            Log.d("xxxxxxx","inputStream = null")
        }*/

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