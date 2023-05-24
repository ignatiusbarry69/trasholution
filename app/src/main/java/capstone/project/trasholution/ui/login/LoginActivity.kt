package capstone.project.trasholution.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityLoginBinding
import capstone.project.trasholution.logic.remote.LoginResponse
import capstone.project.trasholution.logic.repository.retrofit.ApiConfig
import capstone.project.trasholution.ui.mainmenu.MainActivity
import capstone.project.trasholution.ui.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    var PREF_LOGIN = "login"
    var PREF_EMAIL = "email"
    var PREF_PASSWORD = "password"
    var PREF_TOKEN = "token"

    var email = ""
    var password = ""
    var validation = Patterns.EMAIL_ADDRESS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(PREF_EMAIL, "").toString()
        password = sharedPreferences.getString(PREF_PASSWORD, "").toString()

        binding.edtLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })

        binding.edtLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
        binding.btnLoginSubmit.setOnClickListener { login() }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        playAnimation()
    }

    private fun login() {
        val email = binding.edtLoginEmail.text.toString()
        val password = binding.edtLoginPassword.text.toString()

        val apiService = ApiConfig.getApiService()
        val login = apiService.loginUser(email, password)
        showLoading(true)
        login.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString(PREF_EMAIL, binding.edtLoginEmail.text.toString())
                        editor.putString(PREF_PASSWORD, binding.edtLoginPassword.text.toString())
                        editor.putString(PREF_TOKEN, responseBody.data.token)
                        editor.apply()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)

//                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                        startActivity(intent)
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (!email.equals("") && !password.equals("")) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLogin.visibility = View.VISIBLE
        } else {
            binding.progressBarLogin.visibility = View.INVISIBLE
        }
    }

    private fun setMyButtonEnable() {
        val emailResult = binding.edtLoginEmail.text
        val passwordResult = binding.edtLoginPassword.text

        binding.btnLoginSubmit.isEnabled = emailResult != null && passwordResult != null && emailResult.toString().isNotEmpty()
                && passwordResult.toString().isNotEmpty() && validation.matcher(emailResult.toString()).matches() && passwordResult.toString().length >= 8
    }

    private fun playAnimation() {

        val titleLoginAnimation = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val titleEmailAnimation = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val titlePasswordAnimation = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val emailAnimation =  ObjectAnimator.ofFloat(binding.edtLoginEmailLayout, View.ALPHA, 1f).setDuration(500)
        val passwordAnimation =  ObjectAnimator.ofFloat(binding.edtLoginPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val buttonSubmitAnimation = ObjectAnimator.ofFloat(binding.btnLoginSubmit, View.ALPHA, 1f).setDuration(500)
        val messageAnimation = ObjectAnimator.ofFloat(binding.message, View.ALPHA, 1f).setDuration(500)

        val togetherEmail = AnimatorSet().apply {
            playTogether(titleEmailAnimation, emailAnimation)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(titlePasswordAnimation, passwordAnimation)
        }

        AnimatorSet().apply {
            playSequentially(titleLoginAnimation, togetherEmail, togetherPassword, buttonSubmitAnimation, messageAnimation)
            start()
        }

    }
}