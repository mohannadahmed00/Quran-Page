package com.giraffe.quranpage.utils

import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.ui.screens.quran.PageUI

fun hasSajdah(orgPages:List<PageUI>,pageIndex: Int): Boolean {
    val verses = orgPages.firstOrNull{ it.pageIndex==pageIndex }?.contents?.flatMap { it.verses }?: emptyList()
    return verses.any { it.sajda }
}
fun getSurahesName(surahesData:List<SurahModel>,pageIndex: Int):String{
    val str = StringBuilder()
    var surahNumber = 0
    surahesData.filter { pageIndex in it.startPage..it.endPage }
        .forEach{
            if (it.id != surahNumber) {
                surahNumber = it.id
                str.append(surahesData.firstOrNull { surah -> surah.id == surahNumber }?.name ?: "")
                str.append("    ")
            }
        }
    return str.toString().trim()
}
fun getHezb(currentHezbNumber:Int,pageHezbNumber:Int,action:(Int)->Unit):String?{
    return if (currentHezbNumber == pageHezbNumber) {
        null
    } else {
        val prev = currentHezbNumber
        action(pageHezbNumber)
        getHezbStr(prev)
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
    val truncatedResult = (result * 10).toInt() / 10.0
    val digitAfterDecimal = ((result - result.toInt()) * 10).toInt()
    val juz = if (digitAfterDecimal == 0) {
        truncatedResult.toInt()
    } else {
        truncatedResult.toInt() + 1
    }
    return when (juz) {
        0 -> {
            1
        }

        31 -> {
            30
        }

        else -> {
            juz
        }
    }
}