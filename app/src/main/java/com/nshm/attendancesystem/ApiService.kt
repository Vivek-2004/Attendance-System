package com.nshm.attendancesystem

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val retrofit = Retrofit
    .Builder()
    .baseUrl("https://registrationsystem-1a4m.onrender.com/api/users/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val attendanceService : ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("scan/{id}")
    suspend fun getScanBar(
        @Path("id") id: String
    ): ScanResponse
}