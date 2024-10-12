package com.giraffe.quranpage.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.usecases.GetSurahesDataUseCase
import com.giraffe.quranpage.usecases.GetVersesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getVersesUseCase: GetVersesUseCase,
    private val getSurahesDataUseCase: GetSurahesDataUseCase,
) : ViewModel(), SearchEvents {
    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    init {
        getSurahesData()
        getAllVerses()
    }

    private fun getSurahesData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(surahesData = getSurahesDataUseCase()) }
        }
    }

    private fun getAllVerses() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(verses = getVersesUseCase()) }
        }
    }

    override fun onValueChange(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    value = value,
                    filteredVerses = if (value.isEmpty()) emptyList() else _state.value.verses.filter { verse ->
                        verse.normalContent.contains(
                            value
                        )
                    })
            }
        }
    }
}