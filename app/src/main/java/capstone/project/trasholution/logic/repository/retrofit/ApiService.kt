package capstone.project.trasholution.logic.repository.retrofit

import capstone.project.trasholution.logic.remote.LoginResponse
import capstone.project.trasholution.logic.remote.UserResponse
import capstone.project.trasholution.logic.repository.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/signup")
    fun registerUser(
        @Field("username") username: String,
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

    @GET("/pengepul")
    fun getLocation(): Call<PengepulResponse>

    @GET("/artikel")
    suspend fun getArtikel(): ArtikelResponse

    @FormUrlEncoded
    @POST("/pengepul/add")
    fun addPengepul(
        @Field("contact") contact: String,
        @Field("location") location: String,
        @Field("description") description: String,
        @Field("lat") lat: Double?,
        @Field("lon") lon: Double?,
    ) : Call <DataItem>

    @Multipart
    @POST("/artikel/add")
    fun addArtikel(
        @Part("title") title: RequestBody,
        @Part("jenisSampah") jenisSampah: RequestBody,
        @Part("content") content: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ArticleAddItem>

    @Multipart
    @POST("/predictImage")
    fun predict(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Call<ModelResponse>

    @GET("/pengepul/{username}")
    fun getMyData(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<PengepulResponse>

    @FormUrlEncoded
    @PUT("/pengepul/{id}")
    fun updatePengepul(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("contact") contact: String,
        @Field("location") location: String,
        @Field("description") description: String,
        @Field("lat") lat: Double?,
        @Field("lon") lon: Double?,
    ): Call<DataItem>

    @DELETE("/pengepul/{id}")
    fun deletePengepul(

        @Path("id") id: String
    ): Call<DataItem>
}