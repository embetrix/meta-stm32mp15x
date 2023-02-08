require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS += "bc-native dtc-native"

SRCBRANCH = "v2020.01-stm32mp"
SRCREV = "d543131c5835b2ca010d8169a06dbca3014355fd"

SRC_URI  = "git://github.com/STMicroelectronics/u-boot.git;protocol=https;branch=${SRCBRANCH}"
SRC_URI += "file://stm32mp1.h.patch"
SRC_URI += "file://dtc-lexer.l.patch"

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
