package com.giraffe.quranpage.data.datasource.remote

import com.giraffe.quranpage.common.utils.domain.NetworkError
import com.giraffe.quranpage.common.utils.domain.Resource
import com.giraffe.quranpage.data.datasource.remote.responses.ReciterResponse
import com.giraffe.quranpage.data.datasource.remote.responses.TafseerResponse
import com.giraffe.quranpage.data.datasource.remote.responses.VerseTimingResponse

interface RemoteDataSource {
    suspend fun getVersesTimingOfSurah(
        surahIndex: Int,
        reciterId: Int = 5
    ): Resource<List<VerseTimingResponse>, NetworkError>

    suspend fun getTafseer(
        surahIndex: Int,
        ayahIndex: Int
    ): Resource<TafseerResponse, NetworkError>

    suspend fun getReciters(): Resource<List<ReciterResponse>, NetworkError>
}