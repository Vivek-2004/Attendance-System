package com.nshm.attendancesystem

data class ScanResponse(
    val message: String,
    val user: User
)

data class User(
    val _id: String,
    val name: String,
    val collegeEmail: String,
    val collegeId: Long,
    val year: String,
    val contactNumber: Long,
    val whatsappNumber: Long,
    val isPresent: Boolean,
    val __v: Int
)

data class UserList(
    val StudentsList:List<User>
)