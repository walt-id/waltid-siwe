package id.walt.siwe.eip4361

object Eip4361MessageSerializer {
    fun toString(msg: Eip4361Message): String = msg.run {
        """
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
    }
}
