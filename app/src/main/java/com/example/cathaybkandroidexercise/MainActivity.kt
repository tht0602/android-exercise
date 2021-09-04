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

package com.example.cathaybkandroidexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.json.JSONArray
import com.example.cathaybkandroidexercise.data.GithubListUserData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    val githubListUser: MutableList<GithubListUserData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onResume() {
        super.onResume()
        val githubDataMethod = GithubDataMethod()

        lifecycleScope.launch {
            val usersListJsonStr:String = githubDataMethod.sendGetUsersList(0, 20)
            var testStr = usersListJsonStr
            while (testStr.isNotBlank()) { //for test
                println(testStr)
                testStr = testStr.drop(4000);
            }

            githubListJsonParse(usersListJsonStr)

        }
    }

    fun githubListJsonParse(jsonStr: String){
        val listType = object : TypeToken<ArrayList<GithubListUserData>>(){}.type
        val githubListUser = Gson().fromJson<ArrayList<GithubListUserData>>(jsonStr, listType)

        val length = githubListUser.size
        println("length = $length")
        for(obj in githubListUser){
            println(obj.login)
        }
        this.githubListUser.addAll(githubListUser)

    }
}