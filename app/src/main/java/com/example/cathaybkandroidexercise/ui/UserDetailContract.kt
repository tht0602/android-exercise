package com.example.cathaybkandroidexercise.ui

import com.example.cathaybkandroidexercise.BasePresenter
import com.example.cathaybkandroidexercise.BaseView
import com.example.cathaybkandroidexercise.model.User

interface UserDetailContract {

    interface View: BaseView<Presenter> {
        fun showDetail(user: User)
    }

    interface Presenter:BasePresenter{
        fun loadDetail()
    }

}