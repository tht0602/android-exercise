package com.example.cathaybkandroidexercise

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class GithubDataMethod {

    suspend fun sendGetUsersList() {
        val url = "https://api.github.com/users?since=1&per_page=2"
        readData(url)
    }

    suspend fun sendGetUser(userName: String) {
        val url = "https://api.github.com/users/$userName"
        readData(url)
    }

    private suspend fun readData(url : String): String{

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
                while(response.isNotBlank()) {
                    println("$response")
                    response.delete(0, 4000)
                }
                return response.toString()
            }
        } catch (e: Exception) {
            println("Error $e")
        } finally {
            connection.disconnect()
            println("Complete")
        }
        return "";
    }
}