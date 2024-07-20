# fitImage Sign Key

> **Warning**

> The present key **ima_privkey.pem** is just given as an example and should not be used for production !

Generate you own key using *openssl*:

```
mkdir -p /path/to/imaevm
cd /path/to/imaevm
openssl genrsa -out imaevm-key.pem 2048
openssl req -x509 -days 7200  -key imaevm-key.pem -out  imaevm-cert.pem -subj "/O=Embetrix /CN=imaevm"
```

Set the new Key Bitbake Variable in your local.conf:

```
UBOOT_SIGN_KEYDIR  = "/path/to/ubootfit"
```

> **Note**

We recommand strongly to use an HSM to enable secure boot for serious products.


fw_setenv optargs "console=ttySTM0,115200 root=/dev/mmcblk0p4 rootflags=i_version ro rootwait loglevel=7 vt.global_cursor_default=0 fbcon=rotate:3 lsm=integrity ima_appraise=enforce ima_policy=appraise_tcb ima_template=ima-sig log_buf_len=2M"


fw_setenv optargs "console=ttySTM0,115200 root=/dev/mmcblk0p4 rootflags=i_version ro rootwait loglevel=7 vt.global_cursor_default=0 fbcon=rotate:3  lsm=integrity ima_policy=appraise_tcb log_buf_len=2M"


openssl x509 -in imaevm_cert.pem -outform DER  -out imaevm_cert.der

keyctl link @us @s
cat /etc/keys/kmk | keyctl padd user kmk @u
strace /bin/keyctl add encrypted evm-key "load `cat /etc/keys/evm-key`" @u
evmctl import /etc/keys/x509_ima.der $(keyctl newring _evm @u)
echo "1" > /sys/kernel/security/evm


keyctl add trusted kmk "load `cat /etc/keys/kmk-trusted.blob`" @u
keyctl add encrypted evm-key "load `cat /etc/keys/evm-trusted.blob`" @u

cat > ima_policy
dont_appraise fsmagic=0x9fa0
dont_appraise fsmagic=0x62656572
dont_appraise fsmagic=0x858458f6
dont_appraise fsmagic=0x64626720
dont_appraise fsmagic=0x01021994
dont_appraise fsmagic=0x73636673
appraise func=FILE_MMAP mask=MAY_EXEC appraise_type=imasig
appraise func=BPRM_CHECK appraise_type=imasig
appraise func=FIRMWARE_CHECK appraise_type=imasig
appraise fowner=0

cat ima_policy > /sys/kernel/security/ima/policy

