package com.giraffe.quranpage.utils


/*
//==================================================================================================
var imageWidth by remember { mutableStateOf(0.dp) }
var imageHeight by remember { mutableStateOf(0.dp) }
val imageDensity = LocalDensity.current

var imageWidth by remember { mutableStateOf(0.dp) }
var imageHeight by remember { mutableStateOf(0.dp) }
val imageDensity = LocalDensity.current
Modifier.fillParentMaxWidth().onGloballyPositioned {
    imageWidth = with(imageDensity) {
        it.size.width.toDp()
    }
    imageHeight = with(imageDensity) {
        it.size.height.toDp()
    }
}
//==================================================================================================
//val url = "https://raw.githubusercontent.com/batoulapps/quran-svg/main/svg/${pageIndex.toThreeDigits()}.svg"
//val url = "https://raw.githubusercontent.com/salahamassi/Quran-svg-mobile/main/output/${pageIndex.toThreeDigits()}.svg"
//val url = "https://www.maknoon.com/quran/hafs/$pageIndex.svgz"
httpClient.build().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val fileData = response.body?.byteStream()
                if (fileData != null) {
                    try {
                        context.openFileOutput("p_${pageIndex.toThreeDigits()}.svg", Context.MODE_PRIVATE)
                            .use { output ->
                                output.write(fileData.readBytes())
                            }
                        onResponse.onSuccess(pageIndex.toString())
                    } catch (e: IOException) {
                        e.printStackTrace()
                        onResponse.onFail(e.message ?: "IOException: save file error !!")
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onResponse.onFail(e.message ?: "IOException: download file error !!")
            }
        })
//==================================================================================================
private fun downloadPages() {
        viewModelScope.launch {
            for (i in 1..5) {
                downloadPage(i)
            }
        }
    }

    private fun downloadPage(pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteDataSource.downloadPage(pageIndex, object : OnResponse {
                override fun onSuccess(result: String) {

                    //_state.update { it.copy(downloadPagesFlag = true) }
                }
                override fun onFail(errorMsg: String) {
                    Log.e(TAG, "onFail: $errorMsg")
                }
            })
        }
    }

    private fun readAllPages(){
        viewModelScope.launch {
            for (i in 1..20) {
                readPage(i)
            }
        }
    }

    private fun readPage(pageIndex: Int) {
        viewModelScope.launch {
            localDataSource.readQuranPageImage(pageIndex, object : OnResponse {
                override fun onSuccess(result: String) {
                    tempList.add(result)
                    if (tempList.size == 20){
                        _state.update { it.copy(pagesSVG = tempList, readFlag = true) }
                    }
                }

                override fun onFail(errorMsg: String) {

                }
            })
        }
    }

    private fun getSurah(surahIndex: Int) {
        viewModelScope.launch {
            try {
                val response = remoteDataSource.getSurah(surahIndex)
                if (response.isSuccessful) {
                    _state.update {
                        it.copy(
                            surahResponse = response.body(), points = response.body()
                                ?.get(1)?.polygon
                        )
                    }
                } else {
                    _state.update { it.copy(error = response.message()) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Unknown Error") }
            }
        }
    }


    private fun selectAyah(ayahIndex: Int = 0) {
        viewModelScope.launch {
            _state.update {
                it.copy(ayahIndex = ayahIndex, points = it.listOfAyahs[ayahIndex].polygon)
            }
        }
    }

    private fun getPages() {
        viewModelScope.launch {
            val mutableSet = mutableSetOf<String>()
            _state.value.surahResponse?.forEach {
                mutableSet.add(it.page)
            }
            _state.update { it.copy(listOfPagesUrls = mutableSet.toMutableList()) }
        }
    }

    fun nextAyah() {

        val newIndex = ++_state.value.ayahIndex
        selectAyah(newIndex)
        if (newIndex <= _state.value.listOfAyahs.size) {
            selectAyah(newIndex)
        } else {
            selectAyah()
        }
    }

    fun previousAyah() {
        val newIndex = --_state.value.ayahIndex
        if (newIndex < 0) {
            selectAyah()
        } else {
            selectAyah(newIndex)
        }
    }

    private fun getAyahsOfSelectedPage() {
        viewModelScope.launch {
            val ayahs = mutableListOf<AyahResponse>()
            _state.value.surahResponse?.forEach {
                if (it.page == "https://www.mp3quran.net/api/quran_pages_svg/003.svg") {
                    ayahs.add(it)
                }
            }
            _state.update { it.copy(listOfAyahs = ayahs) }
            selectAyah()
        }
    }
//==================================================================================================
data class HomeState(
    val readFlag:Boolean = false,
    val pagesSVG:MutableList<String> = mutableListOf(),
    val text:String = "",
    val downloadPagesFlag:Boolean = false,
    val pageIndex: Int? = 1,
    val points: String? = "",
    val error: String = "",
    var selectedAyah: Int = 6,
    var ayahIndex:Int =0,
    val listOfAyahs:MutableList<AyahResponse> = mutableListOf(),
    val surahResponse: SurahResponse? = null,
    val listOfPagesUrls:MutableList<String> = mutableListOf(),

) {
    private var _pageIndex = pageIndex.toString()


    private fun getPageIndex(): String {
        while (_pageIndex.length < 3) {
            _pageIndex = addZero(pageIndex.toString())
        }
        return _pageIndex
    }


    fun getPageUrl() ="https://www.mp3quran.net/api/quran_pages_svg/${getPageIndex()}.svg"


    private fun addZero(numStr: String) = if (numStr.length < 3) {
        "0".plus(numStr)
    } else {
        numStr
    }
}
//==================================================================================================
val mResponse = URL("https://www.mp3quran.net/api/quran_pages_svg/003.svg").readText()
val filesDir = context.filesDir
val file = File(filesDir, "page_003.svg")
file.writeText(mResponse)
Log.d("messi", "download: ${file.absolutePath}")
Log.d("messi", "download: ${file.readText()}")
onResponse.onSuccess(file.readText())
val request = Request.Builder()
    .url(Constants.PAGES_URL.replace("000", pageIndex.toThreeDigits()))
    .build()
httpClient.build().newCall(request).enqueue(object : Callback {
    override fun onResponse(call: Call, response: Response) {
        val fileData = response.body?.byteStream()
        if (fileData != null) {
            try {

                val filesDir = context.filesDir
                        val filePath = File(filesDir, "p_${pageIndex.toThreeDigits()}.svg").absolutePath
                        val fileOutputStream =context.openFileOutput("p_${pageIndex.toThreeDigits()}.svg", Context.MODE_PRIVATE)
                        fileOutputStream.write(fileData.readBytes())
                        fileOutputStream.close()

                            .use { output ->
                                output.write(fileData.readBytes())
                            }
                        //onResponse.onSuccess(file.readText())
                    } catch (e: IOException) {
                        e.printStackTrace()
                        onResponse.onFail(e.message ?: "IOException: save file error !!")
                    }
                }
            }
    override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onResponse.onFail(e.message ?: "IOException: download file error !!")
            }
})
//==================================================================================================
private fun downloadPages(from: Int, to: Int) {
    viewModelScope.launch(Dispatchers.IO) {
        for (i in from..to) {
            repository.downloadPage(i, object : OnResponse {
                override fun onSuccess(result: String) {
                    repository.storePageSvgData(i, result)
                    if (i == to) {
                        _state.update { it.copy(downloadPagesFlag = true) }
                        //getPages()
                    }
                }

                override fun onFail(errorMsg: String) {
                    Log.e(TAG, "onFail: $errorMsg")
                }
            })
        }
    }
}

private fun getPages() {
    viewModelScope.launch(Dispatchers.IO) {
        _state.update { it.copy(pagesSVG = repository.getPages()) }
    }
}

private fun deletePages() {
    viewModelScope.launch(Dispatchers.IO) {
        repository.deletePages()
    }
}
//==================================================================================================
fun String.convertSvgToImageBitmap(width: Int, height: Int): ImageBitmap {
    val svg = SVG.getFromString(this)
    val picture = svg.renderToPicture()
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas: Canvas
    val resizedPicture = Picture()
    canvas = resizedPicture.beginRecording(width, height)
    canvas.drawPicture(picture, Rect(0, 0, width, height))
    resizedPicture.endRecording()
    val pictureDrawable = PictureDrawable(resizedPicture)
    val newBitmap = pictureDrawable.toBitmap()
    return newBitmap.asImageBitmap()
}
fun Int.convertDrawableResToImageBitmap(context: Context): ImageBitmap {
    val bitmap = BitmapFactory.decodeResource(context.resources, this)
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 35, 41, false)
    return resizedBitmap.asImageBitmap()
}
fun Configuration.getScreenWidth() = this.screenWidthDp
fun Configuration.getScreenHeight() = this.screenHeightDp
//==================================================================================================
@Qualifier
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD
)
annotation class Quran

@Qualifier
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD
)
annotation class AzkarAndHadith
//==================================================================================================
@Update
fun updatePath(ayahModel: AyahModel)

@Query("UPDATE ayahs SET path = :newPath WHERE id = :ayahId")
fun updateAyahPathById(ayahId: Int, newPath: Path)
//==================================================================================================
private fun initStates() {
    viewModelScope.launch(Dispatchers.IO) {
        repository.getAyahsOfSurah(2) {
            repository.getPages { pages ->
                _state.update {
                    it.copy(
                        pages = pages,
                        ayahs = repository.getAyahs(it.pageIndex)
                    )
                }
            }
        }

    }
}
//==================================================================================================
var bitmapState by remember{ mutableStateOf<Bitmap?>(null) }
    var string by remember{ mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(true) {
        kotlin.runCatching {
            val inputStream = context.assets.open("surahes_data.json")

            //bitmapState = BitmapFactory.decodeStream(inputStream)

            /*val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            String(buffer)*/
        }.onSuccess {
            Log.e("messi", "QuranContent(onSuccess): $it")
        }.onFailure {
            Log.e("messi", "QuranContent(onFailure): $it")
        }

    }
