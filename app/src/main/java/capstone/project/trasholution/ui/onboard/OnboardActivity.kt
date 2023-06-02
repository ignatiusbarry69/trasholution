package capstone.project.trasholution.ui.onboard


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityOnboardBinding
import capstone.project.trasholution.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardActivity : AppCompatActivity() {
    private var binding : ActivityOnboardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        hideSystemUI()
        setupView()
        setupAnimation()
    }

    private fun setupView(){
        //view pager 2
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding!!.viewPager
        viewPager.adapter = sectionsPagerAdapter

        //tab layout
        val tabs: TabLayout = binding!!.tabs

        binding?.btnLeft?.text = "SKIP"
        binding?.btnRight?.text = "NEXT"

        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = position.toString()
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position // Get the selected tab position

                // Update button behavior based on the selected tab position
                if (position == 0) {
                    binding?.btnLeft?.text = "SKIP"
                    binding?.btnRight?.text = "NEXT"
                } else if (position == 1) {
                    binding?.btnLeft?.text = "BACK"
                    binding?.btnRight?.text = "NEXT"
                } else {
                    binding?.btnLeft?.text = "BACK"
                    binding?.btnRight?.text = "START"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Not needed for your implementation
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Not needed for your implementation
            }
        })

        // Set click listeners for the buttons
        binding?.btnLeft?.setOnClickListener {

            if (binding?.btnLeft?.text == "SKIP") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val previousTab = tabs.getTabAt(tabs.selectedTabPosition - 1)
                previousTab?.select()
            }
        }

        binding?.btnRight?.setOnClickListener {
            if (binding?.btnRight?.text == "START") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val nextTab = tabs.getTabAt(tabs.selectedTabPosition + 1)
                nextTab?.select()
            }
        }

        supportActionBar?.elevation = 0f

    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAnimation(){

    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}