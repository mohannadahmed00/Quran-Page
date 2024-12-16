package com.giraffe.quranpage.presentation.ui.screens.quran

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import com.giraffe.quranpage.common.utils.domain.NetworkError
import com.giraffe.quranpage.common.utils.presentation.convertVerseToText
import com.giraffe.quranpage.domain.entities.ContentEntity
import com.giraffe.quranpage.domain.entities.PageEntity
import com.giraffe.quranpage.domain.entities.ReciterEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.TafseerEntity
import com.giraffe.quranpage.domain.entities.VerseEntity

@Immutable
data class QuranScreenState(
    val selectedVerseToRead: VerseEntity? = null,
    val selectedVerse: VerseEntity? = null,
    val firstVerse: VerseEntity? = null,
    val allOriginalPages: List<PageUi> = listOf(),
    val allPages: List<PageUi> = listOf(),
    val surahesData: List<SurahDataEntity> = listOf(),
    val surahesByJuz: Map<Int, List<SurahDataEntity>> = mapOf(),
    val reciters: List<ReciterEntity> = emptyList(),
    val selectedReciter: ReciterEntity? = null,
    val selectedVerseTafseer: TafseerEntity? = null,
    val selectedVerseTafseerError: NetworkError? = null,
    val pageIndexToRead: Int? = null,
    val pageIndexToSelection: Int? = null,
    val isRecentDownloaded: Boolean = false,
    val recentUrl: String? = null,
    val recentSurahToDownload: SurahDataEntity? = null,
    val lastPageIndex: Int? = null,
    val bookmarkedVerse: VerseEntity? = null,
    val highlightedVerse: VerseEntity? = null,
)

@Immutable
data class PageUi(
    val pageIndex: Int,
    val orgContents: List<ContentUi>,
    val contents: List<ContentUi>,
    val namesOfSurahes: String = "",
    val juz: Int = 0,
    val hezbStr: String? = null,
    val hezb: Int = 1,
    val hasSajdah: Boolean = false
)

fun PageEntity.toUi() = PageUi(
    pageIndex = pageIndex,
    orgContents = orgContents.map { it.toUi() },
    contents = orgContents.map { it.toUi() },
    namesOfSurahes = namesOfSurahes,
    juz = juz,
    hezbStr = hezbStr,
    hezb = hezb,
    hasSajdah = hasSajdah
)


@Immutable
data class ContentUi(
    val surahNameAr: String,
    val verses: List<VerseEntity>,
    val text: AnnotatedString,
    val pageIndex: Int,
)

fun ContentEntity.toUi() = ContentUi(
    surahNameAr = surahNameAr,
    verses = verses,
    text = convertVerseToText(verses = verses),
    pageIndex = pageIndex
)

