package id.walt.siwe

import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.crypto.Sign.SignatureData
import org.web3j.utils.Numeric

object Web3jSignatureVerifier {

    fun verifySignature(address: String, signedMessage: String, originalMessage: String): Boolean {
        val decryptedAddress = "0x${getAddressUsedToSignHashedPrefixedMessage(signedMessage, originalMessage)}"
        return address == decryptedAddress
    }

    private fun getAddressUsedToSignHashedPrefixedMessage(signedHash: String, originalMessage: String): String {
        val r = signedHash.substring(0, 66)
        val s = "0x" + signedHash.substring(66, 130)
        val v = "0x" + signedHash.substring(130, 132)

        val publicKey = Sign.signedPrefixedMessageToKey(
            originalMessage.toByteArray(), SignatureData(
                Numeric.hexStringToByteArray(v)[0], Numeric.hexStringToByteArray(r), Numeric.hexStringToByteArray(s)
            )
        ).toString(16)

        return Keys.getAddress(publicKey)
    }
}
