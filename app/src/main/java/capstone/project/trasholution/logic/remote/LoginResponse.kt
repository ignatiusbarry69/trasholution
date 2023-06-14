package capstone.project.trasholution.logic.remote

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: Data

)

data class Data(

    @field:SerializedName("token")
    val token: String,

    val username: String

)
