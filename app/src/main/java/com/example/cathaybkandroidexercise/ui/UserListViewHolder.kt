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

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cathaybkandroidexercise.R
import com.example.cathaybkandroidexercise.model.User

private const val EXTRA_USERNAME = "username"

/**
 * View Holder for a [User] RecyclerView list item.
 */
class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val texrViewLogin: TextView = view.findViewById(R.id.textview_login)
    private val imageViewAvatar: ImageView = view.findViewById(R.id.imageview_avatar)
    private val imageViewSiteAdmin: ImageView = view.findViewById(R.id.site_admin)

    private var user: User? = null
    private var context: Context? = null

    init {
        context = view.context
        view.setOnClickListener {

            //click then go to UserDetailActivity
            user?.login?.let { login ->
                val intent = Intent(context, UserDetailActivity::class.java).apply {
                    putExtra(EXTRA_USERNAME, login)
                }
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(user: User?) {
        if (user != null) {
            showUserData(user)
        }
    }

    private fun showUserData(user: User) {
        this.user = user
        texrViewLogin.text = user.login
        if(!user.siteAdmin){
            imageViewSiteAdmin.visibility = View.GONE
        }

        this.context?.let {
            Glide
                .with(it)
                .load(user.avatarUrl)
                .circleCrop()
                .into(imageViewAvatar)
        }
    }

    companion object {
        fun create(parent: ViewGroup): UserListViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.github_userlist_item, parent, false)
            return UserListViewHolder(view)
        }
    }
}