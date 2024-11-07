package com.giraffe.quranpage

import androidx.compose.runtime.mutableStateMapOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val _queueState = MutableStateFlow(mutableStateMapOf<String, User>())

    @Test
    fun `when set a ahmed user in mutableStateMapOf and try to update his name`() {
        updateUser(User("1","ahmed"))
        println("Hello, world!!! ${_queueState.value}")
        updateUser(User("1","ali"))
        println("Hello, world!!! ${_queueState.value}")
        assertEquals(_queueState.value["1"]?.name, "ali")
    }



    private fun updateUser(user: User) {
        _queueState.update { queue ->
            queue.apply {
                set(key = user.id, value = user)
            }
        }
    }


    data class User(
        val id:String,
        val name:String
    )

}