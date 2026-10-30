package com.example.cardindexproject.settings

import android.content.Context
import android.content.SharedPreferences

class Settings {
    private var context: Context
    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    var colorsSettings: ColorsSettings

    constructor(context: Context) {
        this.context = context
        this.sharedPreferences = context.getSharedPreferences(NAME_FILE, Context.MODE_PRIVATE)
        this.editor = sharedPreferences.edit()
        this.colorsSettings = ColorsSettings(sharedPreferences, editor)
    }

    companion object {
        private const val NAME_FILE = "AppSettings"
    }
}