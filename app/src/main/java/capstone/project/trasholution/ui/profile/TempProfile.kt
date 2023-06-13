package capstone.project.trasholution.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityProfileBinding
import capstone.project.trasholution.logic.repository.Result
import capstone.project.trasholution.ui.ViewModelFactory
import capstone.project.trasholution.ui.login.LoginActivity
import capstone.project.trasholution.ui.mainmenu.collector.AddPengepulActivity
import capstone.project.trasholution.ui.mainmenu.collector.UpdatePengepulActivity

class TempProfile : AppCompatActivity() {
//
//    private var binding: ActivityProfileBinding? = null
//    lateinit var sharedPreferences: SharedPreferences
//    private lateinit var profileViewModel: ProfileViewModel
//    private var id: String? = null
//    private var isCollector: Boolean = false
//
//    var PREF_LOGIN = "login"
//    var PREF_TOKEN = "token"
//    var PREF_EMAIL = "email"
//    var PREF_USERNAME = "username"
//
//    var email = ""
//    var token = ""
//    var username = ""
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityProfileBinding.inflate(layoutInflater)
//        setContentView(binding?.root)
//
//        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
//        token = sharedPreferences.getString(PREF_TOKEN, null)!!
//        email = sharedPreferences.getString(PREF_EMAIL, null)!!
//
//        setupViewModel()
//        getData()
//
//        binding?.tvProfileName?.text = email
//
//        if (isCollector == true) {
//            binding?.tvPengepul?.text = getString(R.string.update_collector)
//            binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_update_24dp)
//        } else if (isCollector == false) {
//            binding?.tvPengepul?.text = getString(R.string.join_collector)
//            binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_add_circle_no_fill_24dp)
//        }
//
//        binding?.btnAddPengepul?.setOnClickListener {
//            if (isCollector) {
//                val intent = Intent(this@ProfileActivity, UpdatePengepulActivity::class.java)
//                intent.putExtra(UpdatePengepulActivity.id, id)
//                startActivity(intent)
//            } else {
//                val intent = Intent(this, AddPengepulActivity::class.java)
//                startActivity(intent)
//            }
//        }
//
//
//        binding?.logoutButton?.setOnClickListener {
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            editor.clear()
//            editor.apply()
//            Toast.makeText(this@ProfileActivity, "Logout Successful", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//            finish()
//        }
//
//
//    }
//
//    private fun getData() {
//        profileViewModel.getMyData(token, username).observe(this) { result ->
//            when (result) {
//                is Result.Loading -> {
//
//                }
//                is Result.Success -> {
//                    id = result.data
//                    isCollector = true
////                    binding?.textView4?.text = result.data
//                    binding?.tvPengepul?.text = getString(R.string.update_collector)
//                    binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_update_24dp)
//
//                }
//                is Result.Error -> {
//                    id = ""
//                    isCollector = false
////                    binding?.textView4?.text = "kontol"
//                    binding?.tvPengepul?.text = getString(R.string.join_collector)
//                    binding?.iconPengepul?.setBackgroundResource(R.drawable.ic_baseline_add_circle_no_fill_24dp)
////                        createToast(this@ProfileActivity, getString(R.string.failed_fetch_data))
//                }
//            }
//        }
//
//    }
//
//    private fun setupViewModel() {
//        val factory: ViewModelFactory = ViewModelFactory.getInstance(applicationContext)
//        profileViewModel =
//            ViewModelProvider(this@TempProfile, factory)[ProfileViewModel::class.java]
//    }
}
