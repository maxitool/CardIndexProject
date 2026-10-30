package com.example.cardindexproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cardindexproject.R
import com.example.cardindexproject.data.DocActionDisplayOptions
import com.example.cardindexproject.data.DocumentShortData
import com.example.cardindexproject.enums.TypesOfDisplayNotes

/** The adapter of the RecyclerView class with documents.
 * This adapter is used to show all documents in database.
 * @param context Context,
 * @param callbacks The context of the class that contains the implementation of the interface functions of this adapter.
 * @param arrayListDocs ArrayList of documents short data.
 * @param typeDisplayDocs Type of display documents (TYPE_CURRENT or TYPE_HISTORY or TYPE_SERVER). */
class RecyclerViewDocumentsAdapter(
    private val context: Context,
    private val callbacks: Callbacks,
    private var arrayListDocs: ArrayList<DocumentShortData>,
    private var typeDisplayDocs: TypesOfDisplayNotes
) :
    RecyclerView.Adapter<RecyclerViewDocumentsAdapter.ViewHolder>() {

    interface Callbacks {
        /** Fun actions when selecting document.
         * @param idSelectedDoc Id of document. */
        fun onDocumentSelected(idSelectedDoc: Int)

        /** Fun actions when pressed to ShowAttachedFiles document action.
         * @param idSelectedDoc Id of document. */
        fun onShowAttachedFilesOfDocument(idSelectedDoc: Int)

        /** Fun actions when pressed to SendToServer document action.
         * @param idSelectedDoc Id of document. */
        fun onSendToServerDocument(idSelectedDoc: Int)

        /** Fun actions when pressed to RemoveFromCurrent document action.
         * @param idSelectedDoc Id of document. */
        fun onRemoveFromCurrentDocument(idSelectedDoc: Int)

        /** Fun actions when pressed to Delete document action.
         * @param idSelectedDoc Id of document. */
        fun onDeleteDocument(idSelectedDoc: Int)

        /** Fun actions when pressed to BackToCurrent document action.
         *
         * BackToCurrent action return document from history to current (if document didn't send to server).
         * @param idSelectedDoc Id of document. */
        fun onBackToCurrentDocument(idSelectedDoc: Int)
    }

    /** Change of documents state
     * @param arrayListDocs ArrayList of documents short data.
     * @param typeDisplayDocs Type of display documents (TYPE_CURRENT or TYPE_HISTORY or TYPE_SERVER). */
    fun changeDocumentsState(
        arrayListDocs: ArrayList<DocumentShortData>,
        typeDisplayDocs: TypesOfDisplayNotes
    ) {
        this.typeDisplayDocs = typeDisplayDocs
        this.arrayListDocs = arrayListDocs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewDocumentsAdapter.ViewHolder {
        val view = when (viewType) {
            TypesOfDisplayNotes.TYPE_CURRENT.intType ->
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recyclerview_doc_type_current, parent, false) as View

            TypesOfDisplayNotes.TYPE_HISTORY.intType ->
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recyclerview_doc_type_history, parent, false) as View

            TypesOfDisplayNotes.TYPE_SERVER.intType ->
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recyclerview_doc_type_server, parent, false) as View

            else -> throw Exception("Unknown viewType for typeDisplayNotes field")
        }
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val intTypeDisplayNotes = when (typeDisplayDocs) {
            TypesOfDisplayNotes.TYPE_CURRENT -> TypesOfDisplayNotes.TYPE_CURRENT.intType
            TypesOfDisplayNotes.TYPE_HISTORY -> TypesOfDisplayNotes.TYPE_HISTORY.intType
            TypesOfDisplayNotes.TYPE_SERVER -> TypesOfDisplayNotes.TYPE_SERVER.intType
        }
        return intTypeDisplayNotes
    }

    override fun onBindViewHolder(holder: RecyclerViewDocumentsAdapter.ViewHolder, position: Int) {
        holder.bindDocument(position)
    }

    override fun getItemCount(): Int = arrayListDocs.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val linearLayoutDocument: LinearLayout =
            view.findViewById(R.id.linearLayoutDocument)
        private val textViewFullName: TextView = view.findViewById(R.id.textViewFullName)
        private val textViewBirthDate: TextView = view.findViewById(R.id.textViewBirthDate)
        private val textViewShortProblem: TextView = view.findViewById(R.id.textViewShortProblem)
        private val spinnerNoteActions: Spinner = view.findViewById(R.id.spinnerNoteActions)
        private val textViewSentServerState: TextView? =
            view.findViewById(R.id.textViewSentServerState)
        private val textDateTreatment: TextView? = view.findViewById(R.id.textDateTreatment)
        private val textViewAttendingDoctor: TextView? =
            view.findViewById(R.id.textViewAttendingDoctor)

        /** Bind RecyclerView document data element.
         * @param position id of document. */
        fun bindDocument(position: Int) {
            textViewFullName.text = arrayListDocs[position].fullName
            textViewBirthDate.text = arrayListDocs[position].dateBirth.toString()
            textViewShortProblem.text = arrayListDocs[position].shortProblem
            textViewSentServerState?.let {
                arrayListDocs[position].wasSentToServer
                    ?: run { arrayListDocs[position].wasSentToServer = false }
                if (arrayListDocs[position].wasSentToServer!!) {
                    it.text = HtmlCompat.fromHtml(
                        "<font color=${context.getColor(R.color.dark_blue)}>${context.getString(R.string.sent_ru)}",
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    return@let
                }
                it.text = HtmlCompat.fromHtml(
                    "<font color=${context.getColor(R.color.dark_red)}>${context.getString(R.string.not_sent_ru)}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            textDateTreatment?.let {
                if (arrayListDocs[position].dateTreatment == null) {
                    it.text = "???"
                    return@let
                }
                it.text = arrayListDocs[position].dateTreatment.toString()
            }
            textViewAttendingDoctor?.let {
                if (arrayListDocs[position].attendingDoctor == null) {
                    it.text = "???"
                    return@let
                }
                it.text = arrayListDocs[position].attendingDoctor
            }
            linearLayoutDocument.setOnClickListener { callbacks.onDocumentSelected(position) }
            val adapter = getSpinnerAdapter(arrayListDocs[position].wasSentToServer)
            adapter!!.setDropDownViewResource(ID_SPINNER_DROPDOWN_ITEM)
            spinnerNoteActions.adapter = adapter
            val parentPosition = position
            spinnerNoteActions.setSelection(spinnerNoteActions.count)
            spinnerNoteActions.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        if (position >= spinnerNoteActions.count) {
                            return
                        }
                        val actionsFunctions =
                            getSpinnerFunctions(arrayListDocs[parentPosition].wasSentToServer)
                        actionsFunctions[position](parentPosition)
                        spinnerNoteActions.setSelection(spinnerNoteActions.count)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // do nothing
                    }

                }
        }

        /** Get spinner adapter with document actions.
         * @param wasSentToServer Was document sent to server?
         * @return Spinner adapter. */
        private fun getSpinnerAdapter(wasSentToServer: Boolean?): SpinnerDocActionsAdapter {

            val arrayDisplayOptions = when (typeDisplayDocs) {
                TypesOfDisplayNotes.TYPE_CURRENT -> arrayDisplayOptionsC

                TypesOfDisplayNotes.TYPE_HISTORY -> {
                    if (wasSentToServer!!) {
                        arrayDisplayOptionsHY
                    } else {
                        arrayDisplayOptionsHN
                    }
                }

                TypesOfDisplayNotes.TYPE_SERVER ->
                    arrayDisplayOptionsS
            }
            val adapter =
                SpinnerDocActionsAdapter(context, ID_SPINNER_ITEM, arrayDisplayOptions + noneAction)
            return adapter
        }

        /** Get spinner action functions.
         * @param wasSentToServer Was document sent to server?
         * @return Array with action functions. */
        private fun getSpinnerFunctions(wasSentToServer: Boolean?): Array<(Int) -> Unit> {
            val arrayFunctions = when (typeDisplayDocs) {
                TypesOfDisplayNotes.TYPE_CURRENT -> arrayFunctionsC(callbacks)
                TypesOfDisplayNotes.TYPE_HISTORY -> {
                    if (wasSentToServer!!) {
                        arrayFunctionsHY(callbacks)
                    } else {
                        arrayFunctionsHN(callbacks)
                    }
                }

                TypesOfDisplayNotes.TYPE_SERVER -> arrayFunctionsS(callbacks)
            }
            return arrayFunctions
        }
    }

    companion object {
        @LayoutRes
        private val ID_SPINNER_ITEM = R.layout.item_spinner_doc

        @LayoutRes
        private val ID_SPINNER_DROPDOWN_ITEM = R.layout.item_spinner_dropdown_doc

        /** Array of display options for  CURRENT  display type. */
        private val arrayDisplayOptionsC: Array<DocActionDisplayOptions> = arrayOf(
            DocActionDisplayOptions(
                R.string.attached_files_ru,
                R.color.black,
                R.drawable.icon_attach_file
            ),
            DocActionDisplayOptions(
                R.string.remove_from_current_ru,
                R.color.dark_red,
                R.drawable.icon_exit
            )
        )

        /** Array of action functions for  CURRENT  display type. */
        private fun arrayFunctionsC(callbacks: Callbacks): Array<(Int) -> Unit> {
            return arrayOf(
                callbacks::onShowAttachedFilesOfDocument,
                callbacks::onRemoveFromCurrentDocument
            )
        }

        /** Array of display options for  HISTORY  display type, which  DID SEND  to server. */
        private val arrayDisplayOptionsHY: Array<DocActionDisplayOptions> = arrayOf(
            DocActionDisplayOptions(
                R.string.attached_files_ru,
                R.color.black,
                R.drawable.icon_attach_file
            ),
            DocActionDisplayOptions(R.string.delete_ru, R.color.dark_red, R.drawable.icon_delete)
        )

        /** Array of action functions for  HISTORY  display type, which  DID SEND  to server. */
        private fun arrayFunctionsHY(callbacks: Callbacks): Array<(Int) -> Unit> {
            return arrayOf(
                callbacks::onShowAttachedFilesOfDocument,
                callbacks::onDeleteDocument
            )
        }

        /** Array of display options for  HISTORY  display type, which  DIDN'T SEND  to server. */
        private val arrayDisplayOptionsHN: Array<DocActionDisplayOptions> = arrayOf(
            DocActionDisplayOptions(
                R.string.attached_files_ru,
                R.color.black,
                R.drawable.icon_attach_file
            ),
            DocActionDisplayOptions(
                R.string.icon_back_to_current_ru,
                R.color.black,
                R.drawable.icon_back_to_current
            ),
            DocActionDisplayOptions(
                R.string.send_to_server_ru,
                R.color.black,
                R.drawable.icon_send
            ),
            DocActionDisplayOptions(R.string.delete_ru, R.color.dark_red, R.drawable.icon_delete)
        )

        /** Array of action functions for  HISTORY  display type, which  DIDN'T SEND  to server. */
        private fun arrayFunctionsHN(callbacks: Callbacks): Array<(Int) -> Unit> {
            return arrayOf(
                callbacks::onShowAttachedFilesOfDocument,
                callbacks::onBackToCurrentDocument,
                callbacks::onSendToServerDocument,
                callbacks::onDeleteDocument
            )
        }

        /** Array of display options for  SERVER  display type. */
        private val arrayDisplayOptionsS: Array<DocActionDisplayOptions> = arrayOf(
            DocActionDisplayOptions(
                R.string.attached_files_ru,
                R.color.black,
                R.drawable.icon_attach_file
            )
        )

        /** Array of action functions for  SERVER  display type. */
        private fun arrayFunctionsS(callbacks: Callbacks): Array<(Int) -> Unit> {
            return arrayOf(
                callbacks::onShowAttachedFilesOfDocument
            )
        }

        /** None action display options (empty). */
        private val noneAction =
            DocActionDisplayOptions(R.string.empty, R.color.black, R.drawable.icon_empty)
    }
}