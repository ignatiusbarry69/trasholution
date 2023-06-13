package capstone.project.trasholution.ui.mainmenu.article

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import capstone.project.trasholution.databinding.ActivityAddArticleBinding
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
//import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.logic.repository.retrofit.ApiConfig
import capstone.project.trasholution.ui.mainmenu.MainActivity
import capstone.project.trasholution.ui.mainmenu.prediction.CameraActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import reduceFileImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rotateFile
import uriToFile
import java.io.File

class AddArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddArticleBinding
    private var getFile: File? = null
    lateinit var sharedPreferences: SharedPreferences

    var PREF_LOGIN = "login"
    var PREF_TOKEN = "token"
    var token = ""

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, "Don't get a permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(PREF_TOKEN, null)!!

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnSubmitStory.setOnClickListener { uploadImage() }
    }

    private fun startCameraX() {
        val intent = Intent(this@AddArticleActivity, ArticleCameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val edtDescription = binding.edtAddArticleDescription.text.toString()
            val edtTitle = binding.edtAddArticleTitle.text.toString()
            val edtType = binding.edtAddArticleType.text.toString()
            val description = edtDescription.toRequestBody("text/plain".toMediaType())
            val title = edtTitle.toRequestBody("text/plain".toMediaType())
            val type = edtType.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )

            val apiService = ApiConfig.getApiService(token).addArtikel(title, type, description, imageMultipart)
            apiService.enqueue(object : Callback<ArticleAddItem> {
                override fun onResponse(call: Call<ArticleAddItem>, response: Response<ArticleAddItem>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddArticleActivity, "Article Added Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddArticleActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d("TAGadderr", response.message())
                        Toast.makeText(this@AddArticleActivity, "Failed to Add Article", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArticleAddItem>, t: Throwable) {
                    Log.d("TAGadderr", t.toString())
                    Toast.makeText(this@AddArticleActivity, "Please select a file first", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddArticleActivity)
                getFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}