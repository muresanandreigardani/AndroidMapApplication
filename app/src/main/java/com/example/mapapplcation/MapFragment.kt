package com.example.mapapplcation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var poligonOptions: PolygonOptions = PolygonOptions()
    private lateinit var mapsActivity: MapsActivity

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
            mMap.apply {
                addMarker(
                    MarkerOptions()
                        .position(currentPoint)
                )
            }
            poligonOptions.add(currentPoint)
            if (poligonOptions.points.size == 4) {
                mMap.addPolygon(poligonOptions)
                poligonOptions = PolygonOptions()
            }
        }
    }

    private fun updateLocationUI() {
        mapsActivity.getLocationPermission()
        if (mapsActivity.locationPermissionGranted) {
            val locationResult = mapsActivity.getFusedLocation().lastLocation
            locationResult.addOnCompleteListener(mapsActivity) { task ->
                if (task.isSuccessful) {
                    val lastLocation = task.result
                    mMap.apply {
                        addMarker(
                            MarkerOptions()
                                .position(LatLng(lastLocation.latitude, lastLocation.longitude))
                                .title("My Location")
                        )
                    }
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
