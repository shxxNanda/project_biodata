import java.io.Serializable

data class DataModel(
    val name: String,
    val gender: String,
    val placeOfBirth: String,
    val dateOfBirth: String,
    val address: String
) : Serializable {

    // Method to return a formatted String representation of the object
    fun getFormattedData(): String {
        return "Name: $name\nGender: $gender\nPlace of Birth: $placeOfBirth\nDate of Birth: $dateOfBirth\nAddress: $address"
    }
}
