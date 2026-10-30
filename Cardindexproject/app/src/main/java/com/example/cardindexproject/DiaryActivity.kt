package com.example.cardindexproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardindexproject.adapters.RecyclerViewDocFragmentsAdapter
import com.example.cardindexproject.adapters.RecyclerViewDocumentsAdapter
import com.example.cardindexproject.controllers.AnimationController
import com.example.cardindexproject.fragments.DiaryFragment
import com.example.cardindexproject.fragments.MedicalCardFragment
import com.example.cardindexproject.settings.Settings
import com.example.cardindexproject.viewmodels.DiaryViewModel
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageButton

class DiaryActivity : AppCompatActivity(),
    RecyclerViewDocFragmentsAdapter.Callbacks,
    DiaryFragment.Callbacks,
    RecyclerViewDocumentsAdapter.Callbacks,
    MedicalCardFragment.Callbacks {

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(this)[DiaryViewModel::class.java]
    }

    // Класс с настройками
    private lateinit var settings: Settings

    // Контроллер для анимации меню
    private lateinit var menuAnimController: AnimationController

    private var arrayListDocuments: ArrayList<Int> = arrayListOf()
    private var isMenuOpened: Boolean = false

    // Все view
    private lateinit var gifImageButtonMenu: GifImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var imageButtonAccount: ImageButton
    private lateinit var recyclerViewDocuments: RecyclerView
    private lateinit var imageButtonDropListDocuments: ImageButton
    private lateinit var documentsAdapter: RecyclerViewDocFragmentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = Settings(this)
        setTheme(settings.colorsSettings.getTheme())
        setContentView(R.layout.activity_diary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        gifImageButtonMenu = findViewById(R.id.gifImageButtonMenu)
        drawerLayout = findViewById(R.id.drawerLayout)
        gifImageButtonMenu.setOnClickListener { onMenuClicked() }
        imageButtonAccount = findViewById(R.id.imageButtonAccount)
        imageButtonAccount.setOnClickListener { onAccountClicked() }
        recyclerViewDocuments = findViewById(R.id.recyclerViewDocuments)
        documentsAdapter = RecyclerViewDocFragmentsAdapter(this, arrayListDocuments)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDocuments.layoutManager = linearLayoutManager
        recyclerViewDocuments.adapter = documentsAdapter
        imageButtonDropListDocuments = findViewById(R.id.imageButtonDropListDocuments)
        imageButtonDropListDocuments.setOnClickListener { dropUpDownListDocuments() }

        supportFragmentManager.findFragmentById(R.id.frameLayoutFragment)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayoutFragment)
        if (currentFragment == null) {
            val fragment = DiaryFragment.newInstance("param1")
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayoutFragment, fragment)
                .commit()
        }
        initAnimations()
    }

    /** * Initializing animations. */
    private fun initAnimations() {
        menuAnimController = AnimationController(this, R.drawable.icon_menu)
        menuAnimController.setGifPositions(
            arrayOf(
                0,
                GifDrawable(this.resources, R.drawable.icon_menu).duration / 2
            )
        )
        gifImageButtonMenu.setImageDrawable(menuAnimController.getGifDrawable())
    }

    private fun onMenuClicked() {
        val animPosition = menuAnimController.startAnimation()
        if (animPosition < 0) return
        if (isMenuOpened) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        isMenuOpened = !isMenuOpened
    }

    private fun onAccountClicked() {
        val intent = Intent(this, AccountActivity::class.java)
        startActivity(intent)
    }

    private fun dropUpDownListDocuments() {
        recyclerViewDocuments.apply {
            if (visibility == View.VISIBLE) {
                visibility = View.GONE
                imageButtonDropListDocuments.setImageResource(R.drawable.icon_doc_list_arrow_down)
                return
            }
            visibility = View.VISIBLE
            imageButtonDropListDocuments.setImageResource(R.drawable.icon_doc_list_arrow_up)
        }
    }

    override fun onFragmentAdd() {
        documentsAdapter.clearIdSelectedDocument()
        val fragment = DiaryFragment.newInstance("param1")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutFragment, fragment)
            .commit()
    }

    override fun onFragmentSelected(idSelectedDocument: Int) {
        if (idSelectedDocument >= arrayListDocuments.size) {
            val fragment = DiaryFragment.newInstance("param1")
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayoutFragment, fragment)
                .commit()
            return
        }
        val fragment = MedicalCardFragment.newInstance("param1", "param2")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutFragment, fragment)
            .commit()
    }

    override fun onAddDocument() {
        documentsAdapter.addElement(documentsAdapter.itemCount)
        val fragment = MedicalCardFragment.newInstance("param1", "param2")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutFragment, fragment)
            .commit()
    }

    override fun onDocumentClosed() {
        //TODO("Not yet implemented")
    }

    override fun onDocumentSaved() {
        //TODO("Not yet implemented")
    }

    override fun onDocumentSelected(idSelectedNote: Int) {
        documentsAdapter.addElement(documentsAdapter.itemCount)
        val fragment = MedicalCardFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutFragment, fragment)
            .commit()
    }

    override fun onShowAttachedFilesOfDocument(idSelectedNote: Int) {
        //TODO("Not yet implemented")
    }

    override fun onSendToServerDocument(idSelectedNote: Int) {
        //TODO("Not yet implemented")
    }

    override fun onRemoveFromCurrentDocument(idSelectedNote: Int) {
        //TODO("Not yet implemented")
    }

    override fun onDeleteDocument(idSelectedNote: Int) {
        //TODO("Not yet implemented")
    }

    override fun onBackToCurrentDocument(idSelectedNote: Int) {
        //TODO("Not yet implemented")
    }
}