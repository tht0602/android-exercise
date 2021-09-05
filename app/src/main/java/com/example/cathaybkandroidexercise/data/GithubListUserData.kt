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

package com.example.cathaybkandroidexercise.data

import com.google.gson.annotations.SerializedName

class GithubUserDetailData{

    @SerializedName("login")
    val login: String = ""

    @SerializedName("id")
    val id: Long = 0

    @SerializedName("avatar_url")
    val avatar_url: String = ""

    @SerializedName("site_admin")
    val site_admin: Boolean = true

    @SerializedName("url")
    val url: String = ""
}