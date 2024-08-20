package com.example.cardindexproject.settings

import android.content.SharedPreferences
import com.example.cardindexproject.R

class ColorsSettings {
    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    var typeTheme: String?
        private set

    constructor(sharedPreferences: SharedPreferences, editor: SharedPreferences.Editor) {
        this.sharedPreferences = sharedPreferences
        this.editor = editor
        this.typeTheme = sharedPreferences.getString(keyTheme, colorTheme1)
    }

    fun setNewThemeType(typeTheme: String) {
        when(typeTheme) {
            colorTheme1 ->
                editor.putString(keyTheme, colorTheme1).apply()
            // здесь нужно добавить новые типы тем, если были добавлены типы тем
        }
    }

    fun getTheme() : Int {
        when (typeTheme) {
            colorTheme1 -> return R.style.Theme_MainTheme
            // здесь нужно добавить новые темы, если были добавлены типы тем
            else -> return R.style.Theme_MainTheme
        }
    }

    fun getGradientBackground(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.drawable.gradient_main_background
            // здесь нужно добавить новые градиенты / цвета, если были добавлены типы тем
            else -> return R.drawable.gradient_main_background
        }
    }

    fun getGradientHeader(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.drawable.gradient_main_header
            // здесь нужно добавить новые градиенты / цвета, если были добавлены типы тем
            else -> return R.drawable.gradient_main_background
        }
    }

    fun getGradientAlternative(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.drawable.gradient_main_alternative
            // здесь нужно добавить новые градиенты / цвета, если были добавлены типы тем
            else -> return R.drawable.gradient_main_background
        }
    }

    fun getColorImportant(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.color.main_important
            // здесь нужно добавить новые цвета, если были добавлены типы тем
            else -> return R.color.main_important
        }
    }

    fun getColorAlternative1(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.color.main_alternative_1
            // здесь нужно добавить новые цвета, если были добавлены типы тем
            else -> return R.color.main_alternative_1
        }
    }

    fun getColorAlternative2(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.color.main_alternative_2
            // здесь нужно добавить новые цвета, если были добавлены типы тем
            else -> return R.color.main_alternative_2
        }
    }

    fun getImportantButtonShape(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.drawable.shape_main_important_button
            // здесь нужно добавить новые селекторы (selector), если были добавлены типы тем
            else -> return R.drawable.shape_main_important_button
        }
    }

    fun getAlternativeMenuShape(): Int {
        when (typeTheme) {
            colorTheme1 -> return R.drawable.shape_main_alternative_menu
            // здесь нужно добавить новые селекторы (selector), если были добавлены типы тем
            else -> return R.drawable.shape_main_alternative_menu
        }
    }

    companion object {
        private const val keyTheme = "theme"
        // ниже типы тем, вы можете добавлять новые темы
        private const val colorTheme1 = "main"
    }
}