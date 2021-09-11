package com.example.cathaybkandroidexercise

interface BaseView<T> {
    fun setPresenter(presenter: T)
}