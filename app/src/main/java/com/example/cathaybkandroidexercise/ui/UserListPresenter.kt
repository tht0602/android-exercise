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

package com.example.cathaybkandroidexercise.ui

import com.example.cathaybkandroidexercise.Injection
import com.example.cathaybkandroidexercise.PER_PAGE_SIZE
import com.example.cathaybkandroidexercise.VISIBLE_THRESHOLD
import com.example.cathaybkandroidexercise.data.GithubUserList
import com.example.cathaybkandroidexercise.model.User
import com.example.cathaybkandroidexercise.model.UserListDataResult

class UserListPresenter (
    val view: UserListContract.View)
    : UserListContract.Presenter {

    private lateinit var githubUserList: GithubUserList
    private var userList: List<User>? = null

    override suspend fun loadMoreUserList() {
        val result = githubUserList.requestMore()
        println("response $result")
        parserResult(result)
    }

    override fun onScrolled(pastVisibleItems: Int, visibleItemCount: Int, totalItemCount: Int) {
        println("pastVisibleItems= $pastVisibleItems  visibleItemCount= $visibleItemCount  totalItemCount= $totalItemCount")
        if (pastVisibleItems + visibleItemCount + VISIBLE_THRESHOLD >= totalItemCount) {
            view.onScrolledToBottom()
        }
    }

    override fun getUserListCount() : Int{
        userList?.let { return it.size }
        return 0
    }

    override fun onBindViewHolder(userItemView: UserListContract.UserItemView, position: Int) {
        userList?.let{
            val user = it[position]
            userItemView.bindData(user, position)
        }
    }

    override fun start() {

    }

    override suspend fun reloadUserList() {

        githubUserList = Injection.provideGithubUserList()

        val result = githubUserList.getUserListResultStream()
        println("response $result")
        parserResult(result)


    }

    private fun parserResult(result: UserListDataResult){

        when (result) {

            is UserListDataResult.Success -> {
                val userList = result.data
                this.userList = userList
                if(result.data.size <= PER_PAGE_SIZE) {
                    view.onLoadedList(userList)
                }else{
                    view.onListAdded(userList)
                }
            }

            is UserListDataResult.Error -> {
                view.onLoadedListError(result.errorMessage)
            }

            is UserListDataResult.Info -> {
                println("$result")
            }

        }

    }

}