# SIWE by walt.id

Kotlin implementation of __EIP-4361 Sign-In with Ethereum__.

# Demo

A SIWE demo is deployed here: https://siwe.walt.id

For running SIWE yourself use _Docker_:

__Frontend__ will start at http://localhost:3000/ (works without backend since test-system is configured):

    docker run -it -p 3000:80 waltid/siwe-web-test

__Backend__ will start at http://0.0.0.0:7000/ and expose the endpoints at [/api/nonce](http://0.0.0.0:7000/api/nonce), [/api/verify](http://0.0.0.0:7000/api/verify), [/api/personal_information](http://0.0.0.0:7000/api/personal_information) and [/api/logout](http://0.0.0.0:7000/api/logout):

    docker run -it -p 7000:7000 waltid/siwe

# Usage

### Add dependency

Include the Maven repo: __https://maven.walt.id/repository/waltid/__ in your build config.

_Gradle_

        implementation("id.walt:waltid-siwe:0.1.0")

_Maven_

        <dependency>
            <groupId>id.walt</groupId>
            <artifactId>waltid-siwe</artifactId>
            <version>0.1.0</version>
        </dependency>

### Parser and serializer for EIP-4361 messages
use `id.walt.siwe.Eip4361Message`

#### Parse EIP-4361 message
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

#### Serialize EIP-4361 message
```kotlin
val msg = Eip4361Message(
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

println(msg.toString())
```

## Build & Run with Docker

### Backend

    docker build -t waltid/siwe .
    docker run -it -p 7000:7000 waltid/siwe

### Frontend

    docker build -f Dockerfile-web-test -t waltid/siwe-web-test .
    docker run -it -p 3000:80 waltid/siwe-web-test

## Build & Run natively (for dev)

### Backend

Make sure your have JVM 16 or higher installed.

    gradle clean build
    tar xf build/distributions/waltid-siwe-0.0.1.tar
    ./waltid-siwe-0.0.1/bin/waltid-siwe

### Frontend

Make sure you have Node v16 or higher installed.

    cd src/main/web
    yarn install
    yarn dev

#### Config

Switch backend address by setting the .env file:
   
    yarn dev --dotenv .env-dev

## Deploy to Kubernetes

Replace hostname "walt-test.cloud" in file k8s/deployment-siwe-dev.yaml with yours and run:

    kubectl -n dev apply -f deployment-siwe-dev.yaml

## Test System

The current snapshot for testing is deployed here: https://siwe.walt-test.cloud/
