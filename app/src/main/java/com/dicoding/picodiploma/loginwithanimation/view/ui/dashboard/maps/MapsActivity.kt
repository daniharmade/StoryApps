package com.dicoding.picodiploma.loginwithanimation.view.ui.dashboard.maps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreferences
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.factory.StoryViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val bound = LatLngBounds.Builder()

    private val viewModel by viewModels<MapsViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private val pref by lazy {
        UserPreferences.getInstance(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupMaps()
    }

    private fun setupMaps() {
        binding.menuMaps.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.normal_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                }

                R.id.satellite_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

                }

                R.id.terrain_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

                }

                R.id.hybrid_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

                }

                else -> {

                }
            }
            true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        moveToUSRCampus()
        setMapStyle()

        viewModel.loadStoriesWithLocation()
        viewModel.storiesLocation.observe(this) { storyLocation ->
            storyLocation.forEach { story ->
                val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                )
            }
        }
    }

    private fun moveToUSRCampus() {
        val USRCampus = LatLng(0.510440, 101.438309)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(USRCampus, 20f))
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}