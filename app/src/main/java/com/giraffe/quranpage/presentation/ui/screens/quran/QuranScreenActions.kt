package com.giraffe.quranpage.presentation.ui.screens.quran

import com.giraffe.quranpage.domain.entities.VerseEntity


sealed interface QuranScreenActions {
    data class HighlightVerse(val verse: VerseEntity, val isToRead: Boolean = false) :
        QuranScreenActions

    data class UnhighlightVerse(val isToRead: Boolean = false) : QuranScreenActions
}