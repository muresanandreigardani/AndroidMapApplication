package com.example.mapapplcation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var poligonOptions: PolygonOptions = PolygonOptions()
    private var polylineOptions: PolylineOptions = PolylineOptions()
    private lateinit var mapsActivity: MapsActivity
    private var optionToPaint: String = "Point"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("display_zoom_buttons") { _, bundle ->
            this.mMap.uiSettings.isZoomControlsEnabled = bundle.getBoolean("checkValue")
        }
        setFragmentResultListener("itemToPaint") { _, bundle ->
            this.optionToPaint = bundle.getString("itemToPaintOption").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapsActivity = activity as MapsActivity
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateLocationUI()
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnMapClickListener { currentPoint ->

            when (this.optionToPaint) {
                "Point" -> {
                    drawPoint(currentPoint)
                }
                "Line" -> {
                    drawLine(currentPoint)
                }
                "Polygon" -> {
                    drawPolygon(currentPoint)
                }
            }
        }
    }

    private fun drawPoint(currentPoint: LatLng) {
        mMap.apply {
            addMarker(
                MarkerOptions()
                    .position(currentPoint)
            )
        }
    }

    private fun drawLine(currentPoint: LatLng) {
        drawPoint(currentPoint)
        polylineOptions.add(currentPoint)
        if (polylineOptions.points.size == 4) {
            mMap.addPolyline(polylineOptions)
            polylineOptions = PolylineOptions()
        }
    }

    private fun drawPolygon(currentPoint: LatLng) {
        drawPoint(currentPoint)
        poligonOptions.add(currentPoint)
        if (poligonOptions.points.size == 4) {
            mMap.addPolygon(poligonOptions)
            poligonOptions = PolygonOptions()
        }
    }

    private fun updateLocationUI() {
        mapsActivity.getLocationPermission()
        if (mapsActivity.locationPermissionGranted) {
            val locationResult = mapsActivity.getFusedLocation().lastLocation
            locationResult.addOnCompleteListener(mapsActivity) { task ->
                if (task.isSuccessful) {
                    val lastLocation = task.result
                    if (lastLocation != null) {
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastLocation.latitude,
                                    lastLocation.longitude
                                ), DEFAULT_ZOOM.toFloat()
                            )
                        )
                    }
                }

            }
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 15
    }

}
