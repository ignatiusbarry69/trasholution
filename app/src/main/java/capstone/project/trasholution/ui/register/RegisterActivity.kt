package capstone.project.trasholution.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityRegisterBinding
import capstone.project.trasholution.logic.remote.UserResponse
import capstone.project.trasholution.logic.repository.retrofit.ApiConfig
import capstone.project.trasholution.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    var validation = Patterns.EMAIL_ADDRESS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.edtRegisterName.addTextChangedListener(object : TextWatcher {
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
        binding.edtRegisterEmail.addTextChangedListener(object : TextWatcher {
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
        binding.edtRegisterPassword.addTextChangedListener(object : TextWatcher {
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
        binding.btnRegisterSubmit.setOnClickListener { register() }
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }

    private fun register() {
        val name = binding.edtRegisterName.text.toString()
        val email = binding.edtRegisterEmail.text.toString()
        val password = binding.edtRegisterPassword.text.toString()

        val apiService = ApiConfig.getApiService()
        val regis = apiService.registerUser(name, email, password)
        showLoading(true)
        regis.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(this@RegisterActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarRegister.visibility = View.VISIBLE
        } else {
            binding.progressBarRegister.visibility = View.INVISIBLE
        }
    }

    private fun setMyButtonEnable() {
        val nameResult = binding.edtRegisterName.text
        val emailResult = binding.edtRegisterEmail.text
        val passwordResult = binding.edtRegisterPassword.text

        binding.btnRegisterSubmit.isEnabled = emailResult != null && passwordResult != null && nameResult != null
                && emailResult.toString().isNotEmpty() && passwordResult.toString().isNotEmpty() && nameResult.toString().isNotEmpty()
                && validation.matcher(emailResult.toString()).matches() && passwordResult.toString().length >= 8
    }

    private fun playAnimation() {
        val titleRegisterAnimation = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val titleNameAnimation = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val titleEmailAnimation = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val titlePasswordAnimation = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val nameAnimation = ObjectAnimator.ofFloat(binding.edtRegisterNameLayout, View.ALPHA, 1f).setDuration(500)
        val emailAnimation = ObjectAnimator.ofFloat(binding.edtRegisterEmailLayout, View.ALPHA, 1f).setDuration(500)
        val passwordAnimation = ObjectAnimator.ofFloat(binding.edtRegisterPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val buttonSubmit = ObjectAnimator.ofFloat(binding.btnRegisterSubmit, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.message, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(titleNameAnimation, nameAnimation)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(titleEmailAnimation, emailAnimation)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(titlePasswordAnimation, passwordAnimation)
        }

        AnimatorSet().apply {
            playSequentially(titleRegisterAnimation, togetherName, togetherEmail, togetherPassword, buttonSubmit, message)
            start()
        }
    }
}