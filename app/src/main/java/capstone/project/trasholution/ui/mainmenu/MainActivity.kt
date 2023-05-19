package capstone.project.trasholution.ui.mainmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityMainBinding
import capstone.project.trasholution.ui.mainmenu.article.ArticleFragment
import capstone.project.trasholution.ui.mainmenu.collector.CollectorFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.collector -> replaceFragment(CollectorFragment())
                R.id.idea -> replaceFragment(ArticleFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
    }
    private fun setupView() {
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        val fragmentManager = supportFragmentManager
        val homeFragment = ArticleFragment()
        val fragment = fragmentManager.findFragmentByTag(ArticleFragment::class.java.simpleName)

        if (fragment !is ArticleFragment) {
            fragmentManager.commit {
                add(R.id.frame_container, homeFragment, ArticleFragment::class.java.simpleName)

            }
        }
    }
}