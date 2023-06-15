package capstone.project.trasholution.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityProfileBinding
import capstone.project.trasholution.logic.repository.Result
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.logic.repository.responses.PengepulResponse
import capstone.project.trasholution.logic.repository.retrofit.ApiConfig
import capstone.project.trasholution.ui.ViewModelFactory
import capstone.project.trasholution.ui.about.AboutActivity
import capstone.project.trasholution.ui.login.LoginActivity
import capstone.project.trasholution.ui.mainmenu.article.DetailArticleActivity
import capstone.project.trasholution.ui.mainmenu.collector.AddPengepulActivity
import capstone.project.trasholution.ui.mainmenu.collector.UpdatePengepulActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : AppCompatActivity() {

    private var binding: ActivityProfileBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var profileViewModel: ProfileViewModel
    private var id: String? = null
    private var isCollector: Boolean = false

    var PREF_LOGIN = "login"
    var PREF_TOKEN = "token"
    var PREF_EMAIL = "email"
    var PREF_USERNAME = "username"

    var email = ""
    var token = ""
    var username = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!
        email = sharedPreferences.getString(PREF_EMAIL, null)!!
        username = sharedPreferences.getString(PREF_USERNAME, null)!!

        setupViewModel()
        getData()

        binding?.tvProfileName?.text = username

        if (isCollector == true) {
            binding?.tvPengepul?.text = getString(R.string.update_collector)
            binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_update_24dp)
        } else if (isCollector == false) {
            binding?.tvPengepul?.text = getString(R.string.join_collector)
            binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_add_circle_no_fill_24dp)
        }

        binding?.btnAddPengepul?.setOnClickListener {
            if (isCollector) {
                val setup = ApiConfig.getApiService().getMyData(token, username)
                setup.enqueue(object : Callback<PengepulResponse> {
                    override fun onResponse(
                        call: Call<PengepulResponse>,
                        response: Response<PengepulResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                val intent = Intent(this@ProfileActivity, UpdatePengepulActivity::class.java)
                                intent.putExtra(UpdatePengepulActivity.id, id)
                                intent.putExtra("afContact", responseBody.data[0].contact)
                                intent.putExtra("afLocation", responseBody.data[0].location)
                                intent.putExtra("afDescription", responseBody.data[0].description)
                                startActivity(intent)
                            }

                        }
                    }

                    override fun onFailure(call: Call<PengepulResponse>, t: Throwable) {
                        Toast.makeText(this@ProfileActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                val intent = Intent(this, AddPengepulActivity::class.java)
                startActivity(intent)
            }
        }

        binding?.cardviewAbout?.setOnClickListener {
            val intent = Intent(this@ProfileActivity, AboutActivity::class.java)
            startActivity(intent)
        }


        binding?.logoutButton?.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(this@ProfileActivity, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        binding?.cardviewLanguage?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }


    }

    private fun getData() {
        profileViewModel.getMyData(token, username).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    id = result.data
                    isCollector = true
                    binding?.tvPengepul?.text = getString(R.string.update_collector)
                    binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_update_24dp)

                }
                is Result.Error -> {
                    id = ""
                    isCollector = false
                    binding?.tvPengepul?.text = getString(R.string.join_collector)
                    binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_add_circle_no_fill_24dp)
                }
            }
        }

    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(applicationContext)
        profileViewModel =
            ViewModelProvider(this@ProfileActivity, factory)[ProfileViewModel::class.java]
    }
}