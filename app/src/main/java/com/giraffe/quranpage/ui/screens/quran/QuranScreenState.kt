package com.giraffe.quranpage.ui.screens.quran

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import com.giraffe.quranpage.local.model.ReciterModel
import com.giraffe.quranpage.local.model.SurahAudioModel
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.remote.response.TafseerResponse
import com.giraffe.quranpage.service.DownloadService


data class QuranScreenState(
    val selectedVerseToRead: VerseModel? = null,
    val selectedVerse: VerseModel? = null,
    val firstVerse: VerseModel? = null,
    val orgPages: List<PageUI> = listOf(),
    val pages: List<PageUI> = listOf(),
    val surahesData: List<SurahModel> = listOf(),
    val ayahs: List<VerseModel> = listOf(),
    val currentPageIndex: Int = 0,
    val reciters: List<ReciterModel> = emptyList(),
    val selectedReciter: ReciterModel? = null,
    val selectedAudioData: SurahAudioModel? = null,
    val selectedVerseTafseer: TafseerResponse? = null,
    val pageIndexToRead: Int? = null,
    val pageIndexToSelection: Int? = null,
    val isPlayerDialogVisible: Boolean = false,
    val recentDownload: DownloadService. DownloadedAudio? = null,
    val isRecentDownloaded:Boolean = false,
    val recentUrl:String? = null,
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
    val verses: List<VerseModel>,
    val text: AnnotatedString,
    val pageIndex: Int
)

