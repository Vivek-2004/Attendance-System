package com.nshm.attendancesystem

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

val attendanceService : ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("users/scan/{id}")
    suspend fun getScanBar(
        @Path("id") id: String
    ): ScanResponse

    @GET("users")
    suspend fun getRegisteredStudents(): List<User>

    @POST("register")
    suspend fun registerUser(
        @Body userRegistration: RegistrationData
    ): ApiRegisterResponse

    @POST("/qr-generate/{encryptedToken}")
    suspend fun qrCodeGenerator()
}