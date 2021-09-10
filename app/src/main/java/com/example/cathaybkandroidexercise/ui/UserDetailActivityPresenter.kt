package com.example.cathaybkandroidexercise.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.example.cathaybkandroidexercise.api.GithubUserListService
import kotlinx.coroutines.launch

class UserDetailActivityPresenter(
    val view: UserDetailContract.View,
    private val lifecycle: Lifecycle,
    private val queryUserName: String) : UserDetailContract.Presenter{

    override fun start() {
        loadDetail()
    }

    override fun loadDetail() {

        lifecycle.coroutineScope.launch {

            val githubUserListService = GithubUserListService.create()

            val user = githubUserListService.searchUserDetail(queryUserName)

            println("response $user")

            view.showDetail(user)

        }
    }

}