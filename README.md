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
kas build kas-stm32mp15x.yml
```

or using kas docker container:

```
kas-container build kas-stm32mp15x.yml
```

## Flash & Run

Flash image on a SD Card using [bmap-tools](https://github.com/intel/bmap-tools):


```
sudo bmaptool copy build/tmp/deploy/images/stm32mp157f-dk2/stm32mp15x-demo-image-stm32mp157f-dk2.wic.gz /dev/mmcblk0
```

Insert your SD Card into the compatible device and you should see in your serial console:

```
NOTICE:  CPU: STM32MP157FAC Rev.Z
NOTICE:  Model: STMicroelectronics STM32MP157F-DK2 Discovery Board
NOTICE:  Board: MB1272 Var4.0 Rev.C-02
NOTICE:  BL2: v2.6-stm32mp1-r2.0(release):v2.6-stm32mp-r2-dirty(4b6e8e9b)
NOTICE:  BL2: Built : 13:55:00, Oct 20 2022
NOTICE:  BL2: Booting BL32
optee optee: OP-TEE: revision 3.16 (6c446f21)


U-Boot 2021.10-stm32mp-r2 (Oct 17 2022 - 08:48:26 +0000)

CPU: STM32MP157FAC Rev.Z
Model: STMicroelectronics STM32MP157F-DK2 Discovery Board
Board: stm32mp1 in trusted mode (st,stm32mp157f-dk2)
Board: MB1272 Var4.0 Rev.C-02
DRAM:  512 MiB
optee optee: OP-TEE: revision 3.16 (6c446f21)
Clocks:
- MPU : 800 MHz
- MCU : 208.878 MHz
- AXI : 266.500 MHz
- PER : 24 MHz
- DDR : 533 MHz
WDT:   Started with servicing (32s timeout)
NAND:  0 MiB
MMC:   STM32 SD/MMC: 0, STM32 SD/MMC: 1
Loading Environment from MMC... *** Warning - bad CRC, using default environment

In:    serial
Out:   serial
Err:   serial
Net:   eth0: ethernet@5800a000
Hit any key to stop autoboot:  0 
8182000 bytes read in 463 ms (16.9 MiB/s)
## Loading kernel from FIT Image at c8000000 ...
   Using 'conf-stm32mp157f-dk2.dtb' configuration
   Trying 'kernel-1' kernel subimage
     Description:  Linux kernel
     Created:      2022-10-26   8:41:24 UTC
     Type:         Kernel Image
     Compression:  uncompressed
     Data Start:   0xc8000118
     Data Size:    8061152 Bytes = 7.7 MiB
     Architecture: ARM
     OS:           Linux
     Load Address: 0xc2000000
     Entry Point:  0xc2000000
     Hash algo:    sha256
     Hash value:   ec7b197dde76b982a1381e64fa1a7eb854c90be8cb6be154fd8e3c3fc4438c06
   Verifying Hash Integrity ... sha256+ OK
## Loading fdt from FIT Image at c8000000 ...
   Using 'conf-stm32mp157f-dk2.dtb' configuration
   Trying 'fdt-stm32mp157f-dk2.dtb' fdt subimage
     Description:  Flattened Device Tree blob
     Created:      2022-10-26   8:41:24 UTC
     Type:         Flat Device Tree
     Compression:  uncompressed
     Data Start:   0xc87b0308
     Data Size:    118879 Bytes = 116.1 KiB
     Architecture: ARM
     Load Address: 0xc4000000
     Hash algo:    sha256
     Hash value:   90eb0bd39bf80781b7dd933c47429ecf17f948ad2e3bf84f733cb93a835f3f5f
   Verifying Hash Integrity ... sha256+ OK
   Loading fdt from 0xc87b0308 to 0xc4000000
   Booting using the fdt blob at 0xc4000000
   Loading Kernel Image
   Loading Device Tree to cffdf000, end cffff05e ... OK
Bootstage space exhasuted

Starting kernel ...


Poky (Yocto Project Reference Distro) 4.0.8 stm32mp157f-dk2 ttySTM0

stm32mp157f-dk2 login: root
root@stm32mp157f-dk2:~#
```

## List of tested boards:

* [stm32mp157c-dk2](https://www.st.com/en/evaluation-tools/stm32mp157c-dk2.html)
* [stm32mp157f-dk2](https://www.st.com/en/evaluation-tools/stm32mp157f-dk2.html)

## Contributing

If you want to contribute changes, you can send Github pull requests at:
https://github.com/embetrix/meta-stm32mp15x/pulls


## Maintainers

 - Ayoub Zaki <info@embetrix.com>
