# Kernel Module Sign Key

> **Warning**

> The present key **kmod-sign-key.pem** is just given as an example and should not be used for production !

Generate you own key using *openssl*:


```
openssl ecparam -name prime256v1 -genkey -out /path/to/kmod-sign-key.pem
openssl req -x509 -days 7200  -key kmod-sign-key.pem -out /path/to/kmod-sign-cert.pem -subj "/O=Embetrix /CN=kmod-sign"
```

Set the new Key Bitbake Variable in your local.conf:

```
KMOD_SIGN_KEY  = "/path/to/kmod-sign-key.pem"
KMOD_SIGN_CERT = "/path/to/kmod-sign-cert.pem"
```

> **Note**

We recommand strongly to use an HSM to enable secure boot for serious products.
