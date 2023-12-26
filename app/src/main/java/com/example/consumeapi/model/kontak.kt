package com.example.consumeapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Kontak(
    val Id: Int,
    @SerialName(value = "nama")
    val nama: String,
    val alamat: String,
    val telpon: String
)