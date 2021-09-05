/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            val connection = URL(url).openConnection() as HttpURLConnection
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
            return@withContext ""
        }
    }
}