package com.example.cardindexproject

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cardindexproject.controllers.AnimationController
import com.example.cardindexproject.settings.Settings
import com.example.cardindexproject.viewmodels.RegistrationViewModel
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageButton

class RegistrationActivity : AppCompatActivity() {
    private val registrationViewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    // Класс с настройками
    private lateinit var settings: Settings

    // Для анимации глаза
    private val COUNT_EYES: Int = 2
    private var arrayEyeAnimControllers: Array<AnimationController> = arrayOf()

    // Все view
    private lateinit var arrayGifImageButtonsEyes: Array<GifImageButton>
    private lateinit var arrayEditTextTextPasswords: Array<EditText>
    private lateinit var main: LinearLayout
    private lateinit var toolbar: Toolbar
    private lateinit var imageButtonBack: ImageView
    private lateinit var textViewToolbar: TextView
    private lateinit var imageViewLogo: ImageView
    private lateinit var editTextTextFIO: EditText
    private lateinit var editTextTextEmailAddress: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var linearLayoutAlternativeMenu: LinearLayout
    private lateinit var editTextInstitutionName: EditText
    private lateinit var editTextInstitutionINN: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonClinic: AppCompatRadioButton
    private lateinit var radioButtonPolyclinic: AppCompatRadioButton
    private lateinit var radioButtonOrganization: AppCompatRadioButton
    private lateinit var buttonRegistration: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = Settings(this)
        setTheme(settings.colorsSettings.getTheme())
        setContentView(R.layout.activity_registration)
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
        // Останавка анимаций для глаз
        for (i in arrayEyeAnimControllers.indices) {
            arrayEyeAnimControllers[i].stopAnimation()
        }
        // Сохранение всех важных данных с страницы в ViewModel
        registrationViewModel.loginText = editTextTextEmailAddress.text.toString()
        registrationViewModel.passwordText = arrayEditTextTextPasswords[0].text.toString()
        registrationViewModel.confirmPasswordText = arrayEditTextTextPasswords[1].text.toString()
        registrationViewModel.fioText = editTextTextFIO.text.toString()
        registrationViewModel.phoneText = editTextPhone.text.toString()
        registrationViewModel.institutionNameText = editTextInstitutionName.text.toString()
        registrationViewModel.institutionINNText = editTextInstitutionINN.text.toString()
        registrationViewModel.idInstitutionType =
            radioGroup.indexOfChild(findViewById(radioGroup.checkedRadioButtonId))
    }

    override fun onResume() {
        super.onResume()
        // Вставка состояний глаз и текстовых полей с паролем с ViewModel на страницу
        for (i in arrayEyeAnimControllers.indices) {
            arrayEyeAnimControllers[i].changeAnimPosition(registrationViewModel.arrayEyesStates[i])
            setPasswordState(i)
        }
        // Вставка всех важных данных с ViewModel на страницу
        editTextTextEmailAddress.setText(registrationViewModel.loginText)
        arrayEditTextTextPasswords[0].setText(registrationViewModel.passwordText)
        arrayEditTextTextPasswords[1].setText(registrationViewModel.confirmPasswordText)
        editTextTextFIO.setText(registrationViewModel.fioText)
        editTextPhone.setText(registrationViewModel.phoneText)
        editTextInstitutionName.setText(registrationViewModel.institutionNameText)
        editTextInstitutionINN.setText(registrationViewModel.institutionINNText)
        val radioButtonChecked =
            radioGroup.getChildAt(registrationViewModel.idInstitutionType) as AppCompatRadioButton
        radioButtonChecked.isChecked = true
    }

    /** * Initializing animations. */
    private fun initAnimations() {
        for (i in 0 until COUNT_EYES) {
            arrayEyeAnimControllers += AnimationController(this, R.drawable.icon_eye)
            arrayEyeAnimControllers[i].setGifPositions(
                arrayOf(
                    0,
                    GifDrawable(this.resources, R.drawable.icon_eye).duration / 2
                )
            )
        }
        arrayGifImageButtonsEyes = arrayOf(
            findViewById(R.id.gifImageButtonEye),
            findViewById(R.id.gifImageButtonConfirmEye)
        )
        arrayGifImageButtonsEyes[0].setImageDrawable(arrayEyeAnimControllers[0].getGifDrawable())
        arrayGifImageButtonsEyes[1].setImageDrawable(arrayEyeAnimControllers[1].getGifDrawable())
        // Нажатие на глаза
        arrayGifImageButtonsEyes[0].setOnClickListener {
            changeEyeState(0)
        }
        arrayGifImageButtonsEyes[1].setOnClickListener() {
            changeEyeState(1)
        }
    }

    /** * Initializing views. */
    private fun initAllViews() {
        main = findViewById(R.id.main)
        toolbar = findViewById(R.id.toolbar)
        imageButtonBack = findViewById(R.id.imageButtonBack)
        textViewToolbar = findViewById(R.id.textViewToolbar)
        imageViewLogo = findViewById(R.id.imageViewLogo)
        editTextTextFIO = findViewById(R.id.editTextTextFIO)
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress)
        editTextPhone = findViewById(R.id.editTextPhone)
        arrayEditTextTextPasswords = arrayOf(
            findViewById(R.id.editTextTextPassword),
            findViewById(R.id.editTextConfirmTextPassword)
        )
        linearLayoutAlternativeMenu = findViewById(R.id.linearLayoutAlternativeMenu)
        editTextInstitutionName = findViewById(R.id.editTextInstitutionName)
        editTextInstitutionINN = findViewById(R.id.editTextInstitutionINN)
        radioGroup = findViewById(R.id.radioGroup)
        radioButtonClinic = findViewById(R.id.radioButtonClinic)
        radioButtonPolyclinic = findViewById(R.id.radioButtonPolyclinic)
        radioButtonOrganization = findViewById(R.id.radioButtonOrganization)
        buttonRegistration = findViewById(R.id.buttonRegistration)
        // Listener'ы
        imageButtonBack.setOnClickListener { onGoPreviousActivity() }
    }

    /** * Set colors for views. */
    private fun setColors() {
        main.setBackgroundResource(settings.colorsSettings.getGradientBackground())
        toolbar.setBackgroundResource(settings.colorsSettings.getGradientHeader())
        linearLayoutAlternativeMenu.setBackgroundResource(settings.colorsSettings.getAlternativeMenuShape())
        CompoundButtonCompat.setButtonTintList(
            radioButtonClinic,
            ContextCompat.getColorStateList(this, settings.colorsSettings.getColorImportant())
        )
        CompoundButtonCompat.setButtonTintList(
            radioButtonPolyclinic,
            ContextCompat.getColorStateList(this, settings.colorsSettings.getColorImportant())
        )
        CompoundButtonCompat.setButtonTintList(
            radioButtonOrganization,
            ContextCompat.getColorStateList(this, settings.colorsSettings.getColorImportant())
        )
        buttonRegistration.setBackgroundResource(settings.colorsSettings.getColorImportant())
    }

    /** * Set password state (show or don't show password).
     * @param id Id of eye gif */
    private fun setPasswordState(idEye: Int) {
        if (registrationViewModel.arrayEyesStates[idEye] == 0) {
            arrayEditTextTextPasswords[idEye].transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            arrayEditTextTextPasswords[idEye].setSelection(arrayEditTextTextPasswords[idEye].length())
            return
        }
        arrayEditTextTextPasswords[idEye].transformationMethod =
            PasswordTransformationMethod.getInstance()
        arrayEditTextTextPasswords[idEye].setSelection(arrayEditTextTextPasswords[idEye].length())
    }

    /** * Change state of eye animation (open\close).
     * @param idEye Id of eye gif. */
    private fun changeEyeState(idEye: Int) {
        val eyeAnimPosition = arrayEyeAnimControllers[idEye].startAnimation()
        // Если анимация запустилась
        if (eyeAnimPosition >= 0) {
            registrationViewModel.arrayEyesStates[idEye] = eyeAnimPosition
            setPasswordState(idEye)
        }
    }

    private fun onGoPreviousActivity() {
        finish()
    }
}