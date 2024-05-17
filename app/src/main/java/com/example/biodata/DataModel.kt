import android.os.Parcel
import android.os.Parcelable

data class DataModel(
    var id: String = "",
    val name: String,
    val gender: String,
    val placeOfBirth: String,
    val dateOfBirth: String,
    val address: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "") // Tambahkan konstruktor tanpa argumen

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(gender)
        parcel.writeString(placeOfBirth)
        parcel.writeString(dateOfBirth)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModel> {
        override fun createFromParcel(parcel: Parcel): DataModel {
            return DataModel(parcel)
        }

        override fun newArray(size: Int): Array<DataModel?> {
            return arrayOfNulls(size)
        }
    }
}