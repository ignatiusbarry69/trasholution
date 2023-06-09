package capstone.project.trasholution.ui.mainmenu.prediction

import android.content.Context
import capstone.project.trasholution.logic.repository.Result
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityPredictAndSolutionBinding
import capstone.project.trasholution.ui.ViewModelFactory
import capstone.project.trasholution.ui.mainmenu.MainActivity
import capstone.project.trasholution.ui.mainmenu.MainViewModel
import createToast
import cropFile
import kotlinx.coroutines.MainScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import rotateFile
import showLoading
import java.io.File

class PredictAndSolutionActivity : AppCompatActivity() {
    private var binding : ActivityPredictAndSolutionBinding? = null
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var getFile: File
    private lateinit var mainViewModel: MainViewModel

    var PREF_LOGIN = "login"
    var PREF_TOKEN = "token"
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictAndSolutionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!
//        hideSystemUI()
        setupViewModel()
        showLoading(false, binding!!.progressBar)
        binding?.predictBtn?.setOnClickListener {
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
            } as File

            val isBackCamera = intent.getBooleanExtra(ISBACK_CAMERA, true)

            picture.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file


                binding?.previewImageView?.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }

            val requestImageFile = getFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                getFile.name,
                requestImageFile
            )
            val myToken = "Bearer $token"
            mainViewModel.predict(myToken, imageMultipart).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true, binding!!.progressBar)
                        binding?.prediction?.visibility = View.GONE
                        binding?.desc?.visibility = View.GONE
                    }
                    is Result.Success -> {
                        showLoading(false, binding!!.progressBar)
                        val predictedValue = result.data
                        binding?.prediction?.text = predictedValue
                        binding?.prediction?.visibility = View.VISIBLE

                        //nambah percabangan description
                    }
                    is Result.Error -> {
                        showLoading(false, binding!!.progressBar)
                        createToast(this, result.error)
                        Log.d("TAGerror", result.error)
                    }
                }
            }

        }catch(e: Exception){
            createToast(this, e.toString())
            Log.d("TAGfailure", e.toString())
        }
    }
    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
    companion object {
        val ISBACK_CAMERA = "isBackCamera"
        val PICTURE = "picture"
        val PREDICTED = "predicted value"
    }
}