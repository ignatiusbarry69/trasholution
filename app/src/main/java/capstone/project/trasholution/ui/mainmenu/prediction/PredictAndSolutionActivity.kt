package capstone.project.trasholution.ui.mainmenu.prediction

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityPredictAndSolutionBinding
import capstone.project.trasholution.ui.mainmenu.MainActivity
import createToast
import cropFile
import kotlinx.coroutines.MainScope
import rotateFile
import java.io.File

class PredictAndSolutionActivity : AppCompatActivity() {
    private var binding : ActivityPredictAndSolutionBinding? = null
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictAndSolutionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        hideSystemUI()

        binding?.btnClose?.setOnClickListener {
            val intent = Intent(this@PredictAndSolutionActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

        try{
            val picture = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(PICTURE, File::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra(PICTURE)
            } as? File

            val isBackCamera = intent.getBooleanExtra(ISBACK_CAMERA, true) as Boolean

            picture?.let { file ->
                cropFile(file)
                //iki ngko kudu di crop, decodefile bitmap
                //entuk bitmap e, trs gawe utils scale bitmap
                //lg dirotate
                rotateFile(file, isBackCamera)
                getFile = file


                binding?.previewImageView?.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }catch(e: Exception){
            createToast(this, "something went wrong")
        }
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
    companion object {
        val ISBACK_CAMERA = "isBackCamera"
        val PICTURE = "picture"
    }
}