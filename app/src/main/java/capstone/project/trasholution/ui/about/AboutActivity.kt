package capstone.project.trasholution.ui.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityAboutBinding
import capstone.project.trasholution.databinding.ActivityProfileBinding
import capstone.project.trasholution.ui.profile.ProfileActivity

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        
        binding.btnBackAboutPage.setOnClickListener {
            val intent = Intent(this@AboutActivity, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}