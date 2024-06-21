package com.giraffe.quranpage.ui.screens.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import com.giraffe.quranpage.ui.theme.fontFamilies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    QuranEvents {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()

    init {
        getAllVerses()
    }

    private fun getAllVerses() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getContentOfPage().let {
                _state.update { state ->
                    state.copy(
                        allVerses = it,
                    )
                }
            }
        }
    }

    override fun getPageContent(pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { state ->
                state.copy(
                    pageVerses = state.allVerses.filter { it.pageIndex == pageIndex },
                    pageIndex = pageIndex,
                    pageFont = fontFamilies[pageIndex - 1]
                )
            }
        }
    }

    override fun onVerseSelected(verse: VerseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(selectedVerse = verse) }
        }
    }
}