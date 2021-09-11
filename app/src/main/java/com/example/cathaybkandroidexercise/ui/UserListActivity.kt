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

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cathaybkandroidexercise.R
import com.example.cathaybkandroidexercise.model.User
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch


class UserListActivity : AppCompatActivity(), UserListContract.View {

    private lateinit var presenter: UserListContract.Presenter
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setPresenter(UserListPresenter(this))

        recycler_github_list.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val pastVisibleItems: Int = linearLayoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount: Int = linearLayoutManager.childCount
                    val totalItemCount: Int = linearLayoutManager.itemCount
                    presenter.onScrolled(pastVisibleItems, visibleItemCount, totalItemCount)
                }
            })

        lifecycleScope.launch{
            presenter.reloadUserList()
        }
    }

    override fun onLoadedList(userList: List<User>) {
        val userListAdapter = UserListAdapter(presenter)
        recycler_github_list.adapter = userListAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onListAdded(userList: List<User>) {
        recycler_github_list.adapter?.notifyDataSetChanged()
    }

    override fun onLoadedListError(message: String) {
        Toast.makeText(this, "LoadedListError: $String}", Toast.LENGTH_LONG ).show()
    }

    override fun onScrolledToBottom() {
        lifecycleScope.launch{
            presenter.loadMoreUserList()
        }
    }

    override fun setPresenter(presenter: UserListContract.Presenter) {
        this.presenter = presenter
    }




}
