package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import com.giraffe.quranpage.ui.theme.fontFamilies
import com.giraffe.quranpage.ui.theme.kingFahd001
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
            repository.getContentOfPage().let { ayahs ->
                _state.update { state ->
                    state.copy(
                        pages = ayahs.groupBy { it.pageIndex }.toList().map {
                            PageUI(
                                pageIndex = it.first,
                                verses = it.second,
                                text = convertVerseToText(it.second,if (it.first <= 10) fontFamilies[it.first - 1] else kingFahd001)
                            )
                        }
                    )
                }
            }
        }
    }

    private fun convertVerseToText(
        verses: List<VerseModel>,
        fontFamily: FontFamily
    ): AnnotatedString {
        return buildAnnotatedString {
            verses.forEach { verse ->
                pushStringAnnotation(tag = verse.qcfData, annotation = verse.qcfData)
                withStyle(style = SpanStyle(fontFamily = fontFamily)) {
                    append(verse.qcfData)
                }
                pop()
            }
        }
    }

    override fun onVerseSelected(verse: VerseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(selectedVerse = verse) }
        }
    }
}