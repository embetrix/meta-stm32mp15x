# meta-stm32mp15x

## Summary

**meta-stm32mp15x** is a yocto layer containing BSP support for the [stm32mp15x](https://wiki.st.com/stm32mpu/wiki/Category:STM32MP15x) based devices.

This layer relies on OpenEmbedded/Yocto build system and depends on:

```
[OECORE]
URI: https://git.yoctoproject.org/git/poky.git
layers: meta
branch: same dedicated branch as meta-stm32mp15x
```
and

```
[OE]
URI: https://github.com/openembedded/meta-openembedded.git
layers: meta-oe
branch: same dedicated branch as meta-stm32mp15x
```

## Build

This layer can be integrated in your layers or built standalone using [kas-tool](https://github.com/siemens/kas):

```
KAS_MACHINE=stm32mp157f-dk2 kas build kas-stm32mp15x.yml
```

or using kas docker container:

```
KAS_MACHINE=stm32mp157f-dk2 kas-container build kas-stm32mp15x.yml
```

## Flash SD Card

Flash image on a SD Card using [bmap-tools](https://github.com/intel/bmap-tools):


```
sudo bmaptool copy \
    build/tmp/deploy/images/stm32mp157f-dk2/stm32mp15x-demo-image-stm32mp157f-dk2.wic.gz \
    /dev/mmcblk0
```

## Run

Insert your SD Card into the compatible device and you should see in your serial console:

```
NOTICE:  CPU: STM32MP157FAC Rev.Z
NOTICE:  Model: STMicroelectronics STM32MP157F-DK2 Discovery Board
NOTICE:  Board: MB1272 Var4.0 Rev.C-03
NOTICE:  Bootrom authentication failed
NOTICE:  BL2: v2.8-stm32mp1-r1.1(release):v2.8-stm32mp-r1.1-3-g61924c04c-dirty(61924c04)
NOTICE:  BL2: Built : 13:07:51, Oct 18 2023
NOTICE:  TRUSTED_BOARD_BOOT support enabled
NOTICE:  ROTPK is not deployed on platform. Skipping ROTPK verification.
NOTICE:  ROTPK is not deployed on platform. Skipping ROTPK verification.
NOTICE:  BL2: Booting BL32
optee optee: OP-TEE: revision 3.19 (d0159bbf)


U-Boot 2022.10-stm32mp-r1.1 (Sep 18 2023 - 11:38:19 +0000)

CPU: STM32MP157FAC Rev.Z
Model: STMicroelectronics STM32MP157F-DK2 Discovery Board
Board: stm32mp1 in trusted mode (st,stm32mp157f-dk2)
Board: MB1272 Var4.0 Rev.C-03
DRAM:  512 MiB
optee optee: OP-TEE: revision 3.19 (d0159bbf)
Clocks:
- MPU : 800 MHz
- MCU : 208.878 MHz
- AXI : 266.500 MHz
- PER : 24 MHz
- DDR : 533 MHz
Core:  368 devices, 44 uclasses, devicetree: board
WDT:   Started watchdog with servicing (32s timeout)
NAND:  0 MiB
MMC:   STM32 SD/MMC: 0, STM32 SD/MMC: 1
Loading Environment from MMC... OK
In:    serial
Out:   serial
Err:   serial
Net:   eth0: ethernet@5800a000
Hit any key to stop autoboot:  0 
Saving Environment to MMC... Writing to MMC(0)... OK
6949886 bytes read in 420 ms (15.8 MiB/s)
## Loading kernel from FIT Image at c8000000 ...
   Using 'conf-stm32mp157f-dk2.dtb' configuration
   Verifying Hash Integrity ... sha256,rsa2048:ubootfit+ OK
   Trying 'kernel-1' kernel subimage
     Description:  Linux kernel
     Created:      2023-10-02  11:43:32 UTC
     Type:         Kernel Image
     Compression:  uncompressed
     Data Start:   0xc8000118
     Data Size:    6860504 Bytes = 6.5 MiB
     Architecture: ARM
     OS:           Linux
     Load Address: 0xc2000000
     Entry Point:  0xc2000000
     Hash algo:    sha256
     Hash value:   1c385609fd85f0b2800ff725517261c2122d3e4ab4aa38f7deb8af462040918f
   Verifying Hash Integrity ... sha256+ OK
## Loading fdt from FIT Image at c8000000 ...
   Using 'conf-stm32mp157f-dk2.dtb' configuration
   Verifying Hash Integrity ... sha256,rsa2048:ubootfit+ OK
   Trying 'fdt-stm32mp157f-dk2.dtb' fdt subimage
     Description:  Flattened Device Tree blob
     Created:      2023-10-02  11:43:32 UTC
     Type:         Flat Device Tree
     Compression:  uncompressed
     Data Start:   0xc868b100
     Data Size:    87138 Bytes = 85.1 KiB
     Architecture: ARM
     Load Address: 0xc4000000
     Hash algo:    sha256
     Hash value:   4acc0143bd2cc251ab55e9576e51b60ba9e5f767a734dac308aee309b7bf4d08
   Verifying Hash Integrity ... sha256+ OK
   Loading fdt from 0xc868b100 to 0xc4000000
   Booting using the fdt blob at 0xc4000000
   Loading Kernel Image
   Loading Device Tree to cffe7000, end cffff461 ... OK
Bootstage space exhasuted

Starting kernel ...


Poky (Yocto Project Reference Distro) 4.0.15 stm32mp157f-dk2 ttySTM0

stm32mp157f-dk2 login: root
root@stm32mp157f-dk2:~# 
```

## Secure Boot:

Secure Boot Signing of **tf-a** an **fip** binaries is enabled by default using a development key 

we recommend that you generate your own key using: 

```
openssl ecparam -name prime256v1 -genkey -out /path/to/secure-boot-mykey.pem
```

Set the new Key path in a Bitbake Variable put in your local.conf:

```
SECBOOT_SIGN_KEY = "/path/to/secure-boot-mykey.pem"
```

The hashes of public key are automatically generated under:

```
build/tmp/deploy/images/stm32mp157f-dk2/secureboot-pubhash.bin
```

The corresponding u-boot fuse prog commands are also generated and can be fused using:

```
build/tmp/deploy/images/stm32mp157f-dk2/u-boot-fuse-prog.txt
```

> **Note**

We provide services to integrate HSM signing to secure boot, extend it to the applications
and improve the overall security of your product.


## Wireless LAN

to enable Wifi Manually you should set */etc/wpa_supplicant.conf* file e.g:

```
network={
        ssid="embetrix-2.4"
        psk=315089e3db2ce18ad94caba8a3b71f35947487d1c7913a44130e7ec2f91905040
}
```

and start wpa_supplicant:

```
wpa_supplicant -c /etc/wpa_supplicant.conf -i wlan0 -B
```

you can then acquire an IP address using for example:

```
udhcpc -i wlan0
```

## List of tested boards:

* [stm32mp157c-dk2](https://www.st.com/en/evaluation-tools/stm32mp157c-dk2.html)
* [stm32mp157f-dk2](https://www.st.com/en/evaluation-tools/stm32mp157f-dk2.html)

## Contributing

If you want to contribute changes, you can send Github pull requests at:
https://github.com/embetrix/meta-stm32mp15x/pulls


## Maintainers

 - Ayoub Zaki <info@embetrix.com>
