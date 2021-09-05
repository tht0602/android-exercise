package com.example.cathaybkandroidexercise.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class GithubDataMethod {

    suspend fun sendGetUsersList(since: Int, perPage: Int): String{
        val url = "https://api.github.com/users?since=$since&per_page=$perPage"
        return readData(url)
    }

    suspend fun sendGetUser(userName: String): String{
        val url = "https://api.github.com/users/$userName"
        return readData(url)
    }

    private suspend fun readData(url : String): String{
        return withContext(Dispatchers.IO) {
            val connection =
                URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("Content-Type", "application/vnd.github.v3+json")

            try {

                BufferedReader(InputStreamReader(connection.inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append("$inputLine\n")
                        inputLine = it.readLine()
                    }
                    it.close()

                    return@withContext response.toString()
                }
            } catch (e: Exception) {
                println("Error $e")
            } finally {
                connection.disconnect()
                println("Complete")
            }
            return@withContext "";
        }
    }
}