package id.walt.siwe.eip4361

object Eip4361MessageParser {

    private fun throwMissing(arg: String): Nothing = throw IllegalArgumentException("$arg is missing from request!")

    fun fromString(message: String): Eip4361Message {
        val msgLines = message.lines()

        // 1/3 Parse fixed ones
        val host: String = msgLines[0].removeSuffix(" wants you to sign in with your Ethereum account:")
        val address: String = msgLines[1]
        val description: String = msgLines[3]

        // 2/3 Parse general key-value pairs
        var uri: String? = null
        var version: Int? = null
        var chainId: Int? = null
        var nonce: String? = null
        var issuedAt: String? = null
        var expirationTime: String? = null
        var notBefore: String? = null
        var requestId: String? = null

        msgLines.forEach {
            if (it.contains(": ")) {
                val splitted = it.split(": ")
                val key = splitted[0]
                val value = splitted[1]

                when (key) {
                    "URI" -> uri = value
                    "Version" -> version = value.toInt()
                    "Chain ID" -> chainId = value.toInt()
                    "Nonce" -> nonce = value
                    "Issued At" -> issuedAt = value
                    "Expiration Time" -> expirationTime = value
                    "Not Before" -> notBefore = value
                    "Request ID" -> requestId = value
                }
            }
        }

        // 3/3 Parse resources
        var resources: ArrayList<String>? = null

        val resourcesIndicator = msgLines.indexOf("Resources:")

        if (resourcesIndicator != -1) {
            resources = ArrayList()
            msgLines.subList(resourcesIndicator + 1, msgLines.size).map { it.removePrefix("- ") }.forEach {
                resources.add(it)
            }
        }

        // Check for required attributes
        when {
            uri == null -> throwMissing("URI")
            version == null -> throwMissing("Version")
            chainId == null -> throwMissing("Chain ID")
        }


        return Eip4361Message(
            host = host,
            address = address,
            description = description,
            uri = uri!!,
            nonce = nonce,
            version = version!!,
            chainId = chainId!!,
            issuedAt = issuedAt,
            expirationTime = expirationTime,
            notBefore = notBefore,
            requestId = requestId,
            resources = resources
        )
    }

}
