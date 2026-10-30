package com.example.cardindexproject.viewmodels

import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {
    var arrayEyesStates: Array<Int> = arrayOf(1, 1)
    private val mapInstitutionType: Map<Int, String> =
        mapOf(0 to "Клиника", 1 to "Поликлиника", 2 to "ВЧ (организация)")
    var idInstitutionType: Int = 0
    var loginText: String = ""
    var passwordText: String = ""
    var confirmPasswordText: String = ""
    var fioText: String = ""
    var phoneText: String = ""
    var institutionNameText: String = ""
    var institutionINNText: String = ""

    /** * Get institution type.
     * @param id Id of institution
     * @return Institution type */
    fun getInstitutionType(id: Int): String {
        return mapInstitutionType[id % mapInstitutionType.size]!!
    }

    /** * Get id of institution type.
     * @param typeInstitution String institution type
     * @return Id of institution type */
    fun getInstitutionId(typeInstitution: String): Int? {
        return mapInstitutionType.filterValues { it == typeInstitution }.keys.first()
    }
}