package id.walt.siwe

@kotlinx.serialization.Serializable
data class WaltSiweRequest(
    val message: String,
    val signature: String
)
