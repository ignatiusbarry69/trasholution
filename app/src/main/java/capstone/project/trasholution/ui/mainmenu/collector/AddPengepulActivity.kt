package capstone.project.trasholution.ui.mainmenu.collector

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityAddPengepulBinding
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.logic.repository.retrofit.ApiConfig
import capstone.project.trasholution.ui.mainmenu.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPengepulActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPengepulBinding
    lateinit var sharedPreferences: SharedPreferences

    var PREF_LOGIN = "login"
    var PREF_TOKEN = "token"
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPengepulBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!

        binding.btnAddPengepulSubmit.setOnClickListener {
            addPengepul(token)
        }

    }

    private fun addPengepul(token: String) {
        val contact = binding.edtAddPengepulContact.text.toString()
        val location = binding.edtAddPengepulLocation.text.toString()
        val description = binding.edtAddPengepulDescription.text.toString()

        val client =
            ApiConfig.getApiService(token).addPengepul(contact, location, description)
        client.enqueue(object : Callback<DataItem> {
            override fun onResponse(call: Call<DataItem>, response: Response<DataItem>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddPengepulActivity, "Berhasil Tambah Pengepul", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddPengepulActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<DataItem>, t: Throwable) {
                Toast.makeText(this@AddPengepulActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}