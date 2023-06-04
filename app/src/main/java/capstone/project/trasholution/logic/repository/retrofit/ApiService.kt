package capstone.project.trasholution.logic.repository.retrofit

import capstone.project.trasholution.logic.remote.LoginResponse
import capstone.project.trasholution.logic.remote.UserResponse
import capstone.project.trasholution.logic.repository.responses.ArtikelResponse
import capstone.project.trasholution.logic.repository.responses.PengepulResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/signup")
    fun registerUser(
        @Field("/username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call <UserResponse>

    @FormUrlEncoded
    @POST("/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call <LoginResponse>

    @GET("/pengepul")
    suspend fun getPengepul(): PengepulResponse

    @GET("/artikel")
    suspend fun getArtikel(): ArtikelResponse
}