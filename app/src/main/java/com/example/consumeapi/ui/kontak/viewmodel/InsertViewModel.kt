package com.example.consumeapi.ui.kontak.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.consumeapi.model.Kontak
import com.example.consumeapi.repository.KontakRepository

class InsertViewModel (private val kontakRepository: KontakRepository) : viewModel() {

    var insertKontakState by mutableStateOf(InsertUiState())
        private set

    fun updateInsertKontakState(insertUiEvent: InsertUiEvent) {
        insertKontakState = InsertUiState(insertUiEvent = insertUiEvent)
    }

    suspend fun insertKontak() {
        viewModelScope.launch {
            try {
                kontakRepository.insertKontak(insertKontakState.insertUiEvent.toKontak())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertUiState(
    val insertUiState: InsertUiState = InsertUiEvent(),
)

data class InsertUiEvent(
    val id: Int = 0,
    val nama: String = "",
    val email: String = "",
    val nohp: String = "",
)

fun InsertUiEvent.toKontak(): Kontak = Kontak(
    id = Id,
    nama = nama,
    email = email,
    nohp = nohp,
)

fun Kontak.toUiStateKontak(): InsertUiState = InsertUiState(
    insertUiEvent = toInsertUiEvent(),
)

fun Kontak.toInsertUiEvent(): InsertUiEvent = InsertUiEvent(
    id = Id,
    nama = nama,
    email = email,
    nohp = nohp,
)