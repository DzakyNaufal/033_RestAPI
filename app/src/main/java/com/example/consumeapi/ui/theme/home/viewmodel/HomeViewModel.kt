package com.example.consumeapi.ui.theme.home.viewmodel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumeapi.model.Kontak
import com.example.consumeapi.navigation.DestinasiNavigasi
import com.example.consumeapi.repository.KontakRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class KontakUIState {
    data class Success(val kontak: List<Kontak>) : KontakUIState()
    object Error : KontakUIState()
    object Loading : KontakUIState()
}

class HomeViewModel(private val kontakRepository: KontakRepository) : ViewModel() {
    var kontakUIState: KontakUIState by mutableStateOf(KontakUIState.Loading)
        private set

    init {
        getKontak()
    }

    fun getKontak() {
        viewModelScope.launch {
            kontakUIState = KontakUIState.Loading
            kontakUIState = try {
                KontakUIState.Success(kontakRepository.getKontak())
            } catch (e: IOException) {
                KontakUIState.Error
            } catch (e: HttpException) {
                KontakUIState.Error
            }
        }
    }

    fun deleteKontak(id: Int) {
        viewModelScope.launch {
            try {
                kontakRepository.deleteKontak(id)
            } catch (e: IOException) {
                KontakUIState.Error
            } catch (e: HttpException) {
                KontakUIState.Error
            }
        }
    }
    @Composable
    fun KontakCard(
        kontak: Kontak,
        onDeleteClick: (Kontak) -> Unit = {},
        modifier: Modifier = Modifier
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = kontak.email,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { onDeleteClick(kontak) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                )
            }
        }
    }

    @Composable
    fun KontakLayout(
        kontak: List<Kontak>,
        modifier: Modifier = Modifier,
        onDetailClick: (Kontak) -> Unit,
        onDeleteClick: (Kontak) -> Unit = {}
    ) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(kontak) { kontak ->
                KontakCard(
                    kontak = kontak,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDetailClick(kontak) },
                    onDeleteClick = {
                        onDeleteClick(kontak)
                    }
                )
            }
        }
    }

    @Composable
    fun HomeStatus(
        kontakUIState: KontakUIState,
        retryAction: () -> Unit,
        modifier: Modifier = Modifier,
        onDeleteClick: (Kontak) -> Unit = {},
        onDetailClick: (Kontak) -> Unit
    ) {

        when (kontakUIState) {
            is KontakUIState.Loading -> Onloading(modifier = modifier.fillMaxWidth())
            is KontakUIState.Success -> KontakLayout(
                kontak = kontakUIState.kontak, modifier = modifier.fillMaxWidth(),
                onDetailClick = {
                    onDetailClick(it.id)
                },
                onDeleteClick = {
                    onDeleteClick(it)
                }
            )

            is KontakUIState.Error -> OnError(retryAction, modifier.fillMaxWidth())
        }
    }

    object DestinasiHome : DestinasiNavigasi {
        override val route = "home"
        override val titleRes = "Kontak"
    }

}