package capstone.project.trasholution.ui.mainmenu.collector

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityUpdatePengepulBinding
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.logic.repository.responses.PengepulResponse
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
    var PREF_USERNAME = "username"
    var token = ""
    var username = ""
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePengepulBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!
        username = sharedPreferences.getString(PREF_USERNAME, null)!!

//        token = "Bearer $token"

        intent.getStringExtra(id)
        intent.getStringExtra(username)

        setupAutoFill()

        binding.btnDeletePengepul.setOnClickListener {
            deletePengepul(token)
        }

        binding.btnUpdatePengepul.setOnClickListener {
            updatePengepul(token)

        }

    }

    private fun setupAutoFill() {
        val afContact = intent.getStringExtra("afContact")
        val afLocation = intent.getStringExtra("afLocation")
        val afDescription = intent.getStringExtra("afDescription")

        binding.edtAddPengepulContact.setText(afContact.toString(), TextView.BufferType.EDITABLE)
        binding.edtAddPengepulLocation.setText(afLocation.toString(), TextView.BufferType.EDITABLE)
        binding.edtAddPengepulDescription.setText(afDescription.toString(), TextView.BufferType.EDITABLE)
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
                    this.mlat = location.latitude
                    this.mlon = location.longitude
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

    private fun deletePengepul(token: String) {
        val client = ApiConfig.getApiService().deletePengepul(token)
        client.enqueue(object : Callback<DataItem> {
            override fun onResponse(call: Call<DataItem>, response: Response<DataItem>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@UpdatePengepulActivity,
                        getString(R.string.collector_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun updatePengepul(token: String) {
        val contact = binding.edtAddPengepulContact.text.toString()
        val location = binding.edtAddPengepulLocation.text.toString()
        val description = binding.edtAddPengepulDescription.text.toString()

        val client = ApiConfig.getApiService()
            .updatePengepul(token, contact, location, description)
        client.enqueue(object : Callback<DataItem> {
            override fun onResponse(call: Call<DataItem>, response: Response<DataItem>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@UpdatePengepulActivity,
                        getString(R.string.update_collector_success),
                        Toast.LENGTH_SHORT
                    ).show()
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

    companion object {
        const val id = "id"
    }
}