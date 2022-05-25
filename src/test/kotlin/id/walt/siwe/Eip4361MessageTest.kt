package id.walt.siwe

import id.walt.siwe.eip4361.Eip4361Message
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class Eip4361MessageTest : StringSpec({
    lateinit var msg: Eip4361Message

    "Parse EIP-4361 message" {
        // Message example from https://eips.ethereum.org/EIPS/eip-4361
        msg = Eip4361Message.fromString(
            """
            service.org wants you to sign in with your Ethereum account:
            0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2

            I accept the ServiceOrg Terms of Service: https://service.org/tos

            URI: https://service.org/login
            Version: 1
            Chain ID: 1
            Nonce: 32891756
            Issued At: 2021-09-30T16:25:24Z
            Resources:
            - ipfs://bafybeiemxf5abjwjbikoz4mc3a3dla6ual3jsgpdr4cjr3oz3evfyavhwq/
            - https://example.com/my-web2-claim.json
            """.trimIndent()
        )

        msg.host shouldBe "service.org"
        msg.address shouldBe "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2"
        msg.description shouldBe "I accept the ServiceOrg Terms of Service: https://service.org/tos"
        msg.uri shouldBe "https://service.org/login"
        msg.version shouldBeExactly 1
        msg.chainId shouldBeExactly 1
        msg.nonce shouldBe "32891756"
        msg.issuedAt shouldBe "2021-09-30T16:25:24Z"

        msg.resources shouldNotBe null

        msg.resources!! shouldHaveSize 2
        msg.resources!! shouldContain "ipfs://bafybeiemxf5abjwjbikoz4mc3a3dla6ual3jsgpdr4cjr3oz3evfyavhwq/"
        msg.resources!! shouldContain "https://example.com/my-web2-claim.json"
    }

    "Serialize EIP-4361 message" {
        val serialized = msg.toString()

        val serializedPre = """
            service.org wants you to sign in with your Ethereum account:
            0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2

            I accept the ServiceOrg Terms of Service: https://service.org/tos

            URI: https://service.org/login
            Version: 1
            Chain ID: 1
            Nonce: 32891756
            Issued At: 2021-09-30T16:25:24Z
            Resources:
            - ipfs://bafybeiemxf5abjwjbikoz4mc3a3dla6ual3jsgpdr4cjr3oz3evfyavhwq/
            - https://example.com/my-web2-claim.json
        """.trimIndent()

        serialized shouldBe serializedPre
    }
})
