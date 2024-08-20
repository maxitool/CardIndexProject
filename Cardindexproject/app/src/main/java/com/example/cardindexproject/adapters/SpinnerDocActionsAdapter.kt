package com.example.cardindexproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.cardindexproject.R
import com.example.cardindexproject.data.DocActionDisplayOptions

class SpinnerDocActionsAdapter(context: Context, layoutResource: Int, private val arrayDocActions: Array<DocActionDisplayOptions>) :
    ArrayAdapter<DocActionDisplayOptions>(context, layoutResource, arrayDocActions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_spinner_doc, parent, false)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.item_spinner_dropdown_doc, parent, false) as View
        val textView = view.findViewById<TextView>(R.id.textViewNoteAction)
        val imageView = view.findViewById<ImageView>(R.id.imageViewNoteAction)
        val item = getItem(position)
        item?.let {
            textView.setText(it.idTextResource)
            textView.setTextColor(ContextCompat.getColor(context, it.idTextColorResource))
            imageView.setImageResource(it.idImageResource)
        }
        return view
    }

    override fun getCount(): Int = arrayDocActions.size - 1
}