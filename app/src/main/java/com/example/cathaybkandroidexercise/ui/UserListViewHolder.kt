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
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cathaybkandroidexercise.R
import com.example.cathaybkandroidexercise.model.User
import android.widget.Toast
import com.example.cathaybkandroidexercise.EXTRA_USERNAME


/**
 * View Holder for a [User] RecyclerView list item.
 */
class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), UserListContract.UserItemView {

    private val textViewLogin: TextView = itemView.findViewById(R.id.textview_login)
    private val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageview_avatar)
    private val imageViewSiteAdmin: ImageView = itemView.findViewById(R.id.site_admin)

    private lateinit var user: User
    private val context: Context = itemView.context
    private var numberOfItems = 0

    init {

        itemView.setOnClickListener {

            Toast.makeText(context, "Number of item: $numberOfItems", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, UserDetailActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, user.login)
            }
            itemView.context.startActivity(intent)

        }

    }

    override fun bindData(user: User, numberOfItems: Int) {

        this.numberOfItems = numberOfItems
        this.user = user
        textViewLogin.text = user.login
        if(user.siteAdmin){
            imageViewSiteAdmin.visibility = View.VISIBLE
        }else{
            imageViewSiteAdmin.visibility = View.GONE
        }

        Glide.with(context)
            .load(user.avatarUrl)
            .circleCrop()
            .into(imageViewAvatar)

    }

}