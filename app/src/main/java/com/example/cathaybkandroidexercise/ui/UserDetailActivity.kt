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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cathaybkandroidexercise.model.GithubDataMethod
import com.example.cathaybkandroidexercise.R
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.json.JSONObject

private const val EXTRA_USERNAME = "username"

private const val DEFAULT_USER = "mojombo"

class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        //get {username} from GithubUserListActivity or use DEFAULT_USER
        var queryUserName = DEFAULT_USER
        this.intent.getStringExtra(EXTRA_USERNAME)?.let { username ->
            queryUserName = username
        }

        imageview_detail_x.setOnClickListener {
            this.finish()
        }

        val githubDataMethod = GithubDataMethod()

        lifecycleScope.launch {

            val usersListJsonStr:String = githubDataMethod.sendGetUser(queryUserName)

            println(usersListJsonStr)

            githubListJsonParse(usersListJsonStr)

        }

    }

    private fun githubListJsonParse(jsonStr: String){

        val githubUserDetail = JSONObject(jsonStr)

        textview_detail_username.text = githubUserDetail.getString("name")

        textview_detail_bio.text = githubUserDetail.getString("bio")

        textview_detail_login.text = githubUserDetail.getString("login")

        if(githubUserDetail.getBoolean("site_admin")) {
            textview_detail_admin.visibility = View.VISIBLE
        }

        textview_detail_location.text = githubUserDetail.getString("location")

        textview_detail_link.text = githubUserDetail.getString("blog")

        Glide
            .with(this)
            .load(githubUserDetail.get("avatar_url"))
            .circleCrop()
            .into(imageview_detail_avatar)

    }

}