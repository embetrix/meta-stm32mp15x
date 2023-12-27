# Secure Boot Sign Key

> **Warning**

> The present key **secure-boot.pem** is just given as an example and should not be used for production !

Generate you own key using *openssl*:


```
openssl ecparam -name prime256v1 -genkey -out /path/to/secure-boot-mykey.pem
```

Set the new Key Bitbake Variable in your local.conf:

```
SECBOOT_SIGN_KEY = "/path/to/secure-boot-mykey.pem"
```

> **Note**

We recommand strongly to use an HSM to enable secure boot for serious products.
