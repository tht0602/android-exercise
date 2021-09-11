package com.example.cathaybkandroidexercise.ui

import com.example.cathaybkandroidexercise.BasePresenter
import com.example.cathaybkandroidexercise.BaseView
import com.example.cathaybkandroidexercise.model.User

interface UserDetailContract {

    interface View: BaseView<Presenter> {
        fun onLoadedDetail(user: User)
        fun onLoadedDetailError(message: String)
    }

    interface Presenter:BasePresenter{
        suspend fun loadDetail(pathUserName: String)
    }

}