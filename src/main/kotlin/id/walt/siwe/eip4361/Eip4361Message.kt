package id.walt.siwe.eip4361

/*
${domain} wants you to sign in with your Ethereum account:
${address}

${statement}

URI: ${uri}
Version: ${version}
Chain ID: ${chain-id}
Nonce: ${nonce}
Issued At: ${issued-at}
Expiration Time: ${expiration-time}
Not Before: ${not-before}
Request ID: ${request-id}
Resources:
- ${resources[0]}
- ${resources[1]}
...
- ${resources[n]}
 */
data class Eip4361Message(
    val host: String, // "localhost:8080"
    val address: String, // "0x29c480ee4b8DE1ec0578B4A117B32d52DC1A22ED"
    val description: String, // "Sign in with Ethereum to the app."
    val uri: String, // "http://localhost:8080"
    val nonce: String? = null, // "ievBV6BahTf1dHOgA"
    val version: Int = 1,
    val chainId: Int = 1,
    val issuedAt: String? = null, // "2022-05-25T10:20:16.449Z"
    val expirationTime: String? = null,
    val notBefore: String? = null,
    val requestId: String? = null,
    val resources: List<String>? = null
) {
    override fun toString(): String = Eip4361MessageSerializer.toString(this)

    companion object {
        fun fromString(message: String): Eip4361Message = Eip4361MessageParser.fromString(message)
    }
}
