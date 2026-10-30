package com.example.cardindexproject.data

import java.time.LocalDate

data class DocumentShortData(
    var dateTreatment: LocalDate?,
    var fullName: String,
    var dateBirth: LocalDate,
    var shortProblem: String,
    var attendingDoctor: String,
    var wasSentToServer: Boolean?
)