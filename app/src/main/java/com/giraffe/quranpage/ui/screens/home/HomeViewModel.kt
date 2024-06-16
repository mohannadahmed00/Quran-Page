package com.giraffe.quranpage.ui.screens.home

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        initStates()
    }

    private fun initStates() = viewModelScope.launch(Dispatchers.IO) {
        repository.getPagesOfSurah(2) { pages ->
            _state.update {
                it.copy(pages = pages)
            }
        }
    }

    fun selectAyah(ayahIndex: Int, polygon: List<Offset>, pageIndex: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    selectedAyahIndex = ayahIndex,
                    selectedAyahPolygon = polygon,
                    selectedPageIndex = pageIndex
                )
            }
        }
}