//==================================================================================================
fun getLocalAyahsOfSurah(context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                val inputStream = context.assets.open("quran_text.json")
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                String(buffer)
            }.onSuccess {
                val newList = mutableListOf<VerseModel>()
                for (surahIndex in 1..114){
                    val localVerses = Gson().fromJson(it, Array<VerseModel>::class.java).toList().filter {v-> v.surahNumber == surahIndex }
                    val remoteAyahs = repository.getAyahsOfSurah(surahIndex)
                    remoteAyahs?.forEach {ayahModel->
                        if (ayahModel.ayahIndex!=0) newList.add(localVerses[ayahModel.ayahIndex-1].copy(pageIndex = ayahModel.pageIndex))
                    }
                }
                Log.d(TAG, "getLocalAyahsOfSurah(onSuccess): ${newList.size}")
                val filePath = context.filesDir
                val fileName = "my_file_new_ayahs.txt"
                val file = File(filePath, fileName)
                try {
                    val writer = BufferedWriter(FileWriter(file))
                    writer.write(Gson().toJson(newList))
                    writer.close()
                    Log.d(TAG, "getLocalAyahsOfSurah(fileCreated): ${newList.size}")
                    // File is now created with the specified content
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(TAG, "getLocalAyahsOfSurah(fileNotCreated): ${e.message}")

                    // Handle any exceptions (e.g., permission issues, etc.)
                }
            }.onFailure {
                Log.e(TAG, "getLocalAyahsOfSurah(onFailure): $it")
            }
        }
    }
//==================================================================================================

*/


