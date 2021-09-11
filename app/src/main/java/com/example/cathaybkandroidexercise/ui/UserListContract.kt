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

import com.example.cathaybkandroidexercise.BasePresenter
import com.example.cathaybkandroidexercise.BaseView
import com.example.cathaybkandroidexercise.model.User

interface UserListContract {

    interface View: BaseView<Presenter> {
        fun onLoadedList(userList: List<User>)
        fun onLoadedListError(message: String)
        fun onListAdded(userList: List<User>)
        fun onScrolledToBottom()
    }

    interface Presenter: BasePresenter {
        suspend fun reloadUserList()
        suspend fun loadMoreUserList()
        fun onScrolled(pastVisibleItems: Int, visibleItemCount: Int, totalItemCount: Int)
        fun getUserListCount(): Int
        fun onBindViewHolder(userItemView: UserItemView, position: Int)
    }

    interface UserItemView {
        fun bindData(user: User, numberOfItems: Int)
    }

}