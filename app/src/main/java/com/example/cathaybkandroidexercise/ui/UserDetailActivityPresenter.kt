package com.example.cathaybkandroidexercise.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.example.cathaybkandroidexercise.api.GithubUserListService
import kotlinx.coroutines.launch
import okio.IOException

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

            try {

                val user = githubUserListService.searchUserDetail(queryUserName)
                println("response $user")
                view.onLoadedDetail(user)

            }catch (e: Exception){

                println("Error: $e")
                e.message?.let{
                    view.onLoadedDetailError(it)
                }

            }

        }
    }

}