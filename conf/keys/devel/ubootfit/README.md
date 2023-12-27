# fitImage Sign Key

> **Warning**

> The present key **ubootfit.key** is just given as an example and should not be used for production !

Generate you own key using *openssl*:

```
mkdir -p /path/to/ubootfit
cd /path/to/ubootfit
openssl req -x509 -days 7200 -newkey rsa:2048 -keyout ubootfit.key -out ubootfit.crt -subj "/O=Embetrix /CN=ubootfit"
```

Set the new Key Bitbake Variable in your local.conf:

```
UBOOT_SIGN_KEYDIR  = "/path/to/ubootfit"
```

> **Note**

We recommand strongly to use an HSM to enable secure boot for serious products.
