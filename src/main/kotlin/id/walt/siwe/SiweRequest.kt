package id.walt.siwe

@kotlinx.serialization.Serializable
data class SiweRequest(
    val message: String,
    val signature: String
)
