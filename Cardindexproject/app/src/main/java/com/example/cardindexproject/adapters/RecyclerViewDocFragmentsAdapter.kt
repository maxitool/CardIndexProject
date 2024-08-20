package com.example.cardindexproject.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.cardindexproject.R

/** The adapter of the RecyclerView class with documents fragments, which is located on top of the DiaryActivity.
 * This adapter is used to add and select a fragment of a document or diary.
 * @param callbacks The context of the class that contains the implementation of the interface functions of this adapter. */
class RecyclerViewDocFragmentsAdapter(private val callbacks: Callbacks) :
    RecyclerView.Adapter<RecyclerViewDocFragmentsAdapter.ViewHolder>() {
    private var arrayListDocuments: ArrayList<Int> = arrayListOf(ID_ADD_BUTTON)
    private var idSelectedDocument: Int = 0

    interface Callbacks {
        /** Fun actions when adding document fragment. */
        fun onFragmentAdd()
        /** Fun actions when selecting document fragment. */
        fun onFragmentSelected(idSelectedDocument: Int)
    }
    /** The adapter of the RecyclerView document class, which is located on top of the DiaryActivity.
     * @param callbacks The context of the class that contains the implementation of the interface functions of this fragment.
     * @param arrayListDocuments ArrayList of documents with documents ids from database. */
    constructor(callbacks: Callbacks, arrayListDocuments: ArrayList<Int>) : this(callbacks) {
        this.arrayListDocuments = arrayListDocuments
        this.arrayListDocuments.add(ID_ADD_BUTTON)
    }
    /** The adapter of the RecyclerView document class, which is located on top of the DiaryActivity.
     * @param callbacks The context of the class that contains the implementation of the interface functions of this fragment.
     * @param idSelectedDocument Id of selected fragment (document or diary).
     * @param arrayListDocuments ArrayList of ids documents from database. */
    constructor(callbacks: Callbacks, idSelectedDocument: Int, arrayListDocuments: ArrayList<Int>) : this(callbacks) {
        this.arrayListDocuments = arrayListDocuments
        this.arrayListDocuments.add(ID_ADD_BUTTON)
        if (idSelectedDocument >= arrayListDocuments.size) {
            this.idSelectedDocument = 0
            return
        }
        this.idSelectedDocument = idSelectedDocument
    }
    /** Add document to ArrayList.
     * @param id Id of documents from database. */
    fun addElement(id: Int) {
        arrayListDocuments.add(arrayListDocuments.size - 1, id)
        idSelectedDocument = arrayListDocuments.size - 2
        notifyDataSetChanged()
    }
    /** Remove document from ArrayList.
     * @param idElement Id of fragment which need to delete.
     * @param idSelectedElement ID of fragment which need to select after delete */
    fun removeElement(idElement: Int, idSelectedElement: Int): Boolean {
        this.idSelectedDocument = idSelectedElement % (arrayListDocuments.size - 1)
        val isElDeleted = removeElement(idElement)
        return isElDeleted
    }
    /** Remove document from ArrayList.
     * @param idElement Id of fragment which need to delete. */
    fun removeElement(idElement: Int): Boolean {
        if (idElement >= arrayListDocuments.size - 1)
            return false
        val isElDeleted = arrayListDocuments.remove(idElement)
        if (isElDeleted)
            notifyDataSetChanged()
        return isElDeleted
    }
    /** Select ADD BUTTON (do not select any document fragment). */
    fun clearIdSelectedDocument() { setIdSelectedDocument(arrayListDocuments.size - 1) }
    /** Select document fragment.
     * @param idElement Id of document fragment. */
    private fun setIdSelectedDocument(idElement: Int) {
        idSelectedDocument = idElement
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewDocFragmentsAdapter.ViewHolder {
        when(viewType) {
            TYPEVIEW_BUTTON_DOCUMENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.button_document, parent, false)
                return ViewHolder(view)
            }
            TYPEVIEW_BUTTON_ADD_DOCUMENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.button_add_document, parent, false)
                return ViewHolder(view)
            }
            else -> throw Exception("No viewType for button")
        }
    }
    override fun getItemViewType(position: Int): Int {
        if (position == arrayListDocuments.size - 1)
            return TYPEVIEW_BUTTON_ADD_DOCUMENT
        return TYPEVIEW_BUTTON_DOCUMENT
    }
    override fun onBindViewHolder(holder: RecyclerViewDocFragmentsAdapter.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPEVIEW_BUTTON_DOCUMENT -> holder.bindDocumentButton(position, callbacks)
            TYPEVIEW_BUTTON_ADD_DOCUMENT -> holder.bindAddDocumentButton(position, callbacks)
            else -> throw Exception("No viewType for button")
        }
    }
    override fun getItemCount(): Int = arrayListDocuments.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonDocument: AppCompatButton

        init {
            // Define click listener for the ViewHolder's View
            buttonDocument= view.findViewById(R.id.buttonDocument)
        }
        /** Bind RecyclerView document button element.
         * @param position id of document fragment.
         * @param callbacks Callbacks of this fragment. */
        fun bindDocumentButton(position: Int, callbacks: Callbacks) {
            buttonDocument.text = (position + 1).toString()
            if (position == idSelectedDocument) {
                buttonDocument.setBackgroundResource(R.drawable.shape_main_document_important_button)
                buttonDocument.setTextColor(Color.WHITE)
            }
            else {
                buttonDocument.setBackgroundResource(R.drawable.shape_main_document_alternative_button)
                buttonDocument.setTextColor(Color.BLACK)
            }
            buttonDocument.setOnClickListener {
                setIdSelectedDocument(buttonDocument.text.toString().toInt() - 1)
                callbacks.onFragmentSelected(idSelectedDocument)
            }
        }
        /** Bind RecyclerView add document button element.
         * @param position id of diary fragment.
         * @param callbacks Callbacks of this fragment. */
        fun bindAddDocumentButton(position: Int, callbacks: Callbacks) {
            buttonDocument.setOnClickListener {
                callbacks.onFragmentAdd()
            }
        }
    }

    companion object {
        private const val ID_ADD_BUTTON = -1
        private const val TYPEVIEW_BUTTON_DOCUMENT = 1
        private const val TYPEVIEW_BUTTON_ADD_DOCUMENT = 2
    }
}