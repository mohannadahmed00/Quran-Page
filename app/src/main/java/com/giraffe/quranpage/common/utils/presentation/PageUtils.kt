package com.giraffe.quranpage.common.utils.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.giraffe.quranpage.domain.entities.ContentEntity
import com.giraffe.quranpage.domain.entities.SurahDataEntity
import com.giraffe.quranpage.domain.entities.VerseEntity
import com.giraffe.quranpage.presentation.ui.screens.quran.PageUi
import com.giraffe.quranpage.presentation.ui.theme.fontFamilies
import com.giraffe.quranpage.presentation.ui.theme.onPrimaryContainerLight
import com.giraffe.quranpage.presentation.ui.theme.primaryLight


fun highlightVerse(
    originalPages: List<PageUi>,
    selectedVerse: VerseEntity?,
    selectedVerseToRead: VerseEntity?
): MutableList<PageUi> {
    val pages = originalPages.toMutableList()//pure pages
    var pageUiToSelect = pages.getOrNull(selectedVerse?.pageIndex?.minus(1) ?: -1) //white 603 page
    var pageUiToRead =
        pages.getOrNull(selectedVerseToRead?.pageIndex?.minus(1) ?: -1) //white 603 page
    val contentsToSelect = pageUiToSelect?.orgContents?.map { content ->
        if (content.verses.contains(selectedVerse)) {
            content.copy(
                text = convertVerseToText(
                    content.verses,
                    selectedVerse,
                    selectedVerseToRead
                )
            )
        } else {
            content
        }
    } // just select content is updated
    val contentsToRead = pageUiToRead?.orgContents?.map { content ->
        if (content.verses.contains(selectedVerseToRead)) {
            content.copy(
                text = convertVerseToText(
                    content.verses,
                    selectedVerse,
                    selectedVerseToRead
                )
            )
        } else {
            content
        }
    } // just read content is updated
    contentsToSelect?.let { contents ->
        pageUiToSelect = pageUiToSelect?.copy(contents = contents)
    }
    contentsToRead?.let { contents ->
        if (pageUiToSelect != null && pageUiToSelect?.pageIndex == pageUiToRead?.pageIndex) {
            val newContents = contents.toMutableList()
            val updateContent =
                contentsToSelect?.firstOrNull { content -> content.verses.contains(selectedVerse) }
            val updateContentIndex =
                contentsToSelect?.indexOfFirst { content -> content.verses.contains(selectedVerse) }
            updateContent?.let {
                updateContentIndex?.let {
                    newContents[updateContentIndex] = updateContent
                }
            }
            pageUiToRead = pageUiToRead?.copy(contents = newContents)
        } else {
            pageUiToRead = pageUiToRead?.copy(contents = contents)
        }
    }

    pageUiToSelect?.let { page ->
        pages[page.pageIndex - 1] = page
    }
    pageUiToRead?.let { page ->
        pages[page.pageIndex - 1] = page
    }
    return pages
}


fun convertVerseToText(
    verses: List<VerseEntity>,
    verseToSelect: VerseEntity? = null,
    verseToRead: VerseEntity? = null,
): AnnotatedString {
    return buildAnnotatedString {
        verses.forEach { verse ->
            pushStringAnnotation(tag = verse.qcfContent, annotation = verse.qcfContent)
            val handledVerse = handleVerse(verse)
            withStyle(
                style = SpanStyle(
                    color = onPrimaryContainerLight,
                    fontFamily = fontFamilies[verse.pageIndex - 1],
                    background = when (verse) {
                        verseToSelect -> {
                            primaryLight.copy(
                                alpha = 0.2f
                            )
                        }

                        verseToRead -> {
                            primaryLight.copy(
                                alpha = 0.1f
                            )
                        }

                        else -> Color.Transparent
                    }
                )
            ) {
                append(handledVerse.substring(0, handledVerse.length - 1))
                withStyle(style = SpanStyle(color = primaryLight.copy(alpha = 0.5f))) {
                    append(handledVerse.substring(handledVerse.length - 1))
                }

            }
            pop()

        }
    }
}

fun handleVerse(verse: VerseEntity): String {
    return when (verse.pageIndex) {
        1 -> {
            handelALFatiha(verse.qcfContent, verse.verseIndex)
        }

        2 -> {
            handelFirstPageOfAlBaqarah(verse.qcfContent, verse.verseIndex)
        }

        else -> {
            verse.qcfContent
        }
    }
}

fun handelALFatiha(txt: String, verseIndex: Int): String {
    val str = StringBuilder()
    when (verseIndex) {
        2, 3, 5 -> {
            str.append("\n")
            str.append(txt)
        }

        7 -> {
            txt.forEachIndexed { index, c ->
                str.append(c)
                if (index == txt.length - 4) str.append("\n")
            }
        }

        else -> {
            str.append(txt)
        }
    }
    return str.toString()

}

fun handelFirstPageOfAlBaqarah(txt: String, verseIndex: Int): String {
    val str = StringBuilder()
    val l = txt.length
    when (verseIndex) {
        2 -> {
            txt.forEachIndexed { index, c ->
                str.append(c)
                if (index == l - 3) str.append("\n")
            }
        }

        5 -> {
            txt.forEachIndexed { index, c ->
                str.append(c)
                if (index == l - 4) str.append("\n")
            }
        }

        else -> {
            str.append(txt)
        }
    }
    return str.toString()

}

fun hasSajdah(contents: List<ContentEntity>) = contents.firstOrNull { content ->
    content.verses.firstOrNull { verse -> verse.hasSajda } != null
} != null


fun getHezb(
    currentHezbNumber: Int,
    pageHezbNumber: Int,
    action: (Int) -> Unit
): String? {
    return if (currentHezbNumber == pageHezbNumber) {
        null
    } else {
        action(pageHezbNumber)
        getHezbStr(currentHezbNumber)
    }
}

fun getHezbStr(quarterIndex: Int): String {
    val integerPart = (quarterIndex / 4.0).toInt()//0
    val fractionalPart = (quarterIndex / 4.0) - integerPart//0.25
    val str = StringBuilder()
    if (fractionalPart != 0.0) str.append(if ((fractionalPart / .25).toInt() == 2) "1/2" else "${(fractionalPart / .25).toInt()}/4")
    str.append(" hezb ${integerPart + 1}")
    return str.toString()
}

fun getNamesOfSurahes(surahesData: List<SurahDataEntity>, pageIndex: Int): String {
    val str = StringBuilder()
    var surahNumber = 0
    surahesData.filter { pageIndex in it.startPageIndex..it.endPageIndex }
        .forEach {
            if (it.id != surahNumber) {
                surahNumber = it.id
                str.append(
                    surahesData.firstOrNull { surah -> surah.id == surahNumber }?.englishName
                        ?: ""
                )
                str.append("    ")
            }
        }
    return str.toString().trim()
}

fun getNamesOfSurahes(contents:List<ContentEntity>): String {
    val str = StringBuilder()
    contents.forEach { content->
        str.append(content.verses.first().surahNameEn)
        str.append("    ")
    }
    return str.toString().trim()
}