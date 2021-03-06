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

package com.example.cathaybkandroidexercise.data

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination

import com.example.cathaybkandroidexercise.GITHUB_STARTING_ID
import com.example.cathaybkandroidexercise.PER_PAGE_SIZE
import com.example.cathaybkandroidexercise.TOTAL_USERS_LIMIT
import com.example.cathaybkandroidexercise.api.GithubService
import com.example.cathaybkandroidexercise.model.*
import okio.IOException
import retrofit2.HttpException

/**
 * Repository class that works with local and remote data sources.
 */
class GithubUserList(private val serviceUserList: GithubService) {

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<User>()

    // keep the last user id. When the request is successful, update the last id
    private var lastSinceID = GITHUB_STARTING_ID

    private var totalUsersCount = 0

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    suspend fun getUserListResultStream(): UserListDataResult {

        lastSinceID = GITHUB_STARTING_ID
        totalUsersCount = 0
        inMemoryCache.clear()

        return requestAndSaveData()

    }

    suspend fun requestMore(): UserListDataResult {

        if (totalUsersCount >= TOTAL_USERS_LIMIT) return UserListDataResult.Info("Up to limit")
        if (isRequestInProgress) return UserListDataResult.Info("Request Busy")

        return requestAndSaveData()

    }

    private suspend fun requestAndSaveData(): UserListDataResult {

        isRequestInProgress = true
        lateinit var result: UserListDataResult

        try {

            val response = serviceUserList.searchUsers(lastSinceID, PER_PAGE_SIZE)
            inMemoryCache.addAll(response)
            val reposByName = sortUsersById()
            lastSinceID = reposByName.last().id
            totalUsersCount = reposByName.size
            result = UserListDataResult.Success(reposByName)

        } catch (exception: IOException) {
            result = UserListDataResult.Error(exception.message!!)
        } catch (exception: HttpException) {
            result = UserListDataResult.Error(exception.message!!)
        }

        isRequestInProgress = false
        return result

    }

    private fun sortUsersById(): List<User> {

        // from the in memory cache select only the repos whose name or description matches
        // the query. Then order the results.
        // The emit will be no function once emit cache with mutableList, so be careful.
        return inMemoryCache.sortedWith(compareBy<User> { it.id })

    }
}

