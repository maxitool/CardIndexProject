package com.example.cardindexproject

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cardindexproject.controllers.AnimationController
import com.example.cardindexproject.settings.Settings
import com.google.android.material.navigation.NavigationView
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageButton

class AccountActivity : AppCompatActivity() {
    // Класс с настройками
    private lateinit var settings: Settings
    // Контроллер для анимации меню
    private lateinit var menuAnimController: AnimationController
    // Все view
    private lateinit var scrollView: ScrollView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationMenu: NavigationView
    private lateinit var gifImageButtonMenu: GifImageButton
    private lateinit var imageButtonChangePhoto: ImageButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonEditing: AppCompatButton
    private lateinit var buttonCancel: AppCompatButton
    private lateinit var textViewInstitutionNames: TextView

    private var isMenuOpened: Boolean = false
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = Settings(this)
        setTheme(settings.colorsSettings.getTheme())
        setContentView(R.layout.activity_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAllViews()
        initAnimations()
    }
    /** * Initializing views. */
    private fun initAllViews() {
        scrollView = findViewById(R.id.scrollView)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationMenu = findViewById(R.id.navigationMenu)
        gifImageButtonMenu = findViewById(R.id.gifImageButtonMenu)
        imageButtonChangePhoto = findViewById(R.id.imageButtonChangePhoto)
        radioGroup = findViewById(R.id.radioGroup)
        buttonEditing = findViewById(R.id.buttonEditing)
        buttonCancel = findViewById(R.id.buttonCancel)
        textViewInstitutionNames = findViewById(R.id.textViewInstitutionNames)
        //Listener'ы
        gifImageButtonMenu.setOnClickListener { onMenuClicked() }
        buttonEditing.setOnClickListener { onChangeEditState() }
    }
    /** * Initializing animations. */
    private fun initAnimations() {
        menuAnimController = AnimationController(this, R.drawable.icon_menu)
        menuAnimController.setGifPositions(arrayOf(0, GifDrawable(this.resources, R.drawable.icon_menu).duration / 2))
        gifImageButtonMenu.setImageDrawable(menuAnimController.getGifDrawable())
    }
    private fun onChangeEditState() {
        if (isEditMode) {
            buttonEditing.setText(R.string.edit_ru)
            buttonCancel.visibility = View.GONE
            imageButtonChangePhoto.visibility = View.GONE
            radioGroup.visibility = View.GONE
            // Тут могут быть разные тексты, нужно реализовать (Клиника / Поликлиника / ВЧ (организация))
            textViewInstitutionNames.apply {
                setText(R.string.clinic_ru)
                gravity = Gravity.CENTER
                textSize = 20F
            }
        }
        else {
            buttonEditing.setText(R.string.accept_ru)
            buttonCancel.visibility = View.VISIBLE
            imageButtonChangePhoto.visibility = View.VISIBLE
            radioGroup.startAnimation((AnimationUtils.loadAnimation(this, R.anim.drop_down)))
            radioGroup.visibility = View.VISIBLE
            textViewInstitutionNames.apply {
                setText(R.string.institutions_names_ru)
                gravity = Gravity.START
                textSize = 16F
            }
        }
        isEditMode = !isEditMode
    }
    private fun onMenuClicked() {
        val animPosition = menuAnimController.startAnimation()
        if (animPosition < 0) return
        if (isMenuOpened)
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            drawerLayout.openDrawer(GravityCompat.START)
        isMenuOpened = !isMenuOpened
    }
}