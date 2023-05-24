package capstone.project.trasholution.ui.onboard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
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
//        val fragmentManager = supportFragmentManager
//        val onboardFragment = OnboardFragment()
//        val fragment = fragmentManager.findFragmentByTag(OnboardFragment::class.java.simpleName)
//
//        if (fragment !is OnboardFragment) {
//            fragmentManager.commit {
//                add(R.id.frame_container2, onboardFragment, OnboardFragment::class.java.simpleName)
//            }
//        }

        //view pager 2
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding!!.viewPager
        viewPager.adapter = sectionsPagerAdapter

        //tab layout
        val tabs: TabLayout = binding!!.tabs

//        tabs.selectTab(binding?.tabs?.getTabAt(1))
//        val selectedPosition = tabs.selectedTabPosition

        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = position.toString()
        }.attach()

//        if (selectedPosition == 0) {
//            binding?.btnLeft?.text = "SKIP"
//            binding?.btnLeft?.setOnClickListener {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            }
//            binding?.btnRight?.text = "NEXT"
//            binding?.btnRight?.setOnClickListener {
//                val tabLayout = this.findViewById(R.id.tabs) as TabLayout
//                val tab = tabLayout.getTabAt(1)
//                tab!!.select()
//            }
//        }else if(selectedPosition == 1){
//            binding?.btnLeft?.text = "BACK"
//            binding?.btnLeft?.setOnClickListener {
//                val tabLayout = this.findViewById(R.id.tabs) as TabLayout
//                val tab = tabLayout.getTabAt(0)
//                tab!!.select()
//            }
//            binding?.btnRight?.text = "NEXT"
//            binding?.btnRight?.setOnClickListener {
//                val tabLayout = this.findViewById(R.id.tabs) as TabLayout
//                val tab = tabLayout.getTabAt(2)
//                tab!!.select()
//            }
//
//        }else{
//            binding?.btnLeft?.text = selectedPosition.toString()
//            binding?.btnLeft?.setOnClickListener {
//                val tabLayout = this.findViewById(R.id.tabs) as TabLayout
//                val tab = tabLayout.getTabAt(1)
//                tab!!.select()
//            }
//            binding?.btnRight?.text = "START"
//            binding?.btnRight?.setOnClickListener {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            }
//        }

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