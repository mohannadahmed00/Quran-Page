package com.giraffe.quranpage.ui.screens.quran


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.quranpage.local.model.SurahModel
import com.giraffe.quranpage.local.model.VerseModel
import com.giraffe.quranpage.repo.Repository
import com.giraffe.quranpage.ui.theme.fontFamilies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class QuranViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    QuranEvents {
    private val _state = MutableStateFlow(QuranScreenState())
    val state = _state.asStateFlow()

    init {
        getSurahesData()
        getAllVerses()
    }

    private fun getSurahesData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSurahesData().let { surahes ->
                _state.update { it.copy(surahesData = surahes) }
            }
        }
    }

    fun getAllVerses() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllVerses().let { ayahs ->
                var currentHezbNumber = 1
                _state.update { state ->
                    state.copy(
                        pages = ayahs.groupBy { it.pageIndex }.toList().map {
                            val pageHezb = getHezbNumbre(it.second)//2
                            PageUI(
                                pageIndex = it.first,
                                verses = it.second,
                                fontFamily = fontFamilies[it.first - 11],
                                surahName = getSurahesOfPage(it.second,_state.value.surahesData),
                                juz = ceil(it.first/20.0).toInt(),
                                hezb = if (currentHezbNumber==pageHezb) {
                                    null
                                } else {
                                    val prev = currentHezbNumber
                                    currentHezbNumber = pageHezb
                                    getHezbStr(prev)
                                }
                            )
                        },
                    )
                }
            }
        }
    }

    private fun getHezbNumbre(verses:List<VerseModel>):Int{
        var hezbNumber = 0
        verses.forEach {
            hezbNumber = it.quarterHezbIndex
        }
        return hezbNumber
    }

    private fun getHezbStr(quarterIndex:Int):String{
        val integerPart = (quarterIndex/4.0).toInt()//0
        val fractionalPart = (quarterIndex/4.0)-integerPart//0.25
        val str = StringBuilder()
        if (fractionalPart!=0.0) str.append(decimalToFraction(fractionalPart))
        str.append(" Hezb ${integerPart+1}")
        return str.toString()
    }

    private fun decimalToFraction(decimal: Double) = "${(decimal/.25).toInt()}/4"

    private fun getSurahesOfPage(verses: List<VerseModel>, surahesData: List<SurahModel>): String {
        val str = StringBuilder()
        var surahNumber = 0
        verses.forEach {
            if (it.surahNumber != surahNumber) {
                surahNumber = it.surahNumber
                str.append(surahesData.firstOrNull { surah -> surah.id == surahNumber }?.name ?: "")
                str.append(" ")
            }
        }
        return str.toString().trim()

    }

    override fun onVerseSelected(verse: VerseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(selectedVerse = verse) }
        }
    }
}