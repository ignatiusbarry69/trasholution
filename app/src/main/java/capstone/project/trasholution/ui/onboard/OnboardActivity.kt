package capstone.project.trasholution.ui.onboard


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import capstone.project.trasholution.ui.splash.SplashActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardActivity : AppCompatActivity() {
    private var binding : ActivityOnboardBinding? = null

    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences = this.getSharedPreferences("prefLaunch", Context.MODE_PRIVATE)
        val firstLaunch = sharedPreferences.getBoolean("firstLaunch", true)

        if (!firstLaunch) {
            val intent = Intent(this@OnboardActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            sharedPreferences.edit().putBoolean("firstLaunch", false).apply()
        }
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

        binding?.btnLeft?.text = "Skip"
        binding?.btnRight?.text = "Next"
        setupCircleFirst()


        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = position.toString()
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position // Get the selected tab position

                // Update button behavior based on the selected tab position
                if (position == 0) {
                    setupCircleSecond()
                    binding?.btnLeft?.text = "Skip"
                    binding?.btnRight?.text = "Next"
                    binding?.circleOne?.setBackgroundResource(R.drawable.circle_shape_lime)

                } else if (position == 1) {
                    setupCircleSecond()
                    binding?.btnLeft?.text = "Back"
                    binding?.btnRight?.text = "Next"
                    binding?.circleTwo?.setBackgroundResource(R.drawable.circle_shape_lime)
                } else {
                    setupCircleSecond()
                    binding?.btnLeft?.text = "Back"
                    binding?.btnRight?.text = "Start"
                    binding?.circleThree?.setBackgroundResource(R.drawable.circle_shape_lime)
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

            if (binding?.btnLeft?.text == "Skip") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val previousTab = tabs.getTabAt(tabs.selectedTabPosition - 1)
                previousTab?.select()
            }
        }

        binding?.btnRight?.setOnClickListener {
            if (binding?.btnRight?.text == "Start") {
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

    private fun setupCircleFirst() {
        binding?.circleOne?.setBackgroundResource(R.drawable.circle_shape_lime)
        binding?.circleTwo?.setBackgroundResource(R.drawable.circle_shape)
        binding?.circleThree?.setBackgroundResource(R.drawable.circle_shape)
    }

    private fun setupCircleSecond() {
        binding?.circleOne?.setBackgroundResource(R.drawable.circle_shape)
        binding?.circleTwo?.setBackgroundResource(R.drawable.circle_shape)
        binding?.circleThree?.setBackgroundResource(R.drawable.circle_shape)
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