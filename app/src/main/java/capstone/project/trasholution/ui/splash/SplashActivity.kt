package capstone.project.trasholution.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import capstone.project.trasholution.R
import capstone.project.trasholution.ui.mainmenu.MainActivity
import capstone.project.trasholution.ui.onboard.OnboardActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, OnboardActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}