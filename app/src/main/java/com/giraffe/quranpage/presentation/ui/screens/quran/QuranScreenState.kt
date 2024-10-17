package com.giraffe.quranpage.presentation.ui.screens.quran

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.TafseerEntity
import com.giraffe.quranpage.domain.entities.VerseEntity


data class QuranScreenState(
    val selectedVerseToRead: VerseEntity? = null,
    val selectedVerse: VerseEntity? = null,
    val firstVerse: VerseEntity? = null,
    val allOriginalPages: List<PageUI> = listOf(),
    val allPages: List<PageUI> = listOf(),
    val surahesData: List<SurahDataEntity> = listOf(),
    val surahesByJuz: Map<Int, List<SurahDataEntity>> = mapOf(),
    val allVerses: List<VerseEntity> = listOf(),
    val reciters: List<ReciterEntity> = emptyList(),
    val selectedReciter: ReciterEntity? = null,
    val selectedVerseTafseer: TafseerEntity? = null,
    val pageIndexToRead: Int? = null,
    val pageIndexToSelection: Int? = null,
    val isRecentDownloaded: Boolean = false,
    val recentUrl: String? = null,
    val recentSurahToDownload: SurahDataEntity? = null,
    val lastPageIndex: Int = 0,
    val bookmarkedVerse: VerseEntity? = null,
)

@Immutable
data class PageUI(
    val pageIndex: Int,
    val orgContents: List<Content>,
    val contents: List<Content>,
    val fontFamily: FontFamily,
    val surahName: String = "",
    val juz: Int = 0,
    val hezbStr: String? = null,
    val hezb: Int = 1,
    val hasSajdah: Boolean = false
)


@Immutable
data class Content(
    val surahNameAr: String,
    val verses: List<VerseEntity>,
    val text: AnnotatedString,
    val pageIndex: Int
)

