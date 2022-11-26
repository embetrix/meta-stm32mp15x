#@TYPE: Machine
#@NAME: generic stm32mp15x
#@DESCRIPTION: Machine configuration for generic stm32mp15x
#@MAINTAINER: Ayoub Zaki <ayoub.zaki@embetrix.com>

require conf/machine/include/arm/armv7a/tune-cortexa7.inc
require conf/machine/include/soc-family.inc

DEFAULTTUNE = "cortexa7thf-neon-vfpv4"

SOC_FAMILY = "stm32mp15x"

PREFERRED_PROVIDER_virtual/kernel     = "linux-stm32"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-stm32"
PREFERRED_PROVIDER_u-boot             = "u-boot-stm32"

UBOOT_SUFFIX     = "stm32"
UBOOT_ENTRYPOINT = "0xC2000000"
UBOOT_DTB_LOADADDRESS = "0xC4000000"

SERIAL_CONSOLES       = "115200;ttySTM0"
SERIAL_CONSOLES_CHECK = "${SERIAL_CONSOLES}"

KERNEL_CLASSES  += "kernel-fitimage"
KERNEL_IMAGETYPE = "fitImage"

# Trused Firmware
TFA_DEVICETREE    ?= "stm32mp157f-dk2"

# Kernel
KERNEL_DEVICETREE ?= "stm32mp157f-dk2.dtb"

# U-Boot
UBOOT_MACHINE     ?= "stm32mp15_trusted_defconfig"
UBOOT_DEVICETREE  ?= "stm32mp157f-dk2"
UBOOT_MAKE_TARGET ?= "DEVICE_TREE=${UBOOT_DEVICETREE} all"

MACHINE_FEATURES  = "wifi"

# Preferred providers
PREFERRED_PROVIDER_u-boot-fw-utils = "libubootenv"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS += " \
                    kernel-image \
                    kernel-modules \
                    "

MACHINE_EXTRA_RRECOMMENDS += "\
                    bootcount-reset \
                    u-boot-fw-utils \
                    linux-firmware-bcm43430 \
                    "

WKS_FILE_DEPENDS ?= " \
                    virtual/trusted-firmware-a \
                    virtual/bootloader \
                    e2fsprogs-native \
                    bmap-tools-native \
                    "

# Wic default support
WKS_FILE ?= "stm32mp15x.wks"

IMAGE_FSTYPES  ?= "tar.bz2 wic.gz wic.bmap"
