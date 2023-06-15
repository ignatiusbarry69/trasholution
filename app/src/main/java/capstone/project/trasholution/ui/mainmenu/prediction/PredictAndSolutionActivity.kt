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
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!
//        hideSystemUI()
        setupViewModel()
        showLoading(false, binding!!.progressBar)


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
            predict(myToken, imageMultipart)

        }catch(e: Exception){
            createToast(this, e.toString())
            Log.d("TAGfailure", e.toString())
        }
    }

    private fun predict(myToken: String, imageMultipart: MultipartBody.Part){
        mainViewModel.predict(myToken, imageMultipart).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding!!.progressBar)
                    binding?.prediction?.visibility = View.GONE
                    binding?.desc?.visibility = View.GONE
                    binding?.predictBtn?.isEnabled = false
                    binding?.predictBtn?.text = getString(R.string.find_more)
                    binding?.predictBtn?.alpha = 0.5f
                }
                is Result.Success -> {
                    showLoading(false, binding!!.progressBar)
                    val predictedValue = result.data
                    binding?.prediction?.text = predictedValue
                    binding?.prediction?.visibility = View.VISIBLE
                    binding?.predictBtn?.isEnabled = true
                    binding?.predictBtn?.text = getString(R.string.find_more)
                    binding?.predictBtn?.alpha = 1.0f

                    binding?.desc?.text = when(predictedValue){
                        "Electronic Waste" -> getString(R.string.ewaste_handling)
                        "Food Scraps" -> getString(R.string.foodscrap_handling)
                        "Glass" -> getString(R.string.glass_handling)
                        "Metalic Materials" -> getString(R.string.metalwaste_handling)
                        "Paper" -> getString(R.string.paperwaste_handling)
                        "Plastic" -> getString(R.string.plasticwaste_handling)
                        "Textile" -> getString(R.string.textilewaste_handling)
                        "Organic Vegetation Waste" -> getString(R.string.organicwaste_handling)
                        else -> getString(R.string.no_handling)
                    }
                    
                    binding?.desc?.visibility = View.VISIBLE

                    binding?.predictBtn?.setOnClickListener {
                        val intent = Intent(this@PredictAndSolutionActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
                is Result.Error -> {
                    showLoading(false, binding!!.progressBar)
                    createToast(this, result.error)
                    Log.d("TAGerror", result.error)
                    binding?.predictBtn?.text = getString(R.string.try_again)
                    binding?.predictBtn?.isEnabled = true

                    binding?.predictBtn?.setOnClickListener {
                        predict(myToken, imageMultipart)
                    }
                }
            }
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