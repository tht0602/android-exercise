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

import com.example.cathaybkandroidexercise.api.GithubUserListService
import com.example.cathaybkandroidexercise.model.GithubUserListResult
import com.example.cathaybkandroidexercise.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import okio.IOException
import retrofit2.HttpException

private const val GITHUB_STARTING_ID = 0

private const val TOTAL_USERS_LIMIT = 100

private const val PER_PAGE_SIZE = 20

/**
 * Repository class that works with local and remote data sources.
 */
class GithubUserList(private val serviceUserList: GithubUserListService) {

    // keep the list of all results received
    private val inMemoryCache = mutableListOf<User>()

    // shared flow of results, which allows us to broadcast updates so
    // the subscriber will have the latest data
    private val searchResults = MutableSharedFlow<GithubUserListResult>(replay = 1)

    // keep the last user id. When the request is successful, update the last id
    private var lastSinceID = GITHUB_STARTING_ID

    //
    private var totalUsersCount = 0

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    suspend fun getUserListResultStream(): Flow<GithubUserListResult> {
        lastSinceID = GITHUB_STARTING_ID
        totalUsersCount = 0
        inMemoryCache.clear()
        requestAndSaveData()

        return searchResults
    }

    suspend fun requestMore() {
        if (totalUsersCount >= TOTAL_USERS_LIMIT) return
        if (isRequestInProgress) return

        requestAndSaveData()
    }

    suspend fun retry() {
        if (isRequestInProgress) return

        requestAndSaveData()
    }

    private suspend fun requestAndSaveData(): Boolean {
        isRequestInProgress = true
        var successful = false

        try {

            val response = serviceUserList.searchUsers(lastSinceID, PER_PAGE_SIZE)
            println("response $response")
            inMemoryCache.addAll(response)
            println("emit ")
            val reposByName = usersById()
            searchResults.emit(GithubUserListResult.Success(reposByName))
            println("emit end")
            successful = true
            lastSinceID = reposByName.last().id
            totalUsersCount = reposByName.size

        } catch (exception: IOException) {
            searchResults.emit(GithubUserListResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.emit(GithubUserListResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    private fun usersById(): List<User> {
        // from the in memory cache select only the repos whose name or description matches
        // the query. Then order the results.
        // The emit will be no function once emit cache without sort, I don't Know why.
        return inMemoryCache.sortedWith(compareBy<User> { it.id })
    }
}

