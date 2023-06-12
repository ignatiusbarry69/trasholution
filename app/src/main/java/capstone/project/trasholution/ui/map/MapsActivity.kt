package capstone.project.trasholution.ui.map

import capstone.project.trasholution.logic.repository.Result
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityMapsBinding
import capstone.project.trasholution.ui.ViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import createToast

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
//    private lateinit var userModel: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapsActivity)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupFeatureMap()
        getMyLocation()
        setupAction()
    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(applicationContext)
        mapsViewModel = ViewModelProvider(this@MapsActivity, factory)[MapsViewModel::class.java]

//        mapsViewModel.getUser().observe(this) {
//            this.userModel = it
//        }
    }

    private fun setupAction() {
        mapsViewModel.getLocation().observe(this@MapsActivity) {result ->
            if(result != null){
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        for (pengepul in result.data) {
                            if (pengepul.lat != null && pengepul.lon != null){
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(pengepul.lat!!, pengepul.lon!!))
                                        .title(pengepul.username)
                                        .snippet(pengepul.description)
                                )
                                //here i want to move the camera here
//                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 15f))
                            }
                        }
                    }
                    is Result.Error -> {
                        createToast(this@MapsActivity, "Connection Error")
                    }
                }
            }
        }
    }

    private fun setupFeatureMap() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }


    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mMap.setOnMyLocationChangeListener { location ->
                val myLocation = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13f))
                mMap.setOnMyLocationChangeListener(null)
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}