/*
 * Copyright (C) 2020 The Android Open Source Project
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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cathaybkandroidexercise.R
import com.example.cathaybkandroidexercise.model.User
import kotlinx.android.synthetic.main.activity_user_detail.*

private const val EXTRA_USERNAME = "username"

class UserDetailActivity : AppCompatActivity(), UserDetailContract.View {

    private lateinit var presenter: UserDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val queryUserName = this.intent.getStringExtra(EXTRA_USERNAME)

        imageview_detail_x.setOnClickListener {
            this.finish()
        }

        queryUserName?.let {
            setPresenter(UserDetailActivityPresenter(this, lifecycle, it))
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setPresenter(presenter: UserDetailContract.Presenter) {
        this.presenter = presenter
    }

    override fun onLoadedDetail(user: User) {

        textview_detail_username.text = user.name
        textview_detail_bio.text = user.bio
        textview_detail_login.text = user.login
        if(user.siteAdmin) {
            textview_detail_admin.visibility = View.VISIBLE
        }
        textview_detail_location.text = user.location
        textview_detail_link.text = user.blog

        Glide
            .with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .into(imageview_detail_avatar)

    }

    override fun onLoadedDetailError(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

}