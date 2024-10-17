package com.giraffe.quranpage.common.utils


fun getJuzOfPageIndex(pageIndex: Int): Int {
    val result = pageIndex / 20.0
    val truncatedResult = result.toInt()
    val digitAfterDecimal = result.toString().split(".")[1][0].digitToInt()
    val juz = if (digitAfterDecimal == 0) {
        truncatedResult
    } else {
        truncatedResult + 1
    }
    return if (juz == 0) 1 else if (juz == 31) 30 else juz
}

fun getPageIndexOfJuz(juzIndex: Int) = if (juzIndex == 1) 1 else "${((juzIndex - 1) * 2)}2".toInt()