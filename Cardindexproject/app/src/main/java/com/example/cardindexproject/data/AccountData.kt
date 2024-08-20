package com.example.cardindexproject.data

class AccountData {
    var login: String = ""
    var password: String = ""
    var fullName: String = ""
    var institutionName: String = ""
    var institutionINN: String = ""
    var institutionType: Int = 0
    private var phoneClass: PhoneData = PhoneData()

    constructor()
    constructor(login: String, password: String) {
        this.login = login
        this.password = password
    }
    constructor(login: String, fullName: String, phone: String, institutionName: String, institutionINN: String, institutionType: Int) {
        this.login = login
        this.fullName = fullName
        this.institutionName = institutionName
        this.institutionINN = institutionINN
        this.institutionType = institutionType
        this.phoneClass.setPhoneNumber(phone, "ru")
    }
    constructor(login: String, password: String, fullName: String, phone: String, institutionName: String, institutionINN: String, institutionType: Int) {
        this.login = login
        this.password = password
        this.fullName = fullName
        this.institutionName = institutionName
        this.institutionINN = institutionINN
        this.institutionType = institutionType
        this.phoneClass.setPhoneNumber(phone, "ru")
    }
    fun setPhoneNumber(phone: String): String { return this.phoneClass.setPhoneNumber(phone, "ru") }
    fun getPhoneNumber(): String { return phoneClass.phone }

    companion object{
        private val mapInstitutionType: Map<Int, String> = mapOf(0 to "Клиника", 1 to "Поликлиника", 2 to "ВЧ (организация)")

        /** * Get institution type.
         * @param id Id of institution
         * @return Institution type */
        fun getInstitutionType(id: Int): String { return mapInstitutionType[id % mapInstitutionType.size]!! }
        /** * Get id of institution type.
         * @param typeInstitution String institution type
         * @return Id of institution type */
        fun getInstitutionId(typeInstitution: String): Int? { return mapInstitutionType.filterValues { it == typeInstitution }.keys.first() }
    }
}