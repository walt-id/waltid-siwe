package id.walt

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
    val nonce: String, // "ievBV6BahTf1dHOgA"
    val version: Int = 1,
    val chainId: Int = 1,
    val issuedAt: String? = null, // "2022-05-25T10:20:16.449Z"
    val expirationTime: String? = null,
    val notBefore: String? = null,
    val requestId: String? = null,
    val resources: List<String>? = null
) {
    override fun toString(): String = """
            |$host wants you to sign in with your Ethereum account:
            |$address
            |
            |$description
            |
            |URI: $uri
            |Version: $version
            |Chain ID: $chainId
            |Nonce: $nonce
            |${if (issuedAt != null) "Issued At: $issuedAt" else "--empty--"}
            |${if (expirationTime != null) "Expiration Time: $expirationTime" else "--empty--"}
            |${if (notBefore != null) "Not Before: $notBefore" else "--empty--"}
            |${if (requestId != null) "Request ID: $requestId" else "--empty--"}
            |${if (resources != null) "Resources:\n" + resources.joinToString(separator = "\n") { "|- $it" } else ""}
        """.trimMargin().replace("\n--empty--", "").trimEnd('\n')

    companion object {
        private fun throwMissing(arg: String): Nothing = throw IllegalArgumentException("$arg is missing from request!")

        fun fromString(msg: String): Eip4361Message {
            val lines = msg.lines()

            // 1/3 Parse fixed ones
            val host: String = lines[0].removeSuffix(" wants you to sign in with your Ethereum account:")
            val address: String = lines[1]
            val description: String = lines[3]

            // 2/3 Parse general key-value pairs
            var uri: String? = null
            var version: Int? = null
            var chainId: Int? = null
            var nonce: String? = null
            var issuedAt: String? = null
            var expirationTime: String? = null
            var notBefore: String? = null
            var requestId: String? = null

            msg.lines().forEach {
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

            val resourcesIndicator = lines.indexOf("Resources:")

            if (resourcesIndicator != -1) {
                resources = ArrayList()
                lines.subList(resourcesIndicator + 1, lines.size).map { it.removePrefix("- ") }.forEach {
                    resources.add(it)
                }
            }

            // Check for required attributes
            when {
                uri == null -> throwMissing("URI")
                nonce == null -> throwMissing("Nonce")
                version == null -> throwMissing("Version")
                chainId == null -> throwMissing("Chain ID")
            }


            return Eip4361Message(
                host = host,
                address = address,
                description = description,
                uri = uri!!,
                nonce = nonce!!,
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
}
