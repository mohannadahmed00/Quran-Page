package com.giraffe.quranpage.data.datasource.remote

import com.giraffe.quranpage.common.utils.networking.safeCall
import com.giraffe.quranpage.data.datasource.remote.api.RecitersApiServices
import com.giraffe.quranpage.data.datasource.remote.api.TafseerApiServices
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val tafseerApiServices: TafseerApiServices,
    private val recitersApiServices: RecitersApiServices,
) : RemoteDataSource {
    override suspend fun getVersesTimingOfSurah(
        surahIndex: Int,
        reciterId: Int
    ) = safeCall {
        recitersApiServices.getVerseTimingOfSurah(
            surahIndex = surahIndex,
            reciterId = reciterId
        )
    }

    override suspend fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    ) = safeCall { tafseerApiServices.getTafseer(surahIndex = surahIndex, ayahIndex = ayahIndex) }

    override suspend fun getReciters() = safeCall { recitersApiServices.getReciters() }
}