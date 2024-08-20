package com.example.cardindexproject.data

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

data class DropViewData(var isDropped: Boolean = false, val linearLayout: LinearLayout, val imageView: ImageView, val dropView: View)
