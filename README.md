# walt-siwe

### Parser and serializer for Eip4361 messages
```kotlin
val msg = Eip4361Message.fromString(
    """
    service.org wants you to sign in with your Ethereum account:
    0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2

    I accept the Service terms: https://service.org/tos

    URI: https://service.org/login
    Version: 1
    Chain ID: 1
    Nonce: 32891756
    Issued At: 2021-09-30T16:25:24Z
    Resources:
    - ipfs://bafybeiemxf5abjwjbikoz4m.../
    - https://example.com/my-web2-claim.json
    """.trimIndent()
)
```
