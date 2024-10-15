package com.giraffe.quranpage.utils

import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.ui.screens.quran.PageUI

fun hasSajdah(orgPages: List<PageUI>, pageIndex: Int): Boolean {
    val verses = orgPages.firstOrNull { it.pageIndex == pageIndex }?.contents?.flatMap { it.verses }
        ?: emptyList()
    return verses.any { it.sajda }
}

fun getSurahesName(surahesData: List<SurahModel>, pageIndex: Int): String {
    val str = StringBuilder()
    var surahNumber = 0
    surahesData.filter { pageIndex in it.startPage..it.endPage }
        .forEach {
            if (it.id != surahNumber) {
                surahNumber = it.id
                str.append(surahesData.firstOrNull { surah -> surah.id == surahNumber }?.name ?: "")
                str.append("    ")
            }
        }
    return str.toString().trim()
}

fun getHezb(currentHezbNumber: Int, pageHezbNumber: Int, action: (Int) -> Unit): String? {
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

fun getJuz(pageIndex: Int): Int {
    val result = pageIndex / 20.0
    val truncatedResult = result.toInt() //13
    val digitAfterDecimal = result.toString().split(".")[1][0].digitToInt()
    val juz = if (digitAfterDecimal == 0) {
        truncatedResult
    } else {
        truncatedResult + 1
    }
    return if (juz == 0) 1 else if (juz == 31) 30 else juz
}

fun getPageIndexOfJuz(juzIndex: Int) = if (juzIndex == 1) 1 else "${((juzIndex - 1) * 2)}2".toInt()