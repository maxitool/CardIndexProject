package com.example.cardindexproject.collections

import android.view.View
import com.example.cardindexproject.data.DropViewData

class DropViewArrayList(var view: View?) : ArrayList<DropViewData>() {
    fun add(isDropped: Boolean, idLinearLayout: Int, idImageView: Int, idDropView: Int): Boolean {
        view?: return false
        return super.add(DropViewData(
            isDropped,
            view!!.findViewById(idLinearLayout),
            view!!.findViewById(idImageView),
            view!!.findViewById(idDropView)))
    }
    fun add(idLinearLayout: Int, idImageView: Int, idDropView: Int): Boolean {
        return add(false, idLinearLayout, idImageView, idDropView)
    }
    fun deleteViewReference() { view = null }
}