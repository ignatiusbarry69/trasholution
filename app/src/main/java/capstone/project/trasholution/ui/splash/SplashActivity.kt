package capstone.project.trasholution.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import capstone.project.trasholution.R
import capstone.project.trasholution.ui.login.LoginActivity
import capstone.project.trasholution.ui.mainmenu.MainActivity
import capstone.project.trasholution.ui.onboard.OnboardActivity

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, OnboardActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000L)


    }
}