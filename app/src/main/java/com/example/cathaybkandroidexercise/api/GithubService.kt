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

package com.example.cathaybkandroidexercise.api

import com.example.cathaybkandroidexercise.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Github API communication setup via Retrofit.
 */

interface GithubService {

    /**
     * Get UserList ordered by stars.
     */
    @GET("users")
    suspend fun searchUsers(
        @Query("since") since: Int,
        @Query("per_page") PerPage: Int
    ): List<User>

    /**
     * Get UserList ordered by stars.
     */
    @GET("users/{username}")
    suspend fun searchUserDetail(
        @Path("username") username: String,
    ): User

    companion object {

        private const val BASE_URL = "https://api.github.com/"

        fun create(): GithubService {

            println("GithubUserListService create()")
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService::class.java)

        }
    }
}