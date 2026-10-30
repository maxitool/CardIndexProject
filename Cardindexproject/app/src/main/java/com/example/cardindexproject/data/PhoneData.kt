package com.example.cardindexproject.data

import android.content.Context
import com.example.cardindexproject.R

class PhoneData {
    var phone: String = ""
        private set(value) {
            this.phone = regex.replace(value, "+$1 $2 $3-$4-$5")
        }

    fun setPhoneNumber(value: String, countryCode: String): String {
        var indexPhoneCountryCode: Int? = null
        phoneCountryCodesMap[countryCode]?.forEachIndexed { index, element ->
            if (element == value.substring(0, element.length)) {
                indexPhoneCountryCode = index
                return@forEachIndexed
            }
        }
        indexPhoneCountryCode ?: return ErrorsCodes.BAD_COUNTRY_CODE
        var editedPhone = value
        if (indexPhoneCountryCode != 0) { // 0 - базовый тел. код страны
            editedPhone =
                phoneCountryCodesMap[countryCode]!![0] + editedPhone.substring(phoneCountryCodesMap[countryCode]!![indexPhoneCountryCode!!].length)
        }
        if (editedPhone.length != countPhoneDigits) {
            return ErrorsCodes.FEW_DIGITS
        }
        this.phone = editedPhone
        return ErrorsCodes.OK
    }

    companion object {
        private val regex = """(\d{2})(\d{3})(\d{3})(\d{2})(\d{2})""".toRegex()
        private val countPhoneDigits = 12
        private val phoneCountryCodesMap: Map<String, Array<String>> = mapOf(
            "ru" to arrayOf("+7", "7", "8")
        )

        class ErrorsCodes {
            companion object {
                val OK = "OK"
                val BAD_COUNTRY_CODE = "Bad country code"
                val FEW_DIGITS = "Few digits in phone number"

                fun interpretError(errorCode: String, context: Context) {
                    when (errorCode) {
                        this.BAD_COUNTRY_CODE -> context.resources.getString(R.string.bad_country_code_ru)
                        this.FEW_DIGITS -> context.resources.getString(R.string.few_digits_in_phone_number_ru)
                        else -> context.resources.getString(R.string.unknown_error_with_phone_number_ru)
                    }
                }
            }
        }
    }
}
