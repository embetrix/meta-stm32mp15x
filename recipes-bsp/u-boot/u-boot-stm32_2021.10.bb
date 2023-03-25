require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS += "bc-native dtc-native"
DEPENDS += "python3-setuptools-native"

SRCBRANCH = "v2021.10-stm32mp"
SRCREV = "3984366f6997c680b8c6ccc82d50e77a6e1cccf2"

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
