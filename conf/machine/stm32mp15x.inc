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

UBOOT_ENTRYPOINT = "0xC2000000"
UBOOT_DTB_LOADADDRESS = "0xC4000000"

SERIAL_CONSOLES       = "115200;ttySTM0"
SERIAL_CONSOLES_CHECK = "${SERIAL_CONSOLES}"

KERNEL_CLASSES  += "kernel-fitimage"
KERNEL_IMAGETYPE = "fitImage"

# Trused Firmware
TFA_DEVICETREE    ?= "${MACHINE}"

# Kernel
KERNEL_DEVICETREE ?= "${MACHINE}.dtb"

# U-Boot
UBOOT_MACHINE     = "stm32mp15_defconfig"
#UBOOT_MACHINE     ?= "stm32mp15_trusted_defconfig"
UBOOT_DEVICETREE  ?= "${MACHINE}"
UBOOT_MAKE_TARGET ?= "DEVICE_TREE=${UBOOT_DEVICETREE} all"

# Optee
OPTEE_PLATFORM   ?= "stm32mp1"
OPTEE_CONF       ?= "${MACHINE}"
OPTEE_DEVICETREE ?= "${MACHINE}.dts"
OPTEE_HEADER     = "tee-header_v2"
OPTEE_PAGER      = "tee-pager_v2"
OPTEE_PAGEABLE   = "tee-pageable_v2"
OPTEE_SUFFIX     = "bin"

MACHINE_FEATURES  += "wifi optee"

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
                    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-test optee-examples optee-client', '', d)} \
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
