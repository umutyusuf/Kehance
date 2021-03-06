package com.cutta.kehance.data.remote

import com.cutta.kehance.data.remote.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by CuneytCarikci on 10/01/2018.
 */

interface KehanceService {

    @GET("projects")
    fun getProjects(@Query("api_key") apiKey: String): Single<ProjectList>

    @GET("projects/{project_id}")
    fun getProjectWithId(@Path("project_id") id: Int, @Query("api_key") apiKey: String): Single<ProjectDetail>

    @GET("projects/{project_id}/comments")
    fun getComments(@Path("project_id") id: Int, @Query("api_key") apiKey: String): Single<Comments>

    @GET("users/{user_id}")
    fun getUser(@Path("user_id") id: Int, @Query("api_key") apiKey: String): Single<UserInfo>

    @GET("users/{user_id}/projects")
    fun getUserProject(@Path("user_id") id: Int, @Query("api_key") apiKey: String): Single<ProjectList>


}