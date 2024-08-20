package com.example.cardindexproject.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.example.cardindexproject.R
import com.example.cardindexproject.adapters.RecyclerViewDocumentsAdapter
import com.example.cardindexproject.data.DocumentShortData
import com.example.cardindexproject.decorations.DocumentsItemDecoration
import com.example.cardindexproject.enums.TypesOfDisplayNotes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val ARG_PARAM1 = "ARG_PARAM1"

class DiaryFragment : Fragment() {
    private var param1: String? = null

    private var callbacks: Callbacks? = null
    private var documentsCallbacks: RecyclerViewDocumentsAdapter.Callbacks? = null

    private var isSearchMenuVisible: Boolean = false

    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var imageButtonAddDocument: ImageButton

    private lateinit var arrayIdButtons: Array<AppCompatButton>
    private lateinit var buttonCurrent: AppCompatButton
    private lateinit var buttonHistory: AppCompatButton
    private lateinit var buttonServer: AppCompatButton
    private lateinit var imageButtonShowSearchMenu: ImageButton
    private lateinit var imageButtonAllSendToServer: ImageButton
    private lateinit var linearLayoutSearchMenu: LinearLayout
    private lateinit var imageViewButtonSearch: ImageButton

    val firstApiFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val date = LocalDate.parse("03-08-2024" , firstApiFormat)
    val arrayListNotes: ArrayList<DocumentShortData> = arrayListOf(
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", null),
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", true),
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", false),
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", false),
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", true),
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", null),
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", null),
        DocumentShortData(date, "Шариков Полиграф Полиграфович", date, "Пульпит 27", "Кузнецов Иван Михайлович", null))

    private lateinit var adapter: RecyclerViewDocumentsAdapter

    interface Callbacks {
        fun onAddDocument()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewNotes = view.findViewById(R.id.recyclerViewNotes)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerViewNotes.layoutManager = linearLayoutManager
        adapter = RecyclerViewDocumentsAdapter(requireContext(),
            documentsCallbacks!!, arrayListNotes, TypesOfDisplayNotes.TYPE_CURRENT)
        recyclerViewNotes.adapter = adapter
        val documentsItemDecoration = DocumentsItemDecoration(requireContext())
        recyclerViewNotes.addItemDecoration(documentsItemDecoration)

        buttonCurrent = view.findViewById(R.id.buttonCurrent)
        buttonHistory = view.findViewById(R.id.buttonHistory)
        buttonServer = view.findViewById(R.id.buttonServer)
        arrayIdButtons = arrayOf(buttonCurrent, buttonHistory, buttonServer)
        buttonCurrent.setOnClickListener { changeState(it as AppCompatButton) }
        buttonHistory.setOnClickListener { changeState(it as AppCompatButton) }
        buttonServer.setOnClickListener { changeState(it as AppCompatButton) }
        imageButtonAddDocument = view.findViewById(R.id.imageButtonAddDocument)
        imageButtonAddDocument.setOnClickListener { onAddDocument() }
        imageButtonShowSearchMenu = view.findViewById(R.id.imageButtonShowSearchMenu)
        imageButtonShowSearchMenu.setOnClickListener { onChangeSearchMenuState() }
        imageButtonAllSendToServer = view.findViewById(R.id.imageButtonAllSendToServer)
        linearLayoutSearchMenu = view.findViewById(R.id.linearLayoutSearchMenu)
        imageViewButtonSearch = view.findViewById(R.id.imageViewButtonSearch)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        documentsCallbacks = context as RecyclerViewDocumentsAdapter.Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        documentsCallbacks = null
    }

    private fun onAddDocument() { callbacks!!.onAddDocument() }

    private fun changeState(button: AppCompatButton) {
        val indexSelectedButton = arrayIdButtons.indexOf(button)
        if (indexSelectedButton == -1)
            return
        var typeOfDisplayDoc: TypesOfDisplayNotes = TypesOfDisplayNotes.TYPE_CURRENT
        when(indexSelectedButton) {
            0 -> {
                typeOfDisplayDoc = TypesOfDisplayNotes.TYPE_CURRENT
                imageButtonAllSendToServer.visibility = View.GONE
            }
            1 -> {
                typeOfDisplayDoc = TypesOfDisplayNotes.TYPE_HISTORY
                imageButtonAllSendToServer.visibility = View.VISIBLE
            }
            2 -> {
                typeOfDisplayDoc = TypesOfDisplayNotes.TYPE_SERVER
                imageButtonAllSendToServer.visibility = View.GONE
            }
        }
        adapter.changeDocumentsState(arrayListNotes, typeOfDisplayDoc)
        for (i in arrayIdButtons.indices) {
            if (i == indexSelectedButton) {
                arrayIdButtons[i].setTextColor(Color.WHITE)
                arrayIdButtons[i].setBackgroundResource(R.drawable.shape_main_important_button)
                continue
            }
            arrayIdButtons[i].setTextColor(Color.BLACK)
            arrayIdButtons[i].setBackgroundResource(R.drawable.shape_alternative_button)
        }
    }

    private fun onChangeSearchMenuState() {
        var idBack = 0; var idImage = 0
        if (isSearchMenuVisible) {
            idBack = R.drawable.shape_main_document_alternative_button
            idImage = R.drawable.icon_search_drop_down
            linearLayoutSearchMenu.visibility = View.GONE
        }
        else {
            idBack = R.drawable.shape_main_document_important_button
            idImage = R.drawable.icon_search_drop_up
            linearLayoutSearchMenu.visibility = View.VISIBLE
        }
        imageButtonShowSearchMenu.apply {
            setBackgroundResource(idBack)
            setImageResource(idImage)
        }
        isSearchMenuVisible = !isSearchMenuVisible
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            DiaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}