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
    val department: String,
    val contactNumber: Long,
    val whatsappNumber: Long,
    val isPresent: Boolean,
    val __v: Int,
    val token: String
)

data class RegistrationData(
    val name: String,
    val collegeEmail: String,
    val collegeId: Long,
    val year: String,
    val department: String,
    val contactNumber: Long,
    val whatsappNumber: Long
)

data class ApiRegisterResponse(
    val message: String,
    val user: User
)


/*"name": "Devbrat Pradhan",
"collegeEmail": "devbratpradhan.22@nshm.edu.in",
"collegeId": 22273030242,
"year": "3",
"department": "B.tech CSE",
"contactNumber": 6296398479,
"whatsappNumber": 6296398479,
"token": "a401b99cbd51519c596b91be6b1cb0bc32755f6760c5a4fdfb7c3fd2b3aa6c04b05dcb359dc3cfb99ae1bd62acaeca3e3bc43a7395de035defd3e4fa467c278363d0ea0eb76a0a55fa9ac4acc2f8d1e248097f3c54227efdfacfac448e2c3b5df69ea4ede32c27a7be5fddddbc9c2f81cfcd6e1b58305672f2f48ede2f1860057bab1c2097822de96345bdd715e9987e4653c66f7d6266829f71ae2e4e1de76156312c665d76a551c6b787a335d43a8344c4b61200b2cbe0f24fb1adc695dd8ce2c865439dad4cc959ed571e6c9c758af72b31705b606d84ce9a678d4acd8a886cf6f247f1dca86a57943a9e28a13256",
"isPresent": false,
"_id": "673657e202b945287f52ff1f",
"__v": 0
        */