package com.example.cathaybkandroidexercise.ui

import com.example.cathaybkandroidexercise.api.GithubService

class UserDetailPresenter(
    private var view: UserDetailContract.View?)
    : UserDetailContract.Presenter{

    override suspend fun loadDetail(pathUserName: String) {

        val githubUserListService = GithubService.create()

        try {

            val user = githubUserListService.searchUserDetail(pathUserName)
            println("response $user")
            view?.onLoadedDetail(user)

        } catch (e: Exception){

            println("Error: $e")
            e.message?.let{
                view?.onLoadedDetailError(it)
            }

        }

    }

    override fun start() {

    }

}