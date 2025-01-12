#Java integration for Adyen Payment System API

[Adyen](http://www.adyen.com)
> is the leading technology provider powering payments for global commerce in the 21st century.
> With a seamless solution for mobile, online and in-store transactions, our technology enables merchants to accept almost any
> type of payment, anywhere in the world.

Adyen Payment System, **APS**, lies at the heart of its payment platform and it's the service a merchant integrates with for
payment processing.

**adyen-api** aims to be a cohesive and opinionated way for consuming APS' JSON messaging based services.

##Acknowledgements
* [Chrischy](https://github.com/Golddragon152) - Proxy configuration and CSE

## Milestones
* 1.0.0 - Initial
* 1.2.1 - Added support for proxy configuration
* 1.3.0 - Added support for CSE
* 1.3.1 - Added encryption errors when encryptionKey is defined
* 1.3.2 - Migrated endpoints to v18

##Current version and Maven dependency

```xml
    <dependency>
      <groupId>com.github.woki</groupId>
      <artifactId>payments-adyen-api</artifactId>
      <version>1.3.2</version>
    </dependency>
```
See also this [Sample Client](http://github.com/woki/adyen-client) sample built upon **ayden-api**.
It takes an authorization or modification request in YAML format and communicates with APS.

##Usage

###Client instantiation
```java
   Client client = Client
      .endpoint("https://pal-test.adyen.com")
      // .endpoint(APUtil.TEST_ENDPOINT)
      // .endpoint("https://pal-live.adyen.com")
      // .endpoint(APUtil.LIVE_ENDPOINT)
      .credentials(username, password)
      .build();
```
In case you are behind a proxy just add .proxyConfig() to the composition, as follows
```java
   Client client = Client
      .endpoint("https://pal-test.adyen.com")
      .credentials(username, password)
      .proxyConfig("prxyusr:prxypass@prxysrvr:8888")
      .build();
```
Notice that authentication is optional. For the example above the proxy configuration descriptor would then be like
this: prxysrvr:8888. Either names and IP addresses can be used as the host name.

####CSE - Client Side Encryption
For Adyen's CSE documentation and usage refer to [CSE Documentation](https://docs.adyen.com/developers/easy-encryption). Once you have generated a RSA public key
just instantiate the Client like this:
```java
   Client client = Client
      .endpoint("https://pal-test.adyen.com")
      .credentials(username, password)
      .encryptionKey("10001|FBF867B24626DE756...") // key abbreviated for clarity sake
      .build();
```
The Client will encrypt sensitive card information according to CSE specifications in case there's an encryption key defined.

###Authorisation
```java
   PaymentRequest request = PaymentRequestBuilder
      .merchantAccount(merchantAccount)
      .amount(new Amount(Currency.getInstance("EUR"), 1000L))
      .card(CardBuilder.number("4111111111111111").cvc("737").expiry(2016, 6).holder("Johnny Tester Visa").build())
      .reference(reference(ReferenceType.UUID))
      .shopper(NameBuilder.first("Willian").last("Oki").build(), "willian.oki@gmail.com", "127.0.0.1",
          "Test/DAPI/Authorisation/Willian Oki", ShopperInteraction.Ecommerce)
      .build();
   PaymentResponse response = client.authorise(request);
```

###Capture
```java
   ModificationRequest captureRequest = ModificationRequestBuilder
      .merchantAccount(merchantAccount)
      .modificationAmount(new Amount(Currency.getInstance("EUR"), 1000L))
      .originalReference(paymentResponse.getPspReference())
      .reference(reference(ReferenceType.UUID))
      .build();
   ModificationResponse captureResponse = client.capture(captureRequest);
```

###Cancel/Refund
```java
   ModificationRequest cancelRequest = ModificationRequestBuilder
      .merchantAccount(merchantAccount)
      .originalReference(paymentResponse.getPspReference())
      .reference(reference(ReferenceType.UUID))
      .build();
   ModificationResponse cancelResponse = client.cancel(cancelRequest);
```
```java
   ModificationRequest refundRequest = ModificationRequestBuilder
      .merchantAccount(merchantAccount)
      .modificationAmount(new Amount(Currency.getInstance("EUR"), 1000L))
      .originalReference(paymentResponse.getPspReference())
      .reference(reference(ReferenceType.UUID))
      .build();
   ModificationResponse refundResponse = client.refund(refundRequest);
```
```java
   ModificationRequest cancelOrRefundRequest = ModificationRequestBuilder
       .merchantAccount(merchantAccount)
       .originalReference(paymentResponse.getPspReference())
       .reference(reference(ReferenceType.UUID))
       .build();
   ModificationResponse cancelOrRefundResponse = client.cancelOrRefund(cancelOrRefundRequest);
```
