package com.creyc.test.data

import com.google.gson.annotations.SerializedName

data class Parametrs(
    @SerializedName("phone_identifier") var id: String,
    @SerializedName("locale") var locale: String
)