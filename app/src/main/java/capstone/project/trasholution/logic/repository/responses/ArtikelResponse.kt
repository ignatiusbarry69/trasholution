package capstone.project.trasholution.logic.repository.responses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Parcelize
data class ArtikelResponse(

    @field:SerializedName("data")
    val data: List<ArticleAddItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable

//@Parcelize
//@Entity(tableName = "article_entity")
//data class ArticleItem(
//
//    @field:SerializedName("imgUrl")
//    val imgUrl: String,
//
//    @field:SerializedName("__v")
//    val v: Int,
//
//    @PrimaryKey
//    @field:SerializedName("_id")
//    val id: String,
//
//    @field:SerializedName("title")
//    val title: String,
//
//    @field:SerializedName("content")
//    val content: String,
//
//    @field:SerializedName("createDate")
//    val createDate: String,
//) : Parcelable

@Parcelize
@Entity(tableName = "article_entity")
data class ArticleAddItem(

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("jenisSampah")
    val jenisSampah: String? = null,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("imgUrl")
    val imgUrl: String,

    @PrimaryKey
    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("createDate")
    val createDate: String,

    @field:SerializedName("__v")
    val v: Int,
    ): Parcelable

