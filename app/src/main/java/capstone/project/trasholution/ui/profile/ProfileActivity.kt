package capstone.project.trasholution.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityProfileBinding
import capstone.project.trasholution.logic.repository.Result
import capstone.project.trasholution.ui.ViewModelFactory
import capstone.project.trasholution.ui.login.LoginActivity
import capstone.project.trasholution.ui.mainmenu.article.DetailArticleActivity
import capstone.project.trasholution.ui.mainmenu.collector.AddPengepulActivity
import capstone.project.trasholution.ui.mainmenu.collector.UpdatePengepulActivity




class ProfileActivity : AppCompatActivity() {

    private var binding: ActivityProfileBinding? = null
    private lateinit var profileViewModel: ProfileViewModel
    private var id: String? = null
    private var isCollector: Boolean = false
    lateinit var sharedPreferences: SharedPreferences

    var PREF_USERNAME = "username"
    var PREF_LOGIN = "login"
    var PREF_TOKEN = "token"
    var token = ""
    var username = "Paijo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!
        username = "Paijo"

        setupViewModel()
        getData()



        binding?.textView4?.text = username

        binding?.buttonJoin?.setOnClickListener{
            if (isCollector) {
                val intent = Intent(this, UpdatePengepulActivity::class.java)
                intent.putExtra(UpdatePengepulActivity.id, id)
                Log.d("tag", id.toString())

                startActivity(intent)
            } else {
                val intent = Intent(this, AddPengepulActivity::class.java)
                startActivity(intent)
            }
        }

        binding?.logoutButton?.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.alert))
                setMessage(getString(R.string.yousure))
                setPositiveButton(R.string.logout) { _, _ ->
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                    Toast.makeText(this@ProfileActivity, "Logout Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }


    }

    private fun getData(){
        profileViewModel.getMyData(token, username).observe(this){ result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    id = result.data
                    isCollector = true
//                    binding?.textView4?.text = result.data
                    binding?.buttonJoin?.text = getString(R.string.update_collector)


                }
                is Result.Error -> {
                    id = ""
                    isCollector = false
//                    binding?.textView4?.text = "kontol"
                    binding?.buttonJoin?.text = getString(R.string.join_collector)
//                        createToast(this@ProfileActivity, getString(R.string.failed_fetch_data))
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