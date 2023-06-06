package capstone.project.trasholution.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityProfileBinding
import capstone.project.trasholution.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private var binding: ActivityProfileBinding? = null
    lateinit var sharedPreferences: SharedPreferences

    var PREF_LOGIN = "login"
    var PREF_TOKEN = "token"
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!

        binding?.logoutButton?.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(this@ProfileActivity, "Logout Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }
}