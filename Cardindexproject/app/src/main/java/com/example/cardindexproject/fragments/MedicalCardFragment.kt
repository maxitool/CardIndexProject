package com.example.cardindexproject.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.cardindexproject.R
import com.example.cardindexproject.collections.DropViewArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicalCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicalCardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var callbacks: Callbacks? = null
    private var inputMethodManager: InputMethodManager? = null

    // Все view
    private lateinit var arrayListDropEdit: DropViewArrayList
    private lateinit var imageButtonCloseDocument: ImageButton
    private lateinit var imageButtonSaveDocument: ImageButton

    interface Callbacks{
        fun onDocumentClosed()
        fun onDocumentSaved()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medical_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayListDropEdit = DropViewArrayList(view)
        arrayListDropEdit.add(R.id.linearLayoutComplaints, R.id.imageViewComplaints, R.id.editTextComplaints)
        arrayListDropEdit.add(R.id.linearLayoutAnamnesis, R.id.imageViewAnamnesis, R.id.editTextAnamnesis)
        arrayListDropEdit.add(R.id.linearLayoutObjectively, R.id.imageViewObjectively, R.id.editTextObjectively)
        arrayListDropEdit.add(R.id.linearLayoutDiagnosis, R.id.imageViewDiagnosis, R.id.editTextDiagnosis)
        arrayListDropEdit.add(R.id.linearLayoutTreatment, R.id.imageViewTreatment, R.id.editTextTreatment)
        arrayListDropEdit.add(R.id.linearLayoutFiles, R.id.imageViewFiles, R.id.imageButtonFiles)
        arrayListDropEdit.deleteViewReference()
        setDropViewsState()
        for (i in 0 until arrayListDropEdit.size)
            arrayListDropEdit[i].linearLayout.setOnClickListener { onDropViewAction(it as LinearLayout?) }
        imageButtonCloseDocument = view.findViewById(R.id.imageButtonCloseDocument)
        imageButtonCloseDocument.setOnClickListener { onDocumentClosed() }
        imageButtonSaveDocument = view.findViewById(R.id.imageButtonSaveDocument)
        imageButtonSaveDocument.setOnClickListener { onDocumentSaved() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        inputMethodManager = null
    }

    private fun onDropViewAction(linearLayout: LinearLayout?) {
        linearLayout?: return
        val index = arrayListDropEdit.indexOfFirst { it.linearLayout == linearLayout }
        if (index < 0) return
        arrayListDropEdit[index].run {
            if (isDropped) {
                imageView.setImageResource(R.drawable.icon_down_arrow)
                dropView.apply {
                    visibility = View.GONE
                }
                inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
            }
            else {
                imageView.setImageResource(R.drawable.icon_remove)
                dropView.apply {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.drop_down))
                    visibility = View.VISIBLE
                }
            }
            isDropped = !isDropped
        }
    }

    private fun setDropViewsState() {
        for (i in 0 until arrayListDropEdit.size)
            arrayListDropEdit[i].dropView.apply {
                visibility = View.GONE
            }
    }

    private fun onDocumentClosed() {
        callbacks?.onDocumentClosed()
    }

    private fun onDocumentSaved() {
        callbacks?: return
        callbacks!!.onDocumentSaved()
        imageButtonSaveDocument.setImageResource(R.drawable.icon_save_green)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedicalCardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MedicalCardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}