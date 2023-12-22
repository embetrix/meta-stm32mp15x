require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS += "bc-native dtc-native"
DEPENDS += "python3-setuptools-native"

SRCBRANCH = "v2022.10-stm32mp"
SRCREV = "2c7a6accfa78d34c402fa262bb53f0b952198882"

SRC_URI  = "git://github.com/STMicroelectronics/u-boot.git;protocol=https;branch=${SRCBRANCH}"
SRC_URI += "file://stm32mp15.patch"

UBOOT_INITIAL_ENV = "u-boot-initial-env"

COMPATIBLE_MACHINE = "(stm32mp15x)"

do_deploy:append() {

    install -m 644 ${B}/u-boot-nodtb.bin ${DEPLOYDIR}/u-boot-nodtb-${MACHINE}.bin
    install -m 644 ${B}/u-boot.dtb ${DEPLOYDIR}/u-boot-${MACHINE}.dtb
    cd ${DEPLOYDIR}
    ln -snf  u-boot-nodtb-${MACHINE}.bin u-boot-nodtb.bin
    ln -snf  u-boot-${MACHINE}.dtb u-boot.dtb
    cd -
}
