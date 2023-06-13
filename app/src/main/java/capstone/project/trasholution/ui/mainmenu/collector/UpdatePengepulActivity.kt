package capstone.project.trasholution.ui.mainmenu.collector

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityUpdatePengepulBinding
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.logic.repository.retrofit.ApiConfig
import capstone.project.trasholution.ui.mainmenu.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import createToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePengepulActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePengepulBinding
    lateinit var sharedPreferences: SharedPreferences
    var PREF_LOGIN = "login"
    var PREF_TOKEN = "token"
    var token = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mlat: Double? = null
    private var mlon: Double? = null


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mlat = location.latitude
                    mlon = location.longitude
                } else {
                    createToast(this@UpdatePengepulActivity, getString(R.string.fail_getlocation))
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePengepulBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!
//        token = "Bearer $token"

        intent.getStringExtra(id)

        binding.btnAddPengepulSubmit.setOnClickListener {
            deletePengepul(id)
        }

        binding.btnAddPengepulSubmit2.setOnClickListener {
            updatePengepul(token, id)
        }

        binding.giveLocation.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                getMyLastLocation()
            } else {
                mlat = null
                mlon = null
            }
        }
    }

    private fun deletePengepul(id: String) {
        val client = ApiConfig.getApiService(token).deletePengepul(id)
        client.enqueue(object : Callback<DataItem> {
            override fun onResponse(call: Call<DataItem>, response: Response<DataItem>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdatePengepulActivity, "Berhasil Delete Pengepul", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdatePengepulActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<DataItem>, t: Throwable) {
                Toast.makeText(this@UpdatePengepulActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun updatePengepul(token: String, id: String) {
        val contact = binding.edtAddPengepulContact.text.toString()
        val location = binding.edtAddPengepulLocation.text.toString()
        val description = binding.edtAddPengepulDescription.text.toString()
        val position = binding.giveLocation.isChecked
        var lat : Double? = null
        var lon : Double? = null
        if (position) {
            lat = mlat
            lon = mlon
        }

        val client = ApiConfig.getApiService().updatePengepul(token, id, contact, location, description, lat, lon)
        client.enqueue(object : Callback<DataItem> {
            override fun onResponse(call: Call<DataItem>, response: Response<DataItem>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdatePengepulActivity, "Berhasil Update Pengepul", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdatePengepulActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {

                }
            }

            override fun onFailure(call: Call<DataItem>, t: Throwable) {
                Toast.makeText(this@UpdatePengepulActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

//    companion object {
//        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION)
//        private const val REQUEST_CODE_PERMISSIONS = 10
//    }

    companion object{
        const val id = "id"
    }
}