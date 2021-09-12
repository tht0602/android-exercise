package com.example.cathaybkandroidexercise.ui

import com.example.cathaybkandroidexercise.api.GithubService

class UserDetailPresenter(): UserDetailContract.Presenter{

    private var view: UserDetailContract.View? = null

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

    override fun attach(view: UserDetailContract.View) {
        this.view= view

    }

    override fun detach() {
        view = null
    }

}