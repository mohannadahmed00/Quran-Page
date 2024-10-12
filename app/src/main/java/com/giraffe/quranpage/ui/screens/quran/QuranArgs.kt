package com.giraffe.quranpage.ui.screens.quran

import androidx.lifecycle.SavedStateHandle
import com.giraffe.quranpage.local.model.VerseModel


class QuranArgs(private val savedStateHandle: SavedStateHandle?) {
    val searchResult: VerseModel? = savedStateHandle?.get(SEARCH_RESULT)
    fun clear() = savedStateHandle?.remove<VerseModel?>(SEARCH_RESULT)

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
    }
}