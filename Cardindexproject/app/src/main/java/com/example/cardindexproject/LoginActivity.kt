package com.example.cardindexproject

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cardindexproject.controllers.AnimationController
import com.example.cardindexproject.settings.Settings
import com.example.cardindexproject.viewmodels.LoginViewModel
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageButton

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    // Класс с настройками
    private lateinit var settings: Settings

    // Все для анимации глаза
    private lateinit var eyeAnimController: AnimationController

    // Все view
    private lateinit var main: LinearLayout
    private lateinit var toolbar: Toolbar
    private lateinit var textViewToolbar: TextView
    private lateinit var imageViewLogo: ImageView
    private lateinit var editTextTextEmailAddress: EditText
    private lateinit var editTextTextPassword: EditText
    private lateinit var gifImageButtonEye: GifImageButton
    private lateinit var textViewRestorePassword: TextView
    private lateinit var checkBoxRememberMe: AppCompatCheckBox
    private lateinit var buttonLogin: AppCompatButton
    private lateinit var buttonRegistration: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = Settings(this)
        setTheme(settings.colorsSettings.getTheme())
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAllViews()
        initAnimations()
        setColors()
    }

    override fun onPause() {
        super.onPause()
        eyeAnimController.stopAnimation()
        loginViewModel.loginText = editTextTextEmailAddress.text.toString()
        loginViewModel.passwordText = editTextTextPassword.text.toString()
    }

    override fun onResume() {
        super.onResume()
        eyeAnimController.changeAnimPosition(loginViewModel.eyeAnimPosition)
        setPasswordState()
        editTextTextEmailAddress.setText(loginViewModel.loginText)
        editTextTextPassword.setText(loginViewModel.passwordText)
    }

    /** * Initializing animations. */
    private fun initAnimations() {
        eyeAnimController = AnimationController(this, R.drawable.icon_eye)
        eyeAnimController.setGifPositions(
            arrayOf(
                0,
                GifDrawable(this.resources, R.drawable.icon_eye).duration / 2
            )
        )
        gifImageButtonEye = findViewById(R.id.gifImageButtonEye)
        gifImageButtonEye.setImageDrawable(eyeAnimController.getGifDrawable())
        // нажатие на глаз
        gifImageButtonEye.setOnClickListener {
            changeEyeState()
        }
    }

    /** * Initializing views. */
    private fun initAllViews() {
        main = findViewById(R.id.main)
        toolbar = findViewById(R.id.toolbar)
        textViewToolbar = findViewById(R.id.textViewToolbar)
        imageViewLogo = findViewById(R.id.imageViewLogo)
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress)
        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        textViewRestorePassword = findViewById(R.id.textViewRestorePassword)
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegistration = findViewById(R.id.buttonRegistration)
        // Listener'ы
        buttonRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
        }
    }

    /** * Set colors for views. */
    private fun setColors() {
        main.setBackgroundResource(settings.colorsSettings.getGradientBackground())
        toolbar.setBackgroundResource(settings.colorsSettings.getGradientHeader())
        CompoundButtonCompat.setButtonTintList(
            checkBoxRememberMe,
            ContextCompat.getColorStateList(this, settings.colorsSettings.getColorImportant())
        )
        buttonLogin.setBackgroundResource(settings.colorsSettings.getColorImportant())
    }

    /** * Set password state (show or don't show password). */
    private fun setPasswordState() {
        // Глаз открыт
        if (loginViewModel.eyeAnimPosition == 0) {
            editTextTextPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            editTextTextPassword.setSelection(editTextTextPassword.length())
            return
        }
        editTextTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        editTextTextPassword.setSelection(editTextTextPassword.length())
    }

    /** * Change state of password and eye animation (open\close). */
    private fun changeEyeState() {
        val eyeAnimPosition = eyeAnimController.startAnimation()
        // Если анимация запустилась
        if (eyeAnimPosition >= 0) {
            loginViewModel.eyeAnimPosition = eyeAnimPosition
            setPasswordState()
        }
    }
}