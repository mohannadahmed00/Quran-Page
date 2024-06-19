package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import com.giraffe.quranpage.ui.theme.kingFahd003
import com.giraffe.quranpage.ui.theme.kingFahd007
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
        getPageContent(7, kingFahd007)
    }


    private fun getPageContent(pageIndex: Int, fontFamily: FontFamily) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getContentOfPage(pageIndex).let {
                _state.update { state ->
                    state.copy(
                        fontFamily = fontFamily,
                        verses = it,
                        content = handleVerses(it)
                    )
                }
            }
        }
    }

    override fun handleVerses(verses: List<VerseModel>): String {
        val strBuilder = StringBuilder()
        val list = mutableListOf<String>()
        verses.forEachIndexed { index, verse ->
            val str = if (index == 0) {
                handleFirstVerse(verse.qcfData)
            } else {
                handleRestOfVerse(verse.qcfData)
            }
            list.add(str)
            strBuilder.append(str)
        }
        _state.update { it.copy(versesStr = list) }
        return strBuilder.toString()
    }

    companion object {
        private const val TAG = "QuranViewModel"
    }

    private fun handleFirstVerse(input: String) =
        input.replace(" ", "").substring(0, 1).plus("\u200f")
            .plus(input.replace(" ", "").substring(1))

    private fun handleRestOfVerse(input: String) = input.replace(" ", "")
}