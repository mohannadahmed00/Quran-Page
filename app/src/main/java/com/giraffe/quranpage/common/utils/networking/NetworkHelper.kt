package com.giraffe.quranpage.common.utils.networking

import com.giraffe.quranpage.common.utils.domain.NetworkError
import com.giraffe.quranpage.common.utils.domain.Resource
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import javax.xml.transform.TransformerException
import kotlin.coroutines.coroutineContext

inline fun <reified T> responseToResource(response: Response<T>): Resource<T, NetworkError> {
    return when (response.code()) {
        in 200..299 -> {
            try {
                response.body()?.let { body ->
                    Resource.Success(body)
                } ?: Resource.Error(NetworkError.UNKNOWN)

            } catch (e: TransformerException) {
                Resource.Error(NetworkError.SERIALIZATION)
            }
        }

        408 -> Resource.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Resource.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Resource.Error(NetworkError.SERVER_ERROR)
        else -> Resource.Error(NetworkError.UNKNOWN)
    }
}

suspend inline fun <reified T> safeCall(execute: () -> Response<T>): Resource<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Resource.Error(NetworkError.NO_INTERNET)
    } catch (e: UnknownHostException) {
        return Resource.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return Resource.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Resource.Error(NetworkError.UNKNOWN)
    }
    return responseToResource(response)
}
