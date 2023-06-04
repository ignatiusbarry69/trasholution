package capstone.project.trasholution.logic.repository.responses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Parcelize
data class PengepulResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
@Entity(tableName = "pengepul_entity")
data class DataItem(

	@field:SerializedName("contact")
	val contact: Int,

	@field:SerializedName("__v")
	val v: Int,

	@field:SerializedName("location")
	val location: String,

	@PrimaryKey
	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("createDate")
	val createDate: String
) : Parcelable
