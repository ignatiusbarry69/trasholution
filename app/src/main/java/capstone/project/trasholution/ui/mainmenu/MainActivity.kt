package capstone.project.trasholution.ui.mainmenu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityMainBinding
import capstone.project.trasholution.ui.mainmenu.article.ArticleFragment
import capstone.project.trasholution.ui.mainmenu.collector.CollectorFragment
import capstone.project.trasholution.ui.mainmenu.prediction.CameraActivity
import capstone.project.trasholution.ui.profile.ProfileActivity
import rotateFile
import java.io.File

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setupView()
        setupAction()

        binding?.bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.collector -> replaceFragment(CollectorFragment())
                R.id.idea -> replaceFragment(ArticleFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()
    }

    private fun setupAction() {
        binding?.fab?.setOnClickListener{
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
        binding?.bottomNavigationView?.background = null
        binding?.bottomNavigationView?.menu?.getItem(1)?.isEnabled = false

        val fragmentManager = supportFragmentManager
        val homeFragment = ArticleFragment()
        val fragment = fragmentManager.findFragmentByTag(ArticleFragment::class.java.simpleName)

        if (fragment !is ArticleFragment) {
            fragmentManager.commit {
                add(R.id.frame_container, homeFragment, ArticleFragment::class.java.simpleName)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
