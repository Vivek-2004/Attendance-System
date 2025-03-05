package com.nshm.attendancesystem

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private val retrofit = Retrofit
    .Builder()
    .baseUrl("https://registrationsystem-1a4m.onrender.com/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val attendanceService: ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("users/verify/{id}")
    suspend fun getScanQr(
        @Path("id") id: String
    ): Response<ScanResponse>

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): User

    @POST("users/update/{id}")
    suspend fun updateUserInfo(
        @Path("id") id: String,
        @Body userInfo:User
    ): Response<ResponseBody>

    @GET("users")
    suspend fun getRegisteredStudents(): List<User>

    @POST("register")
    suspend fun registerUser(
        @Body userRegistration: RegistrationData
    ): ApiResponse

    @POST("send-seminar-pass-email")
    suspend fun sendMail(
        @Body collegeId: SendMailBody
    ): Response<ResponseBody>
}