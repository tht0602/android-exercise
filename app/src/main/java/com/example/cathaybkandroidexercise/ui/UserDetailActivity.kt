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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cathaybkandroidexercise.EXTRA_USERNAME
import com.example.cathaybkandroidexercise.R
import com.example.cathaybkandroidexercise.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.coroutines.launch

class UserDetailActivity : AppCompatActivity(), UserDetailContract.View {

    private lateinit var presenter: UserDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val queryUserName = this.intent.getStringExtra(EXTRA_USERNAME)

        imageview_detail_x.setOnClickListener {
            this.finish()
        }

        setPresenter(UserDetailPresenter())

        lifecycleScope.launch{
            queryUserName?.let { presenter.loadDetail(it) }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun setPresenter(presenter: UserDetailContract.Presenter) {
        this.presenter = presenter
        presenter.attach(this)
    }

    override fun onLoadedDetail(user: User) {

        //Show all the details on view
        with(user){

            textview_detail_username.text = name
            textview_detail_bio.text = bio
            textview_detail_login.text = login
            if(siteAdmin) {
                textview_detail_admin.visibility = View.VISIBLE
            }
            textview_detail_location.text = location
            textview_detail_link.text = blog

            Glide
                .with(this@UserDetailActivity)
                .load(avatarUrl)
                .circleCrop()
                .into(imageview_detail_avatar)

        }


    }

    override fun onLoadedDetailError(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

}