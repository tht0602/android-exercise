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

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.cathaybkandroidexercise.Injection
import com.example.cathaybkandroidexercise.databinding.ActivityMainBinding
import com.example.cathaybkandroidexercise.model.GithubUserListResult
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration


class GithubUserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get the view model
        val viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(owner = this))
            .get(GithubUserListViewModel::class.java)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.recyclerGithubList.addItemDecoration(decoration)

        // bind the state
        binding.bindState(
            userListUiState = viewModel.state,
            uiActions = viewModel.accept
        )

    }

    /**
     * Binds the [userListUiState] provided  by the [GithubUserListViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun ActivityMainBinding.bindState(
        userListUiState: LiveData<UserListUiState>,
        uiActions: (UserListUiAction) -> Unit
    ) {

        val userAdapter = UserListAdapter()
        recyclerGithubList.adapter = userAdapter

        bindList(
            userListAdapter = userAdapter,
            uiState = userListUiState,
            onScrollChanged = uiActions
        )

    }
    private fun ActivityMainBinding.bindList(
        userListAdapter: UserListAdapter,
        uiState: LiveData<UserListUiState>,
        onScrollChanged: (UserListUiAction.Scroll) -> Unit
    ) {

        setupScrollListener(onScrollChanged)

        uiState
            .map(UserListUiState::listResult)
            .distinctUntilChanged()
            .observe(this@GithubUserListActivity) { result ->
                when (result) {

                    is GithubUserListResult.Success -> {
                        userListAdapter.submitList(result.data)
                    }

                    is GithubUserListResult.Error -> {
                        Toast.makeText(
                            this@GithubUserListActivity,
                            "Wooops $result}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            }

    }

    private fun ActivityMainBinding.setupScrollListener(
        onScrollChanged: (UserListUiAction.Scroll) -> Unit
    ) {

        val layoutManager = recyclerGithubList.layoutManager as LinearLayoutManager
        recyclerGithubList.addOnScrollListener(object : OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                onScrollChanged(
                    UserListUiAction.Scroll(
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisibleItem,
                        totalItemCount = totalItemCount
                    )
                )

            }

        } )

    }

}
