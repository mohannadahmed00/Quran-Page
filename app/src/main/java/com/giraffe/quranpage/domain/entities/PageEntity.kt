package com.giraffe.quranpage.domain.entities


data class PageEntity(
    val pageIndex: Int,
    val orgContents: List<ContentEntity>,
    val contents: List<ContentEntity>,
    val namesOfSurahes: String = "",
    val juz: Int = 0,
    val hezbStr: String? = null,
    val hezb: Int = 1,
    val hasSajdah: Boolean = false
)